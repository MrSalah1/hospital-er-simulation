package models;

import resources.WaitingRoom;

public class Patient extends Thread {
    private final int id;
    private final WaitingRoom waitingRoom;


    public Patient(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;

    }

    public int getPId() {
        return id;
    }

    @Override
    public String toString() {
        return "P" + id;
    }

    public void enterWaitingRoom() {
        try {
            waitingRoom.EnterPatient(this);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        System.out.println(this +  " arrived at the hospital.");
        enterWaitingRoom();
    }

    public void reEnterPatient() throws InterruptedException {
//        I don't want the doctor to stuck if he calls enter waiting room and the waiting room is full,
//        so we will make a new independent thread for the patient
        new Thread(this::enterWaitingRoom, "reEnter-P" + id).start();

    }

    public void leavePatient() {
        System.out.println(this + " left.");
    }

}
