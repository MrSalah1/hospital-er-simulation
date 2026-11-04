package processes;

import models.Doctor;
import models.Patient;
import resources.TreatmentRooms;
import resources.WaitingRoom;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class TreatmentProcess implements Runnable{
    private final Doctor doctor;
    private final WaitingRoom waitingRoom;
    private final TreatmentRooms treatmentRooms;
    private final int numPatients;
    private final AtomicInteger treatedCount;

    public TreatmentProcess(
            Doctor doctor,
            WaitingRoom waitingRoom,
            TreatmentRooms treatmentRooms,
            int numPatients,
            AtomicInteger treatedCount) {

        this.doctor = doctor;
        this.waitingRoom = waitingRoom;
        this.treatmentRooms = treatmentRooms;
        this.numPatients = numPatients;
        this.treatedCount = treatedCount;
    }

    @Override
    public void run() {

        while (treatedCount.get() < numPatients) {

            try{
                Patient patient = waitingRoom.DoctorgetsPatient(doctor);

                System.out.println(doctor + " is trying to acquire a treatment room.");

                boolean acquired = treatmentRooms.acquireRoom(3000);

                if (acquired) {
                    System.out.println(doctor + " acquired a treatment room.");
                    System.out.println(doctor + " begins treating " + patient + ".");

                    try {
                        int treatmentTime = ThreadLocalRandom.current().nextInt(1000, 3001);

                        Thread.sleep(treatmentTime);

                        System.out.println(doctor + " finished treating " + patient + ".");

                    } finally {
                        treatmentRooms.releaseRoom();
                        System.out.println(doctor + " released the treatment room.");
                    }

                    treatedCount.incrementAndGet();

                    System.out.println(doctor + " is now available.");
                    System.out.println(patient + " left.");

                } else {

                    System.out.println(doctor + " could not acquire a treatment room.");

                    System.out.println(doctor + " is sending " + patient
                            + " back to the waiting room.");

                    waitingRoom.returnPatient(patient);

                    System.out.println(doctor + " is now available.");
                }
                
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
