package dungeonmania.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.MovedAwayBehaviour;
import dungeonmania.entities.OverlapBehaviour;
import dungeonmania.entities.Player;
import dungeonmania.entities.Portal;
import dungeonmania.entities.PotionListener;
import dungeonmania.entities.OnDestroyBehaviour;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.ZombieToastSpawner;
import dungeonmania.entities.logicals.CurrentObserver;
import dungeonmania.entities.logicals.CurrentSubject;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class GameMap {
    private Game game;
    private Map<Position, GraphNode> nodes = new HashMap<>();
    private Player player;

    /**
     * Initialise the game map
     * 1. pair up portals
     * 2. register all movables
     * 3. register all spawners
     * 4. register bombs and switches
     * 5. more...
     */
    public void init() {
        initPairPortals();
        initRegisterMovables();
        initRegisterSpawners();
        initRegisterLogicConnections();
        initPotionListeners();
    }

    private void initRegisterLogicConnections() {
        GameMap map = game.getMap();
        List<CurrentSubject> conductors = getConductors();

        for (CurrentSubject subject : conductors) {
            List<Position> positions = subject.getCardinallyAdjacentPositions();
            for (Position p : positions) {
                List<Entity> entities = getEntities(p);
                for (Entity e : entities) {
                    if (e instanceof CurrentObserver) {
                        CurrentObserver observer = (CurrentObserver) e;
                        subject.attach(observer, map);
                    }
                }
            }
        }
    }

    // Pair up portals if there's any
    private void initPairPortals() {
        Map<String, Portal> portalsMap = new HashMap<>();
        nodes.forEach((k, v) -> {
            v.getEntities().stream().filter(Portal.class::isInstance).map(Portal.class::cast).forEach(portal -> {
                String color = portal.getColor();
                if (portalsMap.containsKey(color)) {
                    portal.bind(portalsMap.get(color));
                } else {
                    portalsMap.put(color, portal);
                }
            });
        });
    }

    private void initRegisterMovables() {
        List<Enemy> enemies = getEntities(Enemy.class);
        enemies.forEach(e -> {
            game.register(() -> e.move(game), Game.AI_MOVEMENT, e.getId());
        });
    }

    private void initRegisterSpawners() {
        List<ZombieToastSpawner> zts = getEntities(ZombieToastSpawner.class);
        zts.forEach(e -> {
            game.register(() -> e.spawn(game), Game.AI_MOVEMENT, e.getId());
        });
        game.register(() -> game.getEntityFactory().spawnSpider(game), Game.AI_MOVEMENT, "spawnSpiders");
    }

    private void initPotionListeners() {
        getEntities().stream().filter(e -> e instanceof PotionListener)
                .forEach(e -> registerPotionListener((PotionListener) e));
    }

    public void registerPotionListener(PotionListener e) {
        player.registerPotionListener(e);
    }

    public void moveTo(Entity entity, Position position) {
        if (!canMoveTo(entity, position))
            return;

        triggerMovingAwayEvent(entity);
        removeNode(entity);
        entity.setPosition(position);
        addEntity(entity);
        triggerOverlapEvent(entity);
    }

    public void moveTo(Entity entity, Direction direction) {
        if (!canMoveTo(entity, Position.translateBy(entity.getPosition(), direction)))
            return;
        triggerMovingAwayEvent(entity);
        removeNode(entity);
        entity.translate(direction);
        addEntity(entity);
        triggerOverlapEvent(entity);
    }

    private void triggerMovingAwayEvent(Entity entity) {
        List<Runnable> callbacks = new ArrayList<>();
        getEntities(entity.getPosition()).forEach(e -> {
            if (e != entity && e instanceof MovedAwayBehaviour)
                callbacks.add(() -> ((MovedAwayBehaviour) e).onMovedAway(this, entity));
        });
        callbacks.forEach(callback -> {
            callback.run();
        });
    }

    private void triggerOverlapEvent(Entity entity) {
        List<Runnable> overlapCallbacks = new ArrayList<>();
        getEntities(entity.getPosition()).forEach(e -> {
            if (e instanceof OverlapBehaviour) {
                OverlapBehaviour overlappable = (OverlapBehaviour) e;
                overlapCallbacks.add(() -> overlappable.onOverlap(this, entity));
            }
        });
        overlapCallbacks.forEach(callback -> {
            callback.run();
        });
    }

    public boolean canMoveTo(Entity entity, Position position) {
        return !nodes.containsKey(position) || nodes.get(position).canMoveOnto(this, entity);
    }

    public Position dijkstraPathFind(Position src, Position dest, Entity entity) {
        // if inputs are invalid, don't move
        if (!nodes.containsKey(src) || !nodes.containsKey(dest))
            return src;

        Map<Position, Integer> dist = new HashMap<>();
        Map<Position, Position> prev = new HashMap<>();
        Map<Position, Boolean> visited = new HashMap<>();

        prev.put(src, null);
        dist.put(src, 0);

        PriorityQueue<Position> q = new PriorityQueue<>((x, y) -> Integer
                .compare(dist.getOrDefault(x, Integer.MAX_VALUE), dist.getOrDefault(y, Integer.MAX_VALUE)));
        q.add(src);

        while (!q.isEmpty()) {
            Position curr = q.poll();
            if (curr.equals(dest) || dist.get(curr) > 200)
                break;
            // check portal
            if (nodes.containsKey(curr) && nodes.get(curr).getEntities().stream().anyMatch(Portal.class::isInstance)) {
                Portal portal = nodes.get(curr).getEntities().stream().filter(Portal.class::isInstance)
                        .map(Portal.class::cast).collect(Collectors.toList()).get(0);
                List<Position> teleportDest = portal.getDestPositions(this, entity);
                teleportDest.stream().filter(p -> !visited.containsKey(p)).forEach(p -> {
                    dist.put(p, dist.get(curr));
                    prev.put(p, prev.get(curr));
                    q.add(p);
                });
                continue;
            }
            visited.put(curr, true);
            List<Position> neighbours = curr.getCardinallyAdjacentPositions().stream()
                    .filter(p -> !visited.containsKey(p))
                    .filter(p -> !nodes.containsKey(p) || nodes.get(p).canMoveOnto(this, entity))
                    .collect(Collectors.toList());

            neighbours.forEach(n -> {
                int newDist = dist.get(curr) + (nodes.containsKey(n) ? nodes.get(n).getWeight() : 1);
                if (newDist < dist.getOrDefault(n, Integer.MAX_VALUE)) {
                    q.remove(n);
                    dist.put(n, newDist);
                    prev.put(n, curr);
                    q.add(n);
                }
            });
        }
        Position ret = dest;
        if (prev.get(ret) == null || ret.equals(src))
            return src;
        while (!prev.get(ret).equals(src)) {
            ret = prev.get(ret);
        }
        return ret;
    }

    public void removeNode(Entity entity) {
        Position p = entity.getPosition();
        if (nodes.containsKey(p)) {
            nodes.get(p).removeEntity(entity);
            if (nodes.get(p).size() == 0) {
                nodes.remove(p);
            }
        }
    }

    public void destroyEntitiesOnPosition(int x, int y) {
        List<Entity> entities = getEntities(new Position(x, y));
        entities = entities.stream().filter(e -> !(e instanceof Player)).collect(Collectors.toList());
        for (Entity e : entities)
            destroyEntity(e);
    }

    public void destroyEntity(Entity entity) {
        removeNode(entity);
        if (entity instanceof OnDestroyBehaviour) {
            ((OnDestroyBehaviour) entity).onDestroy(this);
        }
    }

    public void addEntity(Entity entity) {
        addNode(new GraphNode(entity));
    }

    public void addNode(GraphNode node) {
        Position p = node.getPosition();

        if (!nodes.containsKey(p))
            nodes.put(p, node);
        else {
            GraphNode curr = nodes.get(p);
            curr.mergeNode(node);
            nodes.put(p, curr);
        }
    }

    public Entity getEntity(String id) {
        Entity res = null;
        for (Map.Entry<Position, GraphNode> entry : nodes.entrySet()) {
            List<Entity> es = entry.getValue().getEntities().stream().filter(e -> e.getId().equals(id))
                    .collect(Collectors.toList());
            if (es != null && es.size() > 0) {
                res = es.get(0);
                break;
            }
        }
        return res;
    }

    public List<Entity> getEntities(Position p) {
        GraphNode node = nodes.get(p);
        return (node != null) ? node.getEntities() : new ArrayList<>();
    }

    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        nodes.forEach((k, v) -> entities.addAll(v.getEntities()));
        return entities;
    }

    public <T extends Entity> List<T> getEntities(Class<T> type) {
        return getEntities().stream().filter(type::isInstance).map(type::cast).collect(Collectors.toList());
    }

    private List<CurrentSubject> getConductors() {
        List<CurrentSubject> conductors = getEntities().stream().filter(e -> e instanceof CurrentSubject)
                .map(e -> (CurrentSubject) e).collect(Collectors.toList());
        return conductors;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
