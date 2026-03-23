package core.service.strategy;

import core.model.LlaveEliminatoria;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SorteoAleatorioStrategy implements GeneracionLlavesStrategy {
    @Override
    public List<LlaveEliminatoria> generarLlaves(List<String> equipos, String fase) {
        List<String> mezclados = new ArrayList<>(equipos);
        Collections.shuffle(mezclados);

        List<LlaveEliminatoria> llaves = new ArrayList<>();
        for (int i = 0; i < mezclados.size() - 1; i += 2) {
            llaves.add(new LlaveEliminatoria(fase, mezclados.get(i), mezclados.get(i + 1)));
        }
        return llaves;
    }
}
