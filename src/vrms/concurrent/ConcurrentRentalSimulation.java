package vrms.concurrent;

import java.time.LocalDateTime;

import vrms.model.Customer;
import vrms.model.Rental;
import vrms.model.Vehicle;
import vrms.repository.FileStorage;
import vrms.repository.Repository;
import vrms.service.RentalService;

public class ConcurrentRentalSimulation {

    public static void main(String[] args) {
        runTests();
    }

    public static void runTests() {
        System.out.println(
            "\n=== TEST 1: TWO CUSTOMERS REQUEST THE SAME VEHICLE ==="
        );
        System.out.println(
            "Expected: one accepted request and one rejected request."
        );
        System.out.println(
            "Expected: one new active rental and vehicle status RENTED."
        );

        for (int attempt = 1; attempt <= 10; attempt++) {
            System.out.println("\n--- Attempt " + attempt + "/10 ---");
            runSimulation(false);
        }

        System.out.println(
            "\n=== TEST 2: INTERRUPT BOTH REQUESTS BEFORE RENTAL ==="
        );
        System.out.println(
            "Expected: both requests report interruption."
        );
        System.out.println(
            "Expected: no rental created and vehicle remains AVAILABLE."
        );

        runSimulation(true);

        System.out.println("\nAll concurrency tests passed.");
    }

    private static void runSimulation(boolean interruptionTest) {
        FileStorage fileStorage = new FileStorage();

        Repository<Vehicle> vehicles = new Repository<>();
        Repository<Customer> customers = new Repository<>();
        Repository<Rental> rentals = new Repository<>();

        vehicles.saveAll(fileStorage.loadVehicles("data/vehicles.txt"));
        customers.saveAll(fileStorage.loadCustomers("data/customers.txt"));
        rentals.saveAll(fileStorage.loadRentals("data/rentals.txt"));

        Vehicle vehicle = vehicles.findById("V01");
        Customer firstCustomer = customers.findById("C01");
        Customer secondCustomer = customers.findById("C02");

        if (vehicle == null|| firstCustomer == null|| secondCustomer == null) {
            throw new IllegalStateException("Simulation data is missing");
        }

        if (vehicle.getStatus() != Vehicle.VehicleStatus.AVAILABLE) {
            throw new IllegalStateException("Test vehicle must be available");
        }

        if (countActiveRentals(rentals, vehicle.getId()) != 0) {
            throw new IllegalStateException("Test vehicle must have no active rental");
        }

        if (rentals.findById("SIM-01") != null || rentals.findById("SIM-02") != null) {
            throw new IllegalStateException("Simulation rental IDs must be unused");
        }

        int initialRentalCount = rentals.findAll().size();

        RentalService service =
            new RentalService(vehicles, customers, rentals);

        LocalDateTime start = LocalDateTime.of(2026, 9, 22, 10, 0);
        LocalDateTime end = start.plusHours(2);

        RentalRequestTask firstRequest = new RentalRequestTask(service,"SIM-01",vehicle.getId(),firstCustomer.getId(),start,end);
        RentalRequestTask secondRequest = new RentalRequestTask(service,"SIM-02",vehicle.getId(),secondCustomer.getId(),start,end);

        Thread firstThread = new Thread(firstRequest, "customer-1");
        Thread secondThread = new Thread(secondRequest, "customer-2");

        if (interruptionTest) {
            synchronized (service) {
                firstThread.start();
                secondThread.start();

                firstThread.interrupt();
                secondThread.interrupt();
            }
        } else {
            firstThread.start();
            secondThread.start();
        }

        awaitWorkers(firstThread, secondThread);

        int successes = 0;

        if (firstRequest.isSuccessful()) {
            successes++;
        }

        if (secondRequest.isSuccessful()) {
            successes++;
        }

        int activeRentals = countActiveRentals(
            rentals, vehicle.getId()
        );

        int newRentals = rentals.findAll().size() - initialRentalCount;

        if (interruptionTest) {
            if (successes != 0 || activeRentals != 0|| newRentals != 0|| vehicle.getStatus() != Vehicle.VehicleStatus.AVAILABLE|| firstRequest.getRental() != null|| secondRequest.getRental() != null) {
                throw new IllegalStateException("Interrupted requests must leave the initial state unchanged");
            }

            if (!"Rental request was interrupted".equals(firstRequest.getFailureMessage())|| !"Rental request was interrupted".equals(secondRequest.getFailureMessage())) {
                throw new IllegalStateException("Both requests must report interruption");
            }
        } else {
            if (successes != 1) {
                throw new IllegalStateException(
                    "Expected exactly one successful request"
                );
            }

            if (activeRentals != 1 || newRentals != 1) {
                throw new IllegalStateException(
                    "Expected exactly one new active rental"
                );
            }

            if (vehicle.getStatus() != Vehicle.VehicleStatus.RENTED) {
                throw new IllegalStateException(
                    "Vehicle should be rented"
                );
            }

            RentalRequestTask rejectedRequest;

            if (firstRequest.isSuccessful()) {
                rejectedRequest = secondRequest;
            } else {
                rejectedRequest = firstRequest;
            }

            if (rejectedRequest.getFailureMessage() == null) {
                throw new IllegalStateException("Rejected request must report a reason");
            }
        }

        if (firstRequest.isSuccessful()) {
            System.out.println(
                "Customer " + firstCustomer.getId() + ": ACCEPTED"
            );
        } else {
            System.out.println(
                "Customer " + firstCustomer.getId()
                + ": REFUSED - " + firstRequest.getFailureMessage()
            );
        }

        if (secondRequest.isSuccessful()) {
            System.out.println(
                "Customer " + secondCustomer.getId() + ": ACCEPTED"
            );
        } else {
            System.out.println(
                "Customer " + secondCustomer.getId()
                + ": REFUSED - " + secondRequest.getFailureMessage()
            );
        }

        int expectedSuccesses;
        Vehicle.VehicleStatus expectedStatus;

        if (interruptionTest) {
            expectedSuccesses = 0;
            expectedStatus = Vehicle.VehicleStatus.AVAILABLE;
        } else {
            expectedSuccesses = 1;
            expectedStatus = Vehicle.VehicleStatus.RENTED;
        }

        System.out.println(
            "Successful requests: " + successes + " expected: " + expectedSuccesses
        );

        System.out.println(
            "New rentals: " + newRentals+ "  expected: " + expectedSuccesses
        );

        System.out.println(
            "Active rentals for " + vehicle.getId() + ": " + activeRentals + " expected: " + expectedSuccesses
        );

        System.out.println(
            "Vehicle status: " + vehicle.getStatus() + " expected: " + expectedStatus
        );

        System.out.println("Both worker threads terminated.");
        System.out.println("Result: PASS");
    }

    private static int countActiveRentals(
            Repository<Rental> rentals,
            String vehicleId) {

        int count = 0;

        for (Rental rental : rentals.findAll()) {
            if (vehicleId.equals(rental.getVehicleId()) && rental.isActive()) {
                count++;
            }
        }

        return count;
    }

    private static void awaitWorkers(Thread first, Thread second) {
        boolean interrupted = false;

        try {
            first.join(5000);
            second.join(5000);
        } catch (InterruptedException e) {
            interrupted = true;
        }

        if (!interrupted && !first.isAlive() && !second.isAlive()) {
            return;
        }

        first.interrupt();
        second.interrupt();

        Thread[] workers = { first, second };

        for (Thread worker : workers) {
            try {
                worker.join(5000);
            } catch (InterruptedException e) {
                interrupted = true;
            }
        }

        boolean stillRunning = first.isAlive() || second.isAlive();

        if (interrupted) {
            Thread.currentThread().interrupt();
        }

        if (stillRunning) {
            throw new IllegalStateException("Workers did not terminate within the allowed time");
        }

        throw new IllegalStateException("Simulation cancelled after interruption or timeout");
    }
}