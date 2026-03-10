package com.project;

import com.project.estructuresdades.Objecte;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class LecturaDadesPrimitivesTest {

    @TempDir
    Path directoriTemporal;  // JUnit gestionarà automàticament el directori temporal

    @Test
    void testLecturaDadesPrimitives() {
        // Preparació: Definir el nom del fitxer dins del directori temporal
        String nomFitxer = "ArxiuEscriuPrimitivesTest.dat";
        Path camiFitxer = directoriTemporal.resolve(nomFitxer);

        try {
            // Escriure dades primitives i un objecte serialitzable en un fitxer binari
            try (FileOutputStream fos = new FileOutputStream(camiFitxer.toFile());
                 DataOutputStream dos = new DataOutputStream(fos)) {

                // Escriure dades primitives
                dos.writeUTF("Hola món");
                dos.writeInt(123);
                dos.writeBoolean(true);
                dos.writeChar('A');
                dos.writeDouble(3.14159);

                // Crear un objecte d'exemple i serialitzar-lo
                Objecte obj = new Objecte("Exemple", "Prova");
                byte[] serializedObject;
                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                    oos.writeObject(obj);
                    serializedObject = baos.toByteArray();
                }

                // Escriure l'objecte serialitzat al fitxer
                dos.writeInt(serializedObject.length);
                dos.write(serializedObject);
            }

            // Execució: Cridar al mètode que llegeix i mostra les dades del fitxer
            LecturaDadesPrimitives.llegirIFerMostra(camiFitxer.toString());

            // Verificació: Llegir el fitxer per assegurar que les dades s'han escrit correctament
            try (FileInputStream fis = new FileInputStream(camiFitxer.toFile());
                 DataInputStream dis = new DataInputStream(fis)) {

                String text = dis.readUTF();
                int numero = dis.readInt();
                boolean boolea = dis.readBoolean();
                char caracter = dis.readChar();
                double doble = dis.readDouble();

                int longitudObjecte = dis.readInt();
                byte[] objecteBytes = new byte[longitudObjecte];
                dis.readFully(objecteBytes);
                Objecte objLlegit;
                try (ByteArrayInputStream bais = new ByteArrayInputStream(objecteBytes);
                     ObjectInputStream ois = new ObjectInputStream(bais)) {
                    objLlegit = (Objecte) ois.readObject();
                }

                // Assercions per verificar les dades llegides
                assertEquals("Hola món", text, "El text llegit hauria de coincidir amb el escrit.");
                assertEquals(123, numero, "El número llegit hauria de coincidir amb el escrit.");
                assertTrue(boolea, "El valor booleà llegit hauria de ser true.");
                assertEquals('A', caracter, "El caràcter llegit hauria de ser 'A'.");
                assertEquals(3.14159, doble, 0.00001, "El doble llegit hauria de coincidir amb el escrit.");

                assertNotNull(objLlegit, "L'objecte llegit no hauria de ser null.");
                assertEquals("Exemple", objLlegit.getNom(), "El nom de l'objecte hauria de coincidir.");
                assertEquals("Prova", objLlegit.getUtilitat(), "La utilitat de l'objecte hauria de coincidir.");
            } catch (ClassNotFoundException e) {
                fail("No s'ha pogut deserialitzar l'objecte: " + e.getMessage());
            }

        } catch (IOException e) {
            fail("El test no hauria de fallar amb excepció: " + e.getMessage());
        }
        // No cal netejar manualment el directori temporal; JUnit ho farà automàticament
    }
}
