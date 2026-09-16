package vrms.repository;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import vrms.model.Customer;
import vrms.model.Vehicle;
import vrms.model.Rentals;

public class FileStorage{

    public List<Vehicle> loadVehicles(String filePath){
        
        List<Vehicle> vehicules = new ArrayList<>();

        try {
            File file = new File(filePath);
            Scanner scan = new Scanner(file);
        }
        catch(FileNotFoundException err){
            System.out.println("Error file not found");
        }

        return vehicules;
    
    }
    public List<Customer> loadCustomers(String filePath){
            
        List<Customer> customers = new ArrayList<>();

        try {
            File file = new File(filePath);
            Scanner scan = new Scanner(file);
        }
        catch(FileNotFoundException err){
            System.out.println("Error file not found");
        }

        return customers;
    
    }
    public List<Rentals> loadRentals(String filePath){
        List<Rentals> rentals = new ArrayList<>();

        try (Scanner scan = new Scanner(file);){
            
            
        }
        catch(FileNotFoundException err){
            System.out.println("Error file not found");
        }

        return rentals;
    }
        
}