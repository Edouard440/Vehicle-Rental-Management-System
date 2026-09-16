package vrms.contract;

import vrms.exception.RentalException;

public interface Rentable{

    boolean isAvaible();
    void rent() throws RentalException;
    void returnVehicle() throws RentalException;
    void returnVehicle(double endMileage) throws RentalException;

}