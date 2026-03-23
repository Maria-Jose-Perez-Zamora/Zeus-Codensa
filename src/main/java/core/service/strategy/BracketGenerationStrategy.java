package core.service.strategy;

import core.model.KnockoutBracket;
import java.util.List;

public interface BracketGenerationStrategy {
    List<KnockoutBracket> generarLlaves(List<String> teams, String phase);
}
