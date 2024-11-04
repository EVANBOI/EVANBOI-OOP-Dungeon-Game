package dungeonmania.entities.collectables;

import dungeonmania.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.OverlapBehaviour;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.entities.logicals.CurrentObserver;
import dungeonmania.entities.logicals.CurrentSubject;
import dungeonmania.entities.logicals.logics.Logic;
import dungeonmania.entities.logicals.logics.LogicFactory;
import dungeonmania.map.GameMap;

public class Bomb extends InventoryItem implements OverlapBehaviour, CurrentObserver {
    public enum State {
        SPAWNED, INVENTORY, PLACED
    }

    public static final int DEFAULT_RADIUS = 1;
    private State state;
    private int radius;
    private boolean explode = false;
    private Logic logic;

    private List<CurrentSubject> observing = new ArrayList<>();

    public Bomb(Position position, int radius, String logic) {
        super(position);
        state = State.SPAWNED;
        this.radius = radius;
        this.logic = LogicFactory.getLogic(logic);
    }

    public void onOverlap(GameMap map, Entity entity) {
        if (state != State.SPAWNED)
            return;
        if (entity instanceof Player) {
            observing.stream().forEach(s -> s.detach(this));
            if (!((Player) entity).pickUp(this))
                return;
            map.destroyEntity(this);
        }
        this.state = State.INVENTORY;
    }

    public int getRadius() {
        return radius;
    }

    public void onPutDown(GameMap map, Position p) {
        translate(Position.calculatePositionBetween(getPosition(), p));
        map.addEntity(this);
        this.state = State.PLACED;
        List<Position> adjPosList = getCardinallyAdjacentPositions();
        adjPosList.stream().forEach(node -> {
            List<Entity> entities = map.getEntities(node).stream().filter(e -> (e instanceof CurrentSubject))
                    .collect(Collectors.toList());
            entities.stream().map(CurrentSubject.class::cast).forEach(s -> s.attach(this, map));
        });
    }

    public State getState() {
        return state;
    }

    public void bombDetonation(GameMap map) {
        int x = this.getPositionX();
        int y = this.getPositionY();
        for (int i = x - this.getRadius(); i <= x + this.getRadius(); i++) {
            for (int j = y - this.getRadius(); j <= y + this.getRadius(); j++) {
                map.destroyEntitiesOnPosition(i, j);
            }
        }
    }

    public void updateCurrent(CurrentSubject subject, boolean current) {
        logic.updateNumActivated(subject, current);
    }

    public void updateConditionStatus() {
        explode = logic.checkIfFulfilled();
    }

    public boolean willExplode() {
        return explode;
    }

    @Override
    public void attachToSubject(CurrentSubject subject, boolean current) {
        logic.updateAdjacentConductorsAttachment(subject, current);
        observing.add(subject);
    }

    @Override
    public void detachFromSubject(CurrentSubject subject, boolean current) {
        logic.updateAdjacentConductorsDetachment(subject, current);
    }
}
