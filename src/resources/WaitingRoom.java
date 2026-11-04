package resources;

import models.Patient;
import models.Doctor;
import util.ConsoleLogger;

import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {
    private final int capacity;
    private final Semaphore emptyCounter;
    private final Semaphore fullCounter = new Semaphore(0);
    private final Semaphore mutex = new Semaphore(1);

    private final Queue<Patient> PatientQ = new LinkedList<>();


    public WaitingRoom(int capacity) {
        this.capacity = capacity;
        this.emptyCounter = new Semaphore(capacity);
    }

    public void EnterPatient(Patient patient) throws InterruptedException {

        if (!emptyCounter.tryAcquire(0)) {
            ConsoleLogger.log(patient + " is waiting outside because the waiting room is full.");
            emptyCounter.acquire();
        }

        mutex.acquire();
        PatientQ.add(patient);
        ConsoleLogger.log(patient + " entered the waiting room.");
        mutex.release();

        fullCounter.release();
    }

    public Patient DoctorgetsPatient(Doctor doctor) throws InterruptedException {
        fullCounter.acquire();

        mutex.acquire();
        Patient p = PatientQ.remove();
        ConsoleLogger.log(doctor + " is assigned to " + p);
        mutex.release();

        emptyCounter.release();
        return p;
    }

    public void returnPatient(Patient patient) throws InterruptedException {

        if (!emptyCounter.tryAcquire(0)) {
            ConsoleLogger.log(patient + " is waiting outside because the waiting room is full.");
            emptyCounter.acquire();
        }

        mutex.acquire();
        try {
            PatientQ.add(patient);
            ConsoleLogger.log(patient + " entered the waiting room.");
        } finally {
            mutex.release();
        }

        fullCounter.release();
    }

}