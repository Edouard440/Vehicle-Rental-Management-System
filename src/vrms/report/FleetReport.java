package vrms.report;
import vrms.model.Vehicle;
import java.util.List;

public class FleetReport{
    public void printFleetReport(List<Vehicle> vehicles){
        System.out.println("Fleet report : ");
        if(vehicles.isEmpty()){
            System.out.println("No vehicles in the fleet.");
            return;
        }

        vehicles.forEach(System.out::println);

    }

    public void printAvailableVehicles(List<Vehicle> vehicles){
        System.out.println("Availables vehicles : ");
        List<Vehicle> availablesVehicles = vehicles.stream().filter(vehicle -> vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE).toList();

        if (availablesVehicles.isEmpty()){
            System.out.println("No available vehicles");
            return; //we must return void
        }
        availablesVehicles.forEach(System.out::println);
    }


    public void printRentedVehicles(List<Vehicle> vehicles){
        System.out.println("Rented Vehicles : ");
        List<Vehicle> rentedVehicles = vehicles.stream().filter(vehicle -> vehicle.getStatus() == Vehicle.VehicleStatus.RENTED).toList();
        if (rentedVehicles.isEmpty()){
            System.out.println("No rented vehicles.");
            return;
        }

        rentedVehicles.forEach(System.out::println);

    }

    public void printMaintenanceVehicles(List<Vehicle> vehicles){
        System.out.println("Maintenance Vehicles : ");
        List<Vehicle> maintenanceVehicles = vehicles.stream().filter(vehicle -> vehicle.getStatus() == Vehicle.VehicleStatus.MAINTENANCE).toList();
        if (maintenanceVehicles.isEmpty()){
            System.out.println("No vehicles in maintenance.");
            return;
        }

        maintenanceVehicles.forEach(System.out::println);

    }

    public double calculateAverageMileage(List<Vehicle> vehicles){
        if (vehicles.isEmpty()){
            return 0.0;
        }

        return vehicles.stream().mapToDouble(Vehicle::getMileage).average().orElse(0);
    }

    public double calculateAverageBaseRate(List<Vehicle> vehicles){
        if (vehicles.isEmpty()){
            return 0.0;
        }

        return vehicles.stream().mapToDouble(Vehicle::getBaseRate).average().orElse(0);
    }

    public void printFleetSummary(List<Vehicle> vehicles){
        System.out.println("Fleet summary : ");
        long availableCount = vehicles.stream().filter(vehicle -> vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE).count();
        long rentedCount = vehicles.stream().filter(vehicle -> vehicle.getStatus() == Vehicle.VehicleStatus.RENTED).count();
        long maintenanceCount = vehicles.stream().filter(vehicle -> vehicle.getStatus() == Vehicle.VehicleStatus.MAINTENANCE).count();

        System.out.println("Total vehicles : " + vehicles.size());
        System.out.println("Available : " + availableCount);
        System.out.println("Rented : " + rentedCount);
        System.out.println("Maintenance : " + maintenanceCount);

        System.out.printf(java.util.Locale.US, "Average mileage : %.2f%n", calculateAverageMileage(vehicles));
        System.out.printf(java.util.Locale.US, "Average base rate : %.2f%n", calculateAverageBaseRate(vehicles));
    }

}