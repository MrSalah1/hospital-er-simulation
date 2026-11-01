package resources;

public class TreatmentRooms {
    private final Semaphore semaphore;

    public TreatmentRooms(int numOfRooms) {
        semaphore = new Semaphore(numOfRooms);
    }

    public boolean acquireRoom(long timeout) throws InterruptedException {
        return semaphore.tryAcquire(timeout);
    }

    public void releaseRoom() {
        semaphore.release();
    }
}