package com.project;

import java.io.IOException;
import java.util.*;

public class Main {

    static Scanner in = new Scanner(System.in); // System.in és global, Scanner també ho a de ser

    // Main
    public static void main(String[] args) throws InterruptedException, IOException {
        
        boolean running = true;

        while (running) {

            String menu = "Escull una opció:";
            menu = menu + "\n 0) Gestió d'arxius";
            menu = menu + "\n 1) Escriptura d'arxiu 'Writer'";
            menu = menu + "\n 2) Lectura d'arxiu 'Scanner'";
            menu = menu + "\n 3) Escriptura d'arxiu 'List'";
            menu = menu + "\n 4) Lectura d'arxiu 'List'";
            menu = menu + "\n 5) Escriptura de dades primitives";
            menu = menu + "\n 6) Lectura de dades primitives";
            menu = menu + "\n 7) Escriptura d'objectes";
            menu = menu + "\n 8) Lectura d'objectes";
            menu = menu + "\n 9) Escriptura de llistes";
            menu = menu + "\n 10) Lectura de llistes";
            menu = menu + "\n 11) Gestió CSV";
            menu = menu + "\n 12) Gestió XML";
            menu = menu + "\n 13) Sortir";
            System.out.println(menu);

            int opcio = Integer.valueOf(llegirLinia("Opció:"));
            
            try {
                switch (opcio) {
                    case 0: GestioArxius.main(args);                break;
                    case 1: EscripturaArxiuWriter.main(args);       break;
                    case 2: LecturaArxiuScanner.main(args);         break;
                    case 3: EscripturaArxiuList.main(args);         break;
                    case 4: LecturaArxiuList.main(args);            break;
                    case 5: EscripturaDadesPrimitives.main(args);   break;
                    case 6: LecturaDadesPrimitives.main(args);      break;
                    case 7: EscripturaObjectes.main(args);          break;
                    case 8: LecturaObjectes.main(args);             break;
                    case 9: EscripturaLlistes.main(args);           break;
                    case 10: LecturaLlistes.main(args);             break;
                    case 11: GestioCSV.main(args);                  break;
                    case 12: GestioXML.main(args);                  break;
                    case 13: running = false;                       break;
                    default: break;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

		in.close();
    }

    static public String llegirLinia (String text) {
        System.out.print(text);
        return in.nextLine();
    }
}