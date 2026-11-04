package processes;

import models.Patient;
import util.ConsoleLogger;

import java.util.List;

public class PatientArrivalProcess implements Runnable {
    private static final long ARRIVAL_DELAY_MS = 200;

    private final List<Patient> patients;

    public PatientArrivalProcess(List<Patient> patients) {
        this.patients = patients;
    }

    @Override
    public void run() {
        for (Patient patient : patients) {
            ConsoleLogger.log(patient + " arrived at the hospital.");
            patient.start();

            try {
                Thread.sleep(ARRIVAL_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
