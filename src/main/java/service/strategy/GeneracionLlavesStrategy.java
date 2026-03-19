package service.strategy;

import model.LlaveEliminatoria;
import java.util.List;

public interface GeneracionLlavesStrategy {
    List<LlaveEliminatoria> generarLlaves(List<String> equipos, String fase);
}
