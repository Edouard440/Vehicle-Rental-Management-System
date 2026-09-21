package vrms.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import vrms.exception.RentalException;
import vrms.model.Rental;
import vrms.model.Vehicle;
import vrms.service.RentalService;

public class ConsoleMenu {

    private final RentalService rentalService;
    private final Scanner scanner;

    public ConsoleMenu(RentalService rentalService) {
        this.rentalService = rentalService;
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
        System.out.println("0. Exit");
        System.out.println("=======================================");
    }

    private void handleListVehicles() {
        System.out.println("\n--- Vehicle Fleet ---");
        for (Vehicle v : rentalService.getVehicleRepository().findAll()) {
            System.out.printf("[%s] %s | Mileage: %.1f km | Base Rate: %.2f $/day | Status: %s%n",
                    v.getId(), v.getModel(), v.getMileage(), v.getBaseRate(), v.getStatus());
        }
    }

    private void handleListAvailableVehicles() {
        System.out.println("\n--- Available Vehicles ---");
        boolean found = false;
        for (Vehicle v : rentalService.getVehicleRepository().findAll()) {
            if (v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.printf("[%s] %s | Mileage: %.1f km | Base Rate: %.2f $/day%n",
                        v.getId(), v.getModel(), v.getMileage(), v.getBaseRate());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No vehicles currently available.");
        }
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
            System.err.println("Error: Invalid date format. Please use the ISO format YYYY-MM-DDTHH:MM:SS.");
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