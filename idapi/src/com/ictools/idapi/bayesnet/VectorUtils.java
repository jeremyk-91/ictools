package com.ictools.idapi.bayesnet;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class VectorUtils {

    private VectorUtils() {
        // Don't think of instantiating me. At all.
    }

    public static List<Double> normalize(List<Double> vector) {
        double sum = vector.stream().mapToDouble(Math::abs).sum();
        if (sum == 0.0) {
            throw new IllegalArgumentException("Can't normalize a vector which sums to 0");
        }
        return vector.stream().map(x -> x / sum).collect(Collectors.toCollection(ArrayList::new));
    }
}
