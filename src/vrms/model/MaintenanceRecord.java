package vrms.model;
import java.time.LocalDateTime;
import vrms.contract.Identifiable;



public class MaintenanceRecord implements Identifiable {


    public enum MaintenanceStatus {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED
    }
    private final String maintenanceId;
    private final String vehicleId;
    private final LocalDateTime serviceDate;
    private final double mileage;
    private final String issue;
    private MaintenanceStatus status;

    public MaintenanceRecord(String maintenanceId, String vehicleId, LocalDateTime serviceDate, double mileage, String issue, MaintenanceStatus status){
        if (maintenanceId == null || maintenanceId.isBlank()) {
            throw new IllegalArgumentException("Maintenance ID is required");
        }
        this.maintenanceId = maintenanceId;

        if (vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Vehicle ID is required");
        }
        this.vehicleId = vehicleId;
        if (serviceDate == null) {
            throw new IllegalArgumentException("Service date is required");
        }
        this.serviceDate = serviceDate;
        if (!Double.isFinite(mileage) || mileage < 0.0) {
            throw new IllegalArgumentException("Mileage must be finite and non-negative");
        }
        this.mileage = mileage;

        if (issue == null || issue.isBlank()) {
            throw new IllegalArgumentException("Issue is required");
        }
        this.issue = issue;
        if (status == null) {
            throw new IllegalArgumentException("Maintenance status is required");
        }
        this.status = status;
    }

    @Override 
    public String getId(){
        return maintenanceId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public LocalDateTime getServiceDate() {
        return serviceDate;
    }

    public double getMileage() {
        return mileage;
    }

    public String getIssue() {
        return issue;
    }
    public MaintenanceStatus getStatus() {
        return status;
    
    }


    public void start() {
    if (status != MaintenanceStatus.SCHEDULED) {
        throw new IllegalStateException("Only scheduled maintenance can be started");
    }

    status = MaintenanceStatus.IN_PROGRESS;
    }

    public void complete() {
        if (status != MaintenanceStatus.IN_PROGRESS) {
            throw new IllegalStateException("Only maintenance in progress can be completed");
        }
        status = MaintenanceStatus.COMPLETED;
    }   

    @Override 
    public String toString(){
        return  "maintenance:" + maintenanceId + "vehicle :" + vehicleId + "date of the service :" + serviceDate + "mileage :" + mileage + "issue :" + issue;
    }
}
