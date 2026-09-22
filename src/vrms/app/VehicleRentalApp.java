package vrms.app;

import java.time.LocalDateTime;

import vrms.exception.RentalException;
import vrms.model.Car;
import vrms.model.Customer;
import vrms.model.Rental;
import vrms.model.Vehicle;
import vrms.repository.Repository;
import vrms.service.RentalService;
import vrms.repository.FileStorage;
import java.util.List;

public class VehicleRentalApp {
    public static void main(String [] args){
        Repository<Vehicle> vehicles = new Repository<>();
        Repository<Customer> customers = new Repository<>();
        Repository<Rental> rentals = new Repository<>();
        FileStorage fileStorage = new FileStorage();

        List<Vehicle> loadVehicles = fileStorage.loadVehicles("data/vehicles.txt");
        List<Customer> loadCustomers = fileStorage.loadCustomers("data/customers.txt");
        List<Rental> loadRentals = fileStorage.loadRentals("data/rentals.txt");

        RentalService service = new RentalService(vehicles, customers, rentals);

        vehicles.saveAll(loadVehicles);
        customers.saveAll(loadCustomers);
        rentals.saveAll(loadRentals);

        System.out.println("Vehicles loaded: " + vehicles.findAll().size());
        System.out.println("Customers loaded: " + customers.findAll().size());
        System.out.println("Rentals loaded: " + rentals.findAll().size());

        ConsoleMenu menu = new ConsoleMenu(service);
        menu.start();

    }
}