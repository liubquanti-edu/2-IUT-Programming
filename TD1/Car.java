public class Car extends Vehicle {

    int numberOfSeats;

    public Car(int id, String brand, String model, double distanceTraveled, boolean available, int numberOfSeats) {
        super(id, brand, model, distanceTraveled, available);
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Number of seats : " + numberOfSeats);
    }

}
