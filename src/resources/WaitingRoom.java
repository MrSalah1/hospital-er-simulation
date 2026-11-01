package resources;

import models.Patient;
import models.Doctor;

import java.util.LinkedList;
import java.util.Queue;

public class WaitingRoom {
    private final int capacity;
    private final Semaphore emptyCounter;
    private final Semaphore fullCounter = new Semaphore(0);
    private final Semaphore mutex = new Semaphore(1);

    private final Queue<Patient> PatientQ = new LinkedList<>();


    WaitingRoom(int capacity) {
        this.capacity = capacity;
        this.emptyCounter = new Semaphore(capacity);
    }

    public void EnterPatient(Patient patient) throws InterruptedException {
        System.out.println(patient+ " arrived at the hospital.");

        if (!emptyCounter.tryAcquire(0)) {
            System.out.println("The waiting room is full.");
            System.out.println(patient+ " is waiting outside for an available seat.");
            emptyCounter.acquire();
        }

        mutex.acquire();
        PatientQ.add(patient);
        System.out.println(patient+ " entered the waiting room.");
        mutex.release();

        fullCounter.release();
    }

    public Patient DoctorgetsPatient(Doctor doctor) throws InterruptedException {
        fullCounter.acquire();

        mutex.acquire();
        Patient p = PatientQ.remove();
        System.out.println(doctor+ " is assigned to "+ p);
        mutex.release();

        emptyCounter.release();
        return p;
    }

}