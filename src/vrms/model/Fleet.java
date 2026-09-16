package vrms.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Fleet implements Iterable<Vehicle> {

    private List<Vehicle> vehicles;

    public Fleet() {
        this.vehicles = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
    }

    public int getVehicleCount() {
        return this.vehicles.size();
    }

    @Override
    public Iterator<Vehicle> iterator() {
        return this.vehicles.iterator();
    }
}