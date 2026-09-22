package vrms.concurrent;

import java.time.LocalDateTime;
import vrms.service.RentalService;
import vrms.exception.RentalException;
import vrms.model.Rental;

public class RentalRequestTask implements Runnable{
    private final RentalService rentalService;
    private final String rentalId;
    private final String vehicleId;
    private final String customerId;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private boolean successful;
    private Rental rental;
    private String failureMessage;

    public RentalRequestTask(RentalService rentalService, String rentalId, String vehicleId, String customerId, LocalDateTime start, LocalDateTime end){
        if(rentalService == null){
            throw new IllegalArgumentException("rental service doesn't exist");
        }
        this.rentalService = rentalService;
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.start = start;
        this.end = end;
    }
    
    
    @Override 
    public void run(){
        if(Thread.currentThread().isInterrupted()){
            failureMessage = "Rental request was interrupted";
            return;
        }

        try{
            rental = rentalService.rentVehicle(rentalId, vehicleId, customerId, start, end);
            successful = true;
        }catch(RentalException e){
            successful = false;
            failureMessage = e.getMessage();
        }

    }

    public boolean isSuccessful() {
        return successful;
    }

    public Rental getRental() {
        return rental;
    }

    public String getFailureMessage() {
        return failureMessage;
    }
}