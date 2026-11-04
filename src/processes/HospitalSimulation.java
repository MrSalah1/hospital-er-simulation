package processes;

import models.Doctor;
import models.Patient;
import resources.TreatmentRooms;
import resources.WaitingRoom;
import util.ConsoleLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HospitalSimulation {
    private final int waitingRoomCapacity;
    private final int numDoctors;
    private final int numPatients;

    private final WaitingRoom waitingRoom;
    private final TreatmentRooms treatmentRooms;
    private final List<Doctor> doctors;
    private final List<Patient> patients;

    private Thread arrivalThread;
    private final List<Thread> doctorThreads = new ArrayList<>();
    private final AtomicInteger treatedCount = new AtomicInteger(0);

    public HospitalSimulation(int waitingRoomCapacity, int numDoctors, int numPatients) {
        this.waitingRoomCapacity = waitingRoomCapacity;
        this.numDoctors = numDoctors;
        this.numPatients = numPatients;


        this.waitingRoom = new WaitingRoom(waitingRoomCapacity);
        this.treatmentRooms = new TreatmentRooms(2);
        this.doctors = new ArrayList<>();
        for (int i = 1; i <= numDoctors; i++) {
            doctors.add(new Doctor(i));
        }

        this.patients = new ArrayList<>();
        for (int i = 1; i <= numPatients; i++) {
            patients.add(new Patient(i, waitingRoom));
        }
    }

    public void startSimulation() {

        arrivalThread = new Thread(new PatientArrivalProcess(patients));
        arrivalThread.start();

        for (Doctor doctor : doctors) {
            Thread doctorThread = new Thread(new TreatmentProcess(doctor, waitingRoom, treatmentRooms, numPatients, treatedCount));
            doctorThreads.add(doctorThread);
            doctorThread.start();
        }

        try {
            arrivalThread.join();

            while (treatedCount.get() < numPatients) {
                Thread.sleep(100);
            }

            // Stop doctor threads and join
            for (Thread doctorThread : doctorThreads) {
                doctorThread.interrupt();
            }
            for (Thread doctorThread : doctorThreads) {
                doctorThread.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ConsoleLogger.logAll(
                "All patients are treated.",
                "Simulation completed.");
    }

    public static void main(String[] args) {
        //HospitalSimulation simulation = new HospitalSimulation(2, 3, 5);
        //simulation.startSimulation();
    }
}
