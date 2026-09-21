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
            System.out.print("Choisissez une option : ");
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
                    System.out.println("Fermeture du système. Au revoir !");
                    running = false;
                    break;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.\n");
                    break;
            }
        }
    }

    private void printMenu() {
        System.out.println("\n========== VRMS - MENU PRINCIPAL ==========");
        System.out.println("1. Afficher tous les véhicules");
        System.out.println("2. Afficher les véhicules disponibles");
        System.out.println("3. Louer un véhicule");
        System.out.println("4. Retourner un véhicule");
        System.out.println("0. Quitter");
        System.out.println("===========================================");
    }

    private void handleListVehicles() {
        System.out.println("\n--- Flotte de véhicules ---");
        for (Vehicle v : rentalService.getVehicleRepository().findAll()) {
            System.out.printf("[%s] %s | Kilométrage: %.1f km | Tarif: %.2f $/j | Statut: %s%n",
                    v.getId(), v.getModel(), v.getMileage(), v.getBaseRate(), v.getStatus());
        }
    }

    private void handleListAvailableVehicles() {
        System.out.println("\n--- Véhicules disponibles ---");
        boolean found = false;
        for (Vehicle v : rentalService.getVehicleRepository().findAll()) {
            if (v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                System.out.printf("[%s] %s | Kilométrage: %.1f km | Tarif: %.2f $/j%n",
                        v.getId(), v.getModel(), v.getMileage(), v.getBaseRate());
                found = true;
            }
        }
        if (!found) {
            System.out.println("Aucun véhicule disponible actuellement.");
        }
    }

    private void handleRentVehicle() {
        System.out.println("\n--- Nouvelle location ---");
        try {
            System.out.print("Identifiant de la location (ex: R99) : ");
            String rentalId = scanner.nextLine().trim();

            System.out.print("Identifiant du véhicule (ex: V01) : ");
            String vehicleId = scanner.nextLine().trim();

            System.out.print("Identifiant du client (ex: C01) : ");
            String customerId = scanner.nextLine().trim();

            System.out.print("Date/heure de début (ex: 2026-10-01T10:00:00) : ");
            LocalDateTime start = LocalDateTime.parse(scanner.nextLine().trim());

            System.out.print("Date/heure de fin (ex: 2026-10-03T18:00:00) : ");
            LocalDateTime end = LocalDateTime.parse(scanner.nextLine().trim());

            Rental rental = rentalService.rentVehicle(rentalId, vehicleId, customerId, start, end);
            System.out.printf("Location validée avec succès ! Total facturé : %.2f $%n", rental.getTotalPrice());

        } catch (DateTimeParseException e) {
            System.err.println("Erreur : format de date invalide. Utilisez le format ISO YYYY-MM-DDTHH:MM:SS.");
        } catch (RentalException e) {
            System.err.println("Échec de la location : " + e.getMessage());
        }
    }

    private void handleReturnVehicle() {
        System.out.println("\n--- Clôture de location ---");
        try {
            System.out.print("Identifiant de la location : ");
            String rentalId = scanner.nextLine().trim();

            System.out.print("Kilométrage final du véhicule : ");
            double endMileage = Double.parseDouble(scanner.nextLine().trim());

            rentalService.returnVehicle(rentalId, endMileage);
            System.out.println("Véhicule retourné et remis en disponibilité avec succès !");

        } catch (NumberFormatException e) {
            System.err.println("Erreur : kilométrage invalide.");
        } catch (RentalException e) {
            System.err.println("Échec du retour : " + e.getMessage());
        }
    }
}