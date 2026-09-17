package vrms.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import vrms.exception.RentalException;
import vrms.model.*;
import vrms.model.Vehicle.VehicleStatus;

public class FileStorage{

    public List<Vehicle> loadVehicles(String filePath){

        File file = new File(filePath);
        List<Vehicle> vehicules = new ArrayList<>();

        try (Scanner fileScanner = new Scanner(file);){

            while (fileScanner.hasNextLine()){

                String line = fileScanner.nextLine();
            
                if (line.isEmpty()){
                    continue;
                }

                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");

                Vehicle object = null;

                String type = lineScanner.next();
                String id = lineScanner.next();
                String model = lineScanner.next();
                double mileage = lineScanner.nextDouble();
                double baseRate = lineScanner.nextDouble();
                VehicleStatus status = VehicleStatus.valueOf(lineScanner.next().trim());

                if (type.equals("CAR")){
                    int numberOfSeats = lineScanner.nextInt();
                    object = new Car(id, model, mileage, baseRate, status, numberOfSeats);

                }else if (type.equals("TRUCK")) {
                    double loadCapacity = lineScanner.nextDouble();
                    object = new Truck(id, model, mileage, baseRate, status, loadCapacity);

                }else if(type.equals("BIKE")) {
                    int engineCapacity = lineScanner.nextInt();
                    object = new Bike(id, model, mileage, baseRate, status, engineCapacity);
                }

                if (object != null) vehicules.add(object);
                lineScanner.close();
            }
            
        }
        catch(FileNotFoundException err){
            System.out.println("Error file not found");
        }

        return vehicules;
    
    }

    public List<Customer> loadCustomers(String filePath){
            
        List<Customer> customers = new ArrayList<>();
        File file = new File(filePath);

        try (Scanner fileScanner = new Scanner(file);)
        {
            while (fileScanner.hasNextLine()){

                String line = fileScanner.nextLine();
            
                if (line.isEmpty()){
                    continue;
                }

                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");

                String id = lineScanner.next().trim();
                String name = lineScanner.next().trim();
                String email = lineScanner.next().trim();
                
                Customer object = new Customer(id, name, email);

                if (object != null) customers.add(object);
                lineScanner.close();
            }
        }
        catch(FileNotFoundException err){
            System.out.println("Error file not found");
        }

        return customers;
    
    }
    public List<Rental> loadRentals(String filePath){
        List<Rental> rentals = new ArrayList<>();
        File file = new File(filePath);

        try (Scanner fileScanner = new Scanner(file);){
            while (fileScanner.hasNextLine()){

                String line = fileScanner.nextLine();
            
                if (line.isEmpty()){
                    continue;
                }

                Scanner lineScanner = new Scanner(line);
                lineScanner.useDelimiter(",");

                String rentalId = lineScanner.next().trim();
                String vehicleId = lineScanner.next().trim();
                String customerId = lineScanner.next().trim();
                LocalDateTime start = LocalDateTime.parse(lineScanner.next().trim());
                LocalDateTime end = LocalDateTime.parse(lineScanner.next().trim());
                lineScanner.useLocale(java.util.Locale.US);
                double totalPrice = lineScanner.nextDouble();
                boolean active = Boolean.parseBoolean(lineScanner.next().trim());
                
                try {
                    Rental object = new Rental(rentalId, vehicleId, customerId, start, end, totalPrice, active);
                    if (object != null) rentals.add(object);
                } catch (RentalException e) {
                    System.err.println("Ligne de location invalide ignorée : " + e.getMessage());
                }

                lineScanner.close();
            }
        }
        catch(FileNotFoundException err){
            System.out.println("Error file not found");
        }

        return rentals;
    }
        
}