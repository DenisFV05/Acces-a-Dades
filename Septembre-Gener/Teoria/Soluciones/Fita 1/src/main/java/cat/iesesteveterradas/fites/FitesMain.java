package cat.iesesteveterradas.fites;

import java.util.Scanner;

public class FitesMain {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            System.out.println("Escull una opció:");
            System.out.println("0) Exercici0");
            System.out.println("1) Exercici1");
            System.out.println("2) Exercici2");
            System.out.println("3) Exercici3");
            System.out.println("100) Sortir");
            System.out.print("Opció: ");

            int opcio = in.nextInt();
            in.nextLine(); // Consume newline left-over

            switch (opcio) {
                case 0:
                    Exercici0.main(args);
                    break;
                case 1:
                    Exercici1.main(args);
                    break;
                case 2:
                    Exercici2.main(args);
                    break;
                case 3:
                    Exercici3.main(args);
                    break;
                case 100:
                    running = false;
                    System.out.println("Sortint...");
                    break;
                default:
                    System.out.println("Opció no vàlida.");
                    break;
            }
        }
        in.close();
    }
}
