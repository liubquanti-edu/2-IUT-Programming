public class Main {

    public static void main(String[] args) {
        Vehicle v1 = new Car(1, "Renault", "Clio", 15000, true, 5);
        Vehicle v2 = new Bicycle(2, "Decathlon", "Elops", 320, true, 21);
        Vehicle v3 = new ElectricScooter(3, "Xiaomi", "Pro 2", 540, false, 78);

        v1.displayInfo();
        System.out.println();
        v2.displayInfo();
        System.out.println();
        v3.displayInfo();
    }

}
