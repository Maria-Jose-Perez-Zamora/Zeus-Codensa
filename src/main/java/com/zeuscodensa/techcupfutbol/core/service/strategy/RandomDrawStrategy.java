package com.zeuscodensa.techcupfutbol.core.service.strategy;

import com.zeuscodensa.techcupfutbol.core.model.KnockoutBracket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomDrawStrategy implements BracketGenerationStrategy {
    @Override
    public List<KnockoutBracket> generarLlaves(List<String> teams, String phase) {
        List<String> mezclados = new ArrayList<>(teams);
        Collections.shuffle(mezclados);

        List<KnockoutBracket> llaves = new ArrayList<>();
        for (int i = 0; i < mezclados.size() - 1; i += 2) {
            llaves.add(new KnockoutBracket(phase, mezclados.get(i), mezclados.get(i + 1)));
        }
        return llaves;
    }
}
