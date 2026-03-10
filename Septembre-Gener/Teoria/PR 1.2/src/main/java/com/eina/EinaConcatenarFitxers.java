package com.eina;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EinaConcatenarFitxers extends JFrame {

    private JTextArea textArea;

    public EinaConcatenarFitxers() {
        setTitle("Eina per Concatenar Fitxers");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicialitzarComponents();
        setLocationRelativeTo(null); // Centrar la finestra
    }

    private void inicialitzarComponents() {
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setText("Arrossega un directori aquí...");

        JScrollPane scrollPane = new JScrollPane(textArea);
        getContentPane().add(scrollPane);

        // Configurar el suport de Drag and Drop
        new DropTarget(textArea, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                // No cal implementar
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {
                // No cal implementar
            }

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {
                // No cal implementar
            }

            @Override
            public void dragExit(DropTargetEvent dte) {
                // No cal implementar
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    // Acceptar el drop i obtenir les dades
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Object transferData = dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                    if (transferData instanceof List<?>) {
                        List<?> droppedObjects = (List<?>) transferData;
                        if (!droppedObjects.isEmpty() && droppedObjects.get(0) instanceof File) {
                            File directory = (File) droppedObjects.get(0);
                            if (directory.isDirectory()) {
                                processarDirectori(directory);
                            } else {
                                JOptionPane.showMessageDialog(EinaConcatenarFitxers.this,
                                        "Si us plau, arrossega un directori.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(EinaConcatenarFitxers.this,
                                    "Si us plau, arrossega un directori.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(EinaConcatenarFitxers.this,
                                "Error en obtenir les dades arrossegades.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void processarDirectori(File directory) {
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            private StringBuilder contingutTotal = new StringBuilder();

            @Override
            protected Void doInBackground() {
                try (Stream<Path> paths = Files.walk(directory.toPath())) {
                    List<Path> fitxers = paths.filter(Files::isRegularFile)
                            .filter(path -> {
                                String nomFitxer = path.getFileName().toString().toLowerCase();
                                return nomFitxer.endsWith(".java") || nomFitxer.endsWith(".js") || nomFitxer.endsWith(".py");
                            })
                            .collect(Collectors.toList());

                    for (Path fitxer : fitxers) {
                        String nomFitxer = fitxer.toString();
                        publish("Processant: " + nomFitxer);
                        String contingutFitxer = Files.readString(fitxer);

                        contingutTotal.append("/******* ").append(nomFitxer).append(" *******/\n");
                        contingutTotal.append(contingutFitxer).append("\n\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String missatge : chunks) {
                    textArea.append("\n" + missatge);
                }
            }

            @Override
            protected void done() {
                guardarFitxer(contingutTotal.toString());
            }
        };
        worker.execute();
    }

    private void guardarFitxer(String contingut) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar fitxer");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fitxerAGuardar = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fitxerAGuardar))) {
                writer.write(contingut);
                JOptionPane.showMessageDialog(this, "Fitxer guardat correctament.", "Èxit", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el fitxer.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Operació cancel·lada.", "Cancel·lat", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EinaConcatenarFitxers eina = new EinaConcatenarFitxers();
            eina.setVisible(true);
        });
    }
}
