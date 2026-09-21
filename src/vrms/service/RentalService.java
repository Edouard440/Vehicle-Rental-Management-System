package vrms.service;

import vrms.model.Rental;
import vrms.model.Vehicle;
import vrms.model.Customer;
import vrms.contract.PricingPolicy;
import vrms.exception.RentalException;
import vrms.repository.Repository;
import vrms.service.pricing.DailyPricingPolicy;
import vrms.service.pricing.HourlyPricingPolicy;
import vrms.service.pricing.WeeklyPricingPolicy;

import java.time.LocalDateTime;
import java.time.Duration;

public class RentalService {
    private final Repository<Vehicle> vehicles;
    private final Repository<Customer> customers;
    private final Repository<Rental> rentals;

    public RentalService (Repository<Vehicle> vehicles, Repository<Customer> customers, Repository<Rental> rentals){
        if(vehicles == null || customers == null || rentals == null){
            throw new IllegalArgumentException("please fill repositories");
        }

        this.vehicles = vehicles;
        this.customers = customers;
        this.rentals = rentals;
    }

    public double calculateQuote(int rentalDurationInHours, Vehicle vehicle) throws RentalException{
        if(rentalDurationInHours <= 0 || vehicle == null) throw new RentalException("Please enter the duration and the vehicle");
        PricingPolicy policy;
        if(rentalDurationInHours < 24){
            policy = new HourlyPricingPolicy();
        }
        else if(rentalDurationInHours < 168){
            policy = new DailyPricingPolicy();
        }else{
            policy = new WeeklyPricingPolicy();
        }

        double quote = policy.calculatePrice(rentalDurationInHours, vehicle);
        if(quote <= 0.0){
            throw new RentalException("incorrect calcul of the price ");
        }
        return quote;
    }

    public Rental rentVehicle(String rentalId, String vehicleId, String customerId, LocalDateTime start, LocalDateTime end ) throws RentalException{
        Vehicle vehicle = vehicles.findById(vehicleId);
        if(vehicle == null){
            throw new RentalException("vehicle not found");
        }

        Customer customer = customers.findById(customerId);
        if(customer == null){
            throw new RentalException("customer not found");
        }

        if (rentals.findById(rentalId) != null) {
            throw new RentalException("Rental ID already exists");
        }
        for (Rental existing : rentals.findAll()){
            if(existing.getVehicleId().equals(vehicleId) && existing.isActive()){
                throw new RentalException("vehicle has already an active rental");
            }
        }

        if (start == null || end == null) {
            throw new RentalException("Start and end are required");
        }

        if (!end.isAfter(start)) {
            throw new RentalException("End must be after start");
        }
        Duration duration = Duration.between(start, end);
        long hours = duration.toHours();
        if(!duration.equals(Duration.ofHours(hours))){
            hours ++;
        }
        int rentalDurationInHours = Math.toIntExact(hours);
        double quote = calculateQuote(rentalDurationInHours, vehicle);

        Rental rental = new Rental(rentalId, vehicleId, customerId, start, end, quote, true);
        vehicle.setStatus(Vehicle.VehicleStatus.RENTED);        rentals.save(rental);
        return rental;
    }

    public void returnVehicle(String rentalId, double endMileage)throws RentalException{
        Rental rental = rentals.findById(rentalId);
        if (rental == null) {
            throw new RentalException("Rental not found");
        }

        if (!rental.isActive()) {
            throw new RentalException("Rental is already completed");
        }

        Vehicle vehicle = vehicles.findById(rental.getVehicleId());

        if (vehicle == null) {
            throw new RentalException("Vehicle not found");
        }

        if (vehicle.getStatus() != Vehicle.VehicleStatus.RENTED) {
            throw new RentalException("Vehicle is not currently rented");
        }

        if (endMileage < vehicle.getMileage()) {
            throw new RentalException("Return mileage must be finite and not lower than current mileage");
        }

        vehicle.setMileage(endMileage);
        vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
        rental.completeRent();
    }
    
        public Repository<Vehicle> getVehicleRepository() {
        return this.vehicles;
    }

    public Repository<Rental> getRentalRepository() {
        return this.rentals;
    }

}