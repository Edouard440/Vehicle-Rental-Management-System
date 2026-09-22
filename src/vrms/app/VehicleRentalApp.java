package vrms.app;

import java.util.List;
import vrms.model.Customer;
import vrms.model.Rental;
import vrms.model.Vehicle;
import vrms.repository.FileStorage;
import vrms.repository.Repository;
import vrms.service.RentalService;

public class VehicleRentalApp {
    public static void main(String[] args) {
        Repository<Vehicle> vehicles = new Repository<>();
        Repository<Customer> customers = new Repository<>();
        Repository<Rental> rentals = new Repository<>();
        FileStorage fileStorage = new FileStorage();

        List<Vehicle> loadVehicles = fileStorage.loadVehicles("data/vehicles.txt");
        List<Customer> loadCustomers = fileStorage.loadCustomers("data/customers.txt");
        List<Rental> loadRentals = fileStorage.loadRentals("data/rentals.txt");

        vehicles.saveAll(loadVehicles);
        customers.saveAll(loadCustomers);
        rentals.saveAll(loadRentals);

        System.out.println("=== VRMS Data Initialization ===");
        System.out.println("Vehicles loaded  : " + vehicles.findAll().size());
        System.out.println("Customers loaded : " + customers.findAll().size());
        System.out.println("Rentals loaded   : " + rentals.findAll().size());

        RentalService service = new RentalService(vehicles, customers, rentals);

        ConsoleMenu menu = new ConsoleMenu(service);
        menu.start();
    }
}