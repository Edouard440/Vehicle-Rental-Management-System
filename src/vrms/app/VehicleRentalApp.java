package vrms.app;


import vrms.model.Customer;
import vrms.model.Rental;
import vrms.model.Vehicle;
import vrms.repository.Repository;
import vrms.service.RentalService;
import vrms.repository.FileStorage;
import java.util.List;
import vrms.report.FleetReport;
import vrms.report.PricingInspector;
import vrms.service.pricing.DailyPricingPolicy;
import vrms.service.pricing.HourlyPricingPolicy;
import vrms.service.pricing.WeeklyPricingPolicy;

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

        FleetReport fleetReport = new FleetReport();
        fleetReport.printFleetReport(vehicles.findAll());
        fleetReport.printFleetSummary(vehicles.findAll());
        fleetReport.printAvailableVehicles(vehicles.findAll());

        PricingInspector.inspectPricingPolicy(DailyPricingPolicy.class);
        PricingInspector.inspectPricingPolicy(HourlyPricingPolicy.class);
        PricingInspector.inspectPricingPolicy(WeeklyPricingPolicy.class);

        ConsoleMenu menu = new ConsoleMenu(service);
        menu.start();

    }
}