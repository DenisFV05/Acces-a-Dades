package com.project;

public class MainAuto {

    public static void main(String[] args) {
        
        int successes = 0;
        int failures = 0;

        for (int opcio = 0; opcio <= 12; opcio++) {
            try {
                System.out.println("Executant opció " + opcio + "...");
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
                    default: break;
                }
                successes++;
                // Imprimeix OK en verd
                System.out.println("\u001B[32m" + "Opció " + opcio + " OK" + "\u001B[0m");
            } catch (Exception e) {
                failures++;
                // Imprimeix Error en vermell
                System.out.println("\u001B[31m" + "Opció " + opcio + " Error: " + e.getMessage() + "\u001B[0m");
            }
        }

        System.out.println("\nExecució completada.");
        // Imprimeix total d'OKs i Errors
        System.out.println("Total OKs: \u001B[32m" + successes + "\u001B[0m");
        System.out.println("Total Errors: \u001B[31m" + failures + "\u001B[0m");
    }
}
