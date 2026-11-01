package processes;

import models.Doctor;
import models.Patient;
import resources.TreatmentRooms;
import resources.WaitingRoom;

import java.util.concurrent.atomic.AtomicInteger;

public class TreatmentProcess implements Runnable {
    private final Doctor doctor;
    private final WaitingRoom waitingRoom;
    private final TreatmentRooms treatmentRooms;
    private final int totalPatients;
    private final AtomicInteger treatedCount;

    public TreatmentProcess(Doctor doctor, WaitingRoom waitingRoom, TreatmentRooms treatmentRooms, int totalPatients, AtomicInteger treatedCount) {
        this.doctor = doctor;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
        this.totalPatients = totalPatients;
        this.treatedCount = treatedCount;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && treatedCount.get() < totalPatients) {
            try {
                Patient patient = waitingRoom.DoctorgetsPatient(doctor);

                System.out.println(doctor + " is trying to acquire a treatment room.");
                boolean acquired = treatmentRooms.acquireRoom(3000);

                if (acquired) {
                    System.out.println(doctor + " acquired a treatment room.");
                    System.out.println(doctor + " begins treating " + patient + ".");

                    // Simulate treatment for random period of time
                    Thread.sleep(1000 + (long) (Math.random() * 1500));

                    System.out.println(doctor + " finished treating " + patient + ".");
                    treatmentRooms.releaseRoom();
                    System.out.println(doctor + " released the treatment room.");
                    System.out.println(doctor + " is now available.");
                    System.out.println(patient + " left.");

                    treatedCount.incrementAndGet();
                } else {
                    System.out.println(doctor + " could not acquire a treatment room.");
                    System.out.println(doctor + " is sending " + patient + " back to the waiting room.");
                    waitingRoom.EnterPatient(patient);
                    System.out.println(doctor + " is now available.");
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
