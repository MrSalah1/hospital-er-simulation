package processes;

import models.Doctor;
import resources.TreatmentRooms;
import resources.WaitingRoom;

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

    private Thread arrivalThread;
    private final List<Thread> doctorThreads = new ArrayList<>();
    private final AtomicInteger treatedCount = new AtomicInteger(0);

    public HospitalSimulation(int waitingRoomCapacity, int numDoctors, int numPatients) {
        this.waitingRoomCapacity = waitingRoomCapacity;
        this.numDoctors = numDoctors;
        this.numPatients = numPatients;

        // 1. Create WaitingRoom(capacity), TreatmentRooms(2), doctors list
        this.waitingRoom = new WaitingRoom(waitingRoomCapacity);
        this.treatmentRooms = new TreatmentRooms(2);
        this.doctors = new ArrayList<>();
        for (int i = 1; i <= numDoctors; i++) {
            doctors.add(new Doctor(i));
        }
    }

    public void startSimulation() {
        // 2. Create/start arrival thread + one doctor thread each
        arrivalThread = new Thread(new PatientArrivalProcess(waitingRoom, numPatients));
        arrivalThread.start();

        for (Doctor doctor : doctors) {
            Thread doctorThread = new Thread(new TreatmentProcess(doctor, waitingRoom, treatmentRooms, numPatients, treatedCount));
            doctorThreads.add(doctorThread);
            doctorThread.start();
        }

        // 3. join() all threads, then print a summary
        try {
            arrivalThread.join();

            // Wait until all patients are treated
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

        System.out.println("All patients have been treated.");
        System.out.println("Simulation completed.");
    }

    public static void main(String[] args) {
        HospitalSimulation simulation = new HospitalSimulation(2, 3, 5);
        simulation.startSimulation();
    }
}
