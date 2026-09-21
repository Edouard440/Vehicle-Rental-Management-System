package vrms.service;

import java.time.LocalDateTime;

import vrms.exception.RentalException;
import vrms.model.MaintenanceRecord;
import vrms.repository.Repository;
import vrms.model.Vehicle;

public class MaintenanceService {
    private final Repository<Vehicle> vehicles;
    private final Repository<MaintenanceRecord> maintenanceRecords;

    public MaintenanceService(Repository<Vehicle> vehicles, Repository<MaintenanceRecord> maintenanceRecords){
        
        if(vehicles == null || maintenanceRecords == null){
            throw new IllegalArgumentException("vehicles et maintenanceRecords repositories are required");
        }
        this.vehicles = vehicles;
        this.maintenanceRecords  = maintenanceRecords;
    }

    public MaintenanceRecord schedulMaintenanceRecord( String maintenanceId, String vehicleId, LocalDateTime serviceDate, double mileage, String issue){
        Vehicle vehicle = vehicles.findById(vehicleId);
        if(vehicle == null){
            throw new IllegalArgumentException("Vehicle not found ");
        }

        if(maintenanceRecords.findById(maintenanceId) != null){
            throw new IllegalArgumentException("maintenance id already exists");
        }

        MaintenanceRecord record = new MaintenanceRecord(maintenanceId, vehicleId, serviceDate, mileage, issue, MaintenanceRecord.MaintenanceStatus.SCHEDULED);
        maintenanceRecords.save(record);
        return record;
    }


    public void startMaintenance(String maintenanceId) throws RentalException{
        MaintenanceRecord record =maintenanceRecords.findById(maintenanceId);

        if (record == null) {
            throw new IllegalArgumentException("Maintenance record not found");
        }

        Vehicle vehicle = vehicles.findById(record.getVehicleId());

        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle not found");
        }

        if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
            throw new IllegalStateException("A rented vehicle cannot enter maintenance");
        }

        record.start();
        vehicle.setStatus(Vehicle.VehicleStatus.MAINTENANCE);
    }

    public void completeMaintenance(String maintenanceId) throws RentalException {

        MaintenanceRecord record = maintenanceRecords.findById(maintenanceId);

        if (record == null) {
            throw new IllegalArgumentException("Maintenance record not found");
        }

        Vehicle vehicle = vehicles.findById(record.getVehicleId());

        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle not found");
        }

        record.complete();
        vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);
    }




    
}
