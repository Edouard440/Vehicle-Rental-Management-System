package vrms.concurrent;

import java.time.LocalDateTime;

import vrms.model.Customer;
import vrms.model.Rental;
import vrms.model.Vehicle;
import vrms.repository.FileStorage;
import vrms.repository.Repository;
import vrms.service.RentalService;

public class ConcurrentRentalSimulation{
    public static void main (String [] args){

        FileStorage fileStorage = new FileStorage();
        Repository<Vehicle> vehicles = new Repository<>();
        Repository<Customer> customers = new Repository<>();
        Repository<Rental> rentals = new Repository<>();

        vehicles.saveAll(fileStorage.loadVehicles("data/vehicles.txt"));
        customers.saveAll(fileStorage.loadCustomers("data/customers.txt"));
        rentals.saveAll(fileStorage.loadRentals("data/rentals.txt"));

        RentalService service = new RentalService(vehicles, customers, rentals);

        Vehicle vehicle = vehicles.findById("V01");
        Customer firstCustomer = customers.findById("C01");
        Customer secondCustomer = customers.findById("C02");

        if (vehicle == null || firstCustomer == null || secondCustomer == null) {
            throw new IllegalStateException("Simulation data is missing");
        }

        LocalDateTime start = LocalDateTime.of(2026, 9, 22, 10, 0);
        LocalDateTime end = start.plusHours(2);

        RentalRequestTask firstRequest = new RentalRequestTask(service, "SIM-01", vehicle.getId(), firstCustomer.getId(), start, end);
        RentalRequestTask secondRequest = new RentalRequestTask(service, "SIM-02", vehicle.getId(), secondCustomer.getId(), start, end);

        Thread firstThread = new Thread(firstRequest, "customer-1");
        Thread secondThread = new Thread(secondRequest, "customer-2");

        firstThread.start();
        secondThread.start();

        try {
            firstThread.join(5000);
            secondThread.join(5000);

            if (firstThread.isAlive() || secondThread.isAlive()) {
                firstThread.interrupt();
                secondThread.interrupt();

                System.out.println("Simulation timeout: results are not final.");
                return;
            }

        } catch (InterruptedException e) {
            firstThread.interrupt();
            secondThread.interrupt();

            Thread.currentThread().interrupt();
            return;
        }

        int successes = 0;
        if (firstRequest.isSuccessful()) {
            successes++;
        }
        if (secondRequest.isSuccessful()) {
            successes++;
        }
        System.out.println("Successful requests: " + successes);
        if (successes != 1) {
            throw new IllegalStateException("Expected exactly one successful request");
            
        }
        int activeRentals = 0;
        for (Rental rental : rentals.findAll()) {
            if (rental.getVehicleId().equals(vehicle.getId()) && rental.isActive()) {
                activeRentals++;
            }
        }

        if (activeRentals != 1) {
            throw new IllegalStateException("Expected exactly one active rental for this vehicle"
            );
        }

        if (vehicle.getStatus() != Vehicle.VehicleStatus.RENTED) {
            throw new IllegalStateException("Vehicle should be rented");
        }

        System.out.println("Concurrent rental test passed.");
    }

}