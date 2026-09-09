public abstract class Vehicle {

    int id;
    String brand;
    String model;
    double distanceTraveled;
    boolean available;

    public Vehicle(int id, String brand, String model, double distanceTraveled, boolean available) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.distanceTraveled = distanceTraveled;
        this.available = available;
    }

    public void displayInfo() {
        System.out.println("ID : " + id);
        System.out.println("Brand : " + brand);
        System.out.println("Model : " + model);
        System.out.println("Distance : " + distanceTraveled);
        System.out.println("Available : " + available);
    }

}