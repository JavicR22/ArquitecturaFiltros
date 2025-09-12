package org.example.domain.usecases;

import org.example.IFiltro;
import org.example.application.service.FileConversionService;
import org.example.domain.entities.BatchConversionResult;
import org.example.infrastructure.config.DependencyInjection;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BinarioABase64Process implements IFiltro {
    @Override
    public void process(String ruta) throws IOException{
        DependencyInjection di = null;
        di = DependencyInjection.getInstance();
        FileConversionService service = di.getFileConversionService();
        if (ruta.startsWith("\"") && ruta.endsWith("\"")) {
            ruta = ruta.substring(1, ruta.length() - 1);
        }


        if (Files.isDirectory(Paths.get(ruta))) {
            System.out.println("🚀 Iniciando conversión por lotes para el directorio: " + ruta);
            BatchConversionResult batchResult = service.convertDirectoryToBase64(ruta);
            service.printBatchConversionResult(batchResult);
            System.exit(batchResult.hasFailures() ? 1 : 0);
        }
    }
}
