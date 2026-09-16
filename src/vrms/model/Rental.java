package vrms.model;
import vrms.exception.RentalException;
import java.time.LocalDateTime;
import java.time.Duration;

public class Rental {
    private final String rentalId;
    private final String vehicleId;
    private final String customerId;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final double totalPrice;
    private boolean active;


    public Rental(String rentalId, String vehicleId, String customerId, LocalDateTime start, LocalDateTime end, double totalPrice,boolean active)throws RentalException{
        
        if (vehicleId == null || vehicleId.isBlank()) {
            throw new RentalException("Vehicle ID is required");
        }
        this.rentalId = rentalId;
        if (rentalId == null || rentalId.isBlank()) {
            throw new RentalException("Rental ID is required");
        }
        this.vehicleId = vehicleId;
        if (customerId == null || customerId.isBlank()) {
            throw new RentalException("Customer ID is required");
        }
        this.customerId = customerId;
        if ( start == null || end == null){
            throw new RentalException("start and end must have values");
        }
        if(!end.isAfter(start)){
            throw new RentalException("end must be after start");
        }
        this.start = start;
        this.end = end;
        if(totalPrice < 0.0){
            throw new RentalException("total price must be greater or equal to 0");
        }
        this.totalPrice = totalPrice;
        this.active = active;
    }
    public String getRentalId() {
    return rentalId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public boolean isActive() {
        return active;
    }

    public long getDurationMinutes(){
        return Duration.between(start,end).toMinutes();
    }

    public void completeRent() throws RentalException{
        if(!active){
            throw new RentalException("rental is already completed");
        }
        active = false;
    }

    @Override 
    public String toString(){
        return "Rental :" + rentalId + ", vehicle:" + vehicleId + ", customer :" + customerId + ", start :" + start + ", end: " + end + "total price :" + totalPrice + ", active :" + active;
    }
}
