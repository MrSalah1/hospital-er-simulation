import processes.HospitalSimulation;

import java.util.Scanner;

public class HospitalMain {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Waiting room capacity:");
            int waitingRoomCapacity = scanner.nextInt();

            System.out.println("Number of doctors:");
            int numDoctors = scanner.nextInt();

            System.out.println("Number of patients:");
            int numPatients = scanner.nextInt();

            if (waitingRoomCapacity <= 0 || numDoctors <= 0 || numPatients <= 0) {
                System.out.println("Waiting room capacity, number of doctors, and number of patients must be positive.");
                return;
            }

            HospitalSimulation simulation = new HospitalSimulation(waitingRoomCapacity, numDoctors, numPatients);
            simulation.startSimulation();
        }
    }
}
