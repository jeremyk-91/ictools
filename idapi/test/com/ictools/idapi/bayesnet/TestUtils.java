package com.ictools.idapi.bayesnet;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.core.Is.is;

public class TestUtils {
    public static final double EPSILON = 1e-7;

    public static void checkVectorEquality(List<Double> v1, List<Double> v2) {
        assertThat(v1.size(), is(v2.size()));
        for (int i = 0; i < v1.size(); i++) {
            assertThat(v1.get(i), is(closeTo(v2.get(i), EPSILON)));
        }
    }

    public static void checkVectorRatios(List<Double> v1, List<Double> v2) {
        checkVectorEquality(VectorUtils.normalize(v1), VectorUtils.normalize(v2));
    }
}
