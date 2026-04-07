package com.zeuscodensa.techcupfutbol.core.service.strategy;

import com.zeuscodensa.techcupfutbol.core.model.KnockoutBracket;
import java.util.List;

public interface BracketGenerationStrategy {
    List<KnockoutBracket> generarLlaves(List<String> teams, String phase);
}
