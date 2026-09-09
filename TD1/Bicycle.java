public class Bicycle extends Vehicle {

    int numberOfGears;

    public Bicycle(int id, String brand, String model, double distanceTraveled, boolean available, int numberOfGears) {
        super(id, brand, model, distanceTraveled, available);
        this.numberOfGears = numberOfGears;
    }

}
