package vrms.test;

import java.util.ArrayList;
import java.util.List;
import vrms.model.Car;
import vrms.model.Vehicle;
import vrms.ordering.VehicleRateComparator;

public class OrderingTest {

    public static void main(String[] args) {
        List<Vehicle> vehicles = new ArrayList<>();

        vehicles.add(new Car("V03", "Car C", 20000, 50, Vehicle.VehicleStatus.AVAILABLE, 5));
        vehicles.add(new Car("V01", "Car A", 10000, 40, Vehicle.VehicleStatus.AVAILABLE, 5));
        vehicles.add(new Car("V02", "Car B", 20000, 50, Vehicle.VehicleStatus.AVAILABLE, 5));

        vehicles.sort(null);
        System.out.println("Natural order: " + vehicles.get(0).getId());

        vehicles.sort(new VehicleRateComparator());
        System.out.println("Rate order: " + vehicles.get(0).getId());
        System.out.println("Tie breaker: " + vehicles.get(1).getId());
    }
}