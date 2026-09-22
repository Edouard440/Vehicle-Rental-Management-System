package vrms.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import vrms.exception.RentalException;
import vrms.model.Rental;
import vrms.model.Vehicle;
import vrms.report.FleetReport;
import vrms.report.PricingInspector;
import vrms.service.RentalService;
import vrms.service.pricing.DailyPricingPolicy;
import vrms.service.pricing.HourlyPricingPolicy;
import vrms.service.pricing.WeeklyPricingPolicy;
import vrms.concurrent.ConcurrentRentalSimulation;

public class ConsoleMenu {

    private final RentalService rentalService;
    private final FleetReport fleetReport;
    private final Scanner scanner;

    public ConsoleMenu(RentalService rentalService) {
        this.rentalService = rentalService;
        this.fleetReport = new FleetReport();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleListVehicles();
                    break;
                case "2":
                    handleListAvailableVehicles();
                    break;
                case "3":
                    handleRentVehicle();
                    break;
                case "4":
                    handleReturnVehicle();
                    break;
                case "5":
                    handleFleetSummary();
                    break;
                case "6":
                    handleInspectPricingPolicies();
                    break;
                case "7":
                    try {
                        ConcurrentRentalSimulation.runTests();
                    } catch (IllegalStateException e) {
                        System.err.println("Simulation failed: " + e.getMessage());
                    }

                    if (Thread.currentThread().isInterrupted()) {
                        running = false;
                    }
                    break;
                case "0":
                    System.out.println("Shutting down the system. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.\n");
                    break;
            }
        }
    }

    private void printMenu() {
        System.out.println("\n========== VRMS - MAIN MENU ==========");
        System.out.println("1. List all vehicles");
        System.out.println("2. List available vehicles");
        System.out.println("3. Rent a vehicle");
        System.out.println("4. Return a vehicle");
        System.out.println("5. Fleet summary report");
        System.out.println("6. Inspect pricing policies");
        System.out.println("7. Run concurrent rental tests");
        
        System.out.println("0. Exit");
        System.out.println("=======================================");
    }

    private void handleListVehicles() {
        System.out.println("\n--- Vehicle Fleet ---");
        List<Vehicle> vehicleList = rentalService.getVehicleRepository().findAll();
        fleetReport.printFleetReport(vehicleList);
    }

    private void handleListAvailableVehicles() {
        System.out.println("\n--- Available Vehicles ---");
        List<Vehicle> vehicleList = rentalService.getVehicleRepository().findAll();
        fleetReport.printAvailableVehicles(vehicleList);
    }

    private void handleFleetSummary() {
        System.out.println("\n--- Fleet Analytics & Summary ---");
        List<Vehicle> vehicleList = rentalService.getVehicleRepository().findAll();
        fleetReport.printFleetSummary(vehicleList);
    }

    private void handleInspectPricingPolicies() {
        System.out.println("\n--- Pricing Policies Inspection ---");
        PricingInspector.inspectPricingPolicy(HourlyPricingPolicy.class);
        PricingInspector.inspectPricingPolicy(DailyPricingPolicy.class);
        PricingInspector.inspectPricingPolicy(WeeklyPricingPolicy.class);
    }

    private void handleRentVehicle() {
        System.out.println("\n--- New Rental ---");
        try {
            System.out.print("Rental ID (e.g. R99): ");
            String rentalId = scanner.nextLine().trim();

            System.out.print("Vehicle ID (e.g. V01): ");
            String vehicleId = scanner.nextLine().trim();

            System.out.print("Customer ID (e.g. C01): ");
            String customerId = scanner.nextLine().trim();

            System.out.print("Start date & time (e.g. 2026-10-01T10:00:00): ");
            LocalDateTime start = LocalDateTime.parse(scanner.nextLine().trim());

            System.out.print("End date & time (e.g. 2026-10-03T18:00:00): ");
            LocalDateTime end = LocalDateTime.parse(scanner.nextLine().trim());

            Rental rental = rentalService.rentVehicle(rentalId, vehicleId, customerId, start, end);
            System.out.printf("Rental confirmed successfully! Total charged: %.2f $%n", rental.getTotalPrice());

        } catch (DateTimeParseException e) {
            System.err.println("Error: Invalid date format. Please use ISO format YYYY-MM-DDTHH:MM:SS.");
        } catch (RentalException e) {
            System.err.println("Rental failed: " + e.getMessage());
        }
    }

    private void handleReturnVehicle() {
        System.out.println("\n--- Return Vehicle ---");
        try {
            System.out.print("Rental ID: ");
            String rentalId = scanner.nextLine().trim();

            System.out.print("Final odometer mileage: ");
            double endMileage = Double.parseDouble(scanner.nextLine().trim());

            rentalService.returnVehicle(rentalId, endMileage);
            System.out.println("Vehicle returned and set back to AVAILABLE successfully!");

        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid mileage format.");
        } catch (RentalException e) {
            System.err.println("Return failed: " + e.getMessage());
        }
    }
}