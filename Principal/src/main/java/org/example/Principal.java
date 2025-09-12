package org.example;



import org.example.domain.usecases.BinarioABase64Process;
import org.example.hashing.SHA256HashService;
import org.example.infrastructure.TextoABinarioProcess;
import org.example.processor.FiltroImagenProcess;


import javax.swing.*;
import java.io.File;
import java.io.IOException;


public class Principal {


    public static void main(String[] args) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione un archivo o directorio");

        // ✅ Permitir archivos y directorios
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        int seleccion = fileChooser.showOpenDialog(null);
        if (seleccion != JFileChooser.APPROVE_OPTION) {
            System.out.println("No se seleccionó nada.");
            return;
        }
        Tuberia<String> tuberia1 = new Tuberia<>();
        Tuberia<String> tuberia2 = new Tuberia<>();

        tuberia1.addFiltro(new TextoABinarioProcess());
        tuberia1.addFiltro(new SHA256HashService());

        tuberia2.addFiltro(new FiltroImagenProcess());
        tuberia2.addFiltro(new BinarioABase64Process());

        File seleccionado = fileChooser.getSelectedFile();

        if (seleccionado.isFile()) {
            System.out.println("📄 Archivo seleccionado: " + seleccionado.getAbsolutePath());
            tuberia1.execute(seleccionado.getAbsolutePath());
        } else if (seleccionado.isDirectory()) {
            System.out.println("📂 Directorio seleccionado: " + seleccionado.getAbsolutePath());
            tuberia2.execute(seleccionado.getAbsolutePath());
        } else {
            System.out.println("La selección no es válida.");
        }

    }
    }
