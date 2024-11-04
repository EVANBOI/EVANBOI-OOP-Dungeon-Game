package dungeonmania.entities.logicals;

public interface CurrentObserver {
    public void updateCurrent(CurrentSubject subject, boolean current);

    public void attachToSubject(CurrentSubject subject, boolean current);

    public void detachFromSubject(CurrentSubject subject, boolean current);

    public String getId();
}
