package processes;

import models.Doctor;
import models.Patient;
import resources.TreatmentRooms;
import resources.WaitingRoom;
import util.ConsoleLogger;

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

            try {
                Patient patient = waitingRoom.DoctorgetsPatient(doctor);

                ConsoleLogger.log(doctor + " is trying to acquire a treatment room.");

                boolean acquired = treatmentRooms.acquireRoom(3000);

                if (acquired) {
                    ConsoleLogger.logAll(
                            doctor + " acquired a treatment room.",
                            doctor + " begins treating " + patient + ".");

                    try {
                        int treatmentTime = ThreadLocalRandom.current().nextInt(1000, 6001);

                        Thread.sleep(5000);
                    } finally {
                        treatmentRooms.releaseRoom();
                    }

                    ConsoleLogger.logAll(
                            doctor + " finished treating " + patient + ".",
                            doctor + " released the treatment room.");

                    treatedCount.incrementAndGet();

                    ConsoleLogger.logAll(
                            doctor + " is now available.",
                            patient + " left.");
                } else {
                    ConsoleLogger.logAll(
                            doctor + " could not acquire a treatment room.",
                            doctor + " is sending " + patient + " back to the waiting room.");

                    waitingRoom.returnPatient(patient);

                    ConsoleLogger.log(doctor + " is now available.");
                }

            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
