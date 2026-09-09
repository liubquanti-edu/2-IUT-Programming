public class ElectricScooter extends Vehicle {

    int batteryLevel;

    public ElectricScooter(int id, String brand, String model, double distanceTraveled, boolean available, int batteryLevel) {
        super(id, brand, model, distanceTraveled, available);
        this.batteryLevel = batteryLevel;
    }

}
