package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tuberia<T> {
    private List<IFiltro<T>> filters = new ArrayList<>();

    public void addFiltro(IFiltro<T> filter) {
        filters.add(filter);
    }

    public void execute(String entrada) throws IOException {
        String resultado = entrada;
        for (IFiltro<T> filter : filters) {
            filter.process(resultado);
        }
    }

}
