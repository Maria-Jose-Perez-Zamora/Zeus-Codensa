package core.service.strategy;

import core.model.LlaveEliminatoria;
import java.util.List;

public interface GeneracionLlavesStrategy {
    List<LlaveEliminatoria> generarLlaves(List<String> equipos, String fase);
}
