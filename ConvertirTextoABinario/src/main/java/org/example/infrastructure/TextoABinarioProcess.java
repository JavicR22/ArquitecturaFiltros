package org.example.infrastructure;

import org.example.IFiltro;
import org.example.application.usecase.ConvertTextFileUseCase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextoABinarioProcess implements IFiltro {

    @Override
    public void process(String ruta) throws IOException{
        ConvertTextFileUseCase useCase = new ConvertTextFileUseCase(
                new TextToBinaryFileConverter()
        );

        if (ruta.startsWith("\"") && ruta.endsWith("\"")) {
            ruta = ruta.substring(1, ruta.length() - 1);
        }
        System.err.println("Son: "+ruta);
        Path inputFile = Paths.get(ruta);

        if (!Files.exists(inputFile)) {
            System.err.println("❌ El archivo no existe: " + ruta);
            System.exit(1);
        }


        useCase.execute(ruta);
        System.err.println(ruta);
        String envioRuta = buildBinPath(ruta);
        System.out.println(envioRuta);
        System.out.flush();
    }
    private static String buildBinPath(String originalPath) {
        Path inputFile = Paths.get(originalPath);
        String name = inputFile.getFileName().toString();

        int dotIndex = name.lastIndexOf(".");
        String baseName = (dotIndex == -1) ? name : name.substring(0, dotIndex);

        Path parent = inputFile.getParent();
        if (parent == null) {
            parent = Paths.get("."); // directorio actual
        }

        // Siempre termina en .bin
        return parent.resolve(baseName + "Binario.bin").toString();
    }

}
