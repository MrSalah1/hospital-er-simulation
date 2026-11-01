package processes;

import models.Patient;
import resources.WaitingRoom;

public class PatientArrivalProcess implements Runnable {
    private final WaitingRoom waitingRoom;
    private final int numPatients;

    public PatientArrivalProcess(WaitingRoom waitingRoom, int numPatients) {
        this.waitingRoom = waitingRoom;
        this.numPatients = numPatients;
    }

    @Override
    public void run() {
        System.out.println("Patients arriving:");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= numPatients; i++) {
            sb.append("P").append(i).append(i == numPatients ? "" : " ");
        }
        System.out.println(sb.toString());

        for (int i = 1; i <= numPatients; i++) {
            Patient patient = new Patient(i);
            try {
                waitingRoom.EnterPatient(patient);
                Thread.sleep(300); // Small delay between patient arrivals
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
