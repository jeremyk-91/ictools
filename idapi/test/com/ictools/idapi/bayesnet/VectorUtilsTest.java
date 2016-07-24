package com.ictools.idapi.bayesnet;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;
import java.util.Vector;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

public class VectorUtilsTest {

    private final double EPSILON = 1e-7;

    @Test
    public void normalizesOneElementVectorCorrectly() {
        List<Double> vector = Lists.newArrayList(6.4);
        assertThat(Iterables.getOnlyElement(VectorUtils.normalize(vector)), is(1.0));
    }

    @Test
    public void normalizesNegativeOneElementVectorCorrectly() {
        List<Double> vector = Lists.newArrayList(-6.4);
        assertThat(Iterables.getOnlyElement(VectorUtils.normalize(vector)), is(-1.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsToNormalizeSingletonZeroVector() {
        VectorUtils.normalize(Lists.newArrayList(0.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsToNormalizeMultiElementZeroVector() {
        VectorUtils.normalize(Lists.newArrayList(0.0, 0.0, 0.0, 0.0));
    }

    @Test
    public void normalizesTwoElementVectorCorrectly_BothPositive() {
        checkNormalization(Lists.newArrayList(3.0, 9.0), Lists.newArrayList(0.25, 0.75));
    }

    @Test
    public void normalizesTwoElementVectorCorrectly_MixedSign() {
        checkNormalization(Lists.newArrayList(3.0, -9.0), Lists.newArrayList(0.25, -0.75));
    }

    @Test
    public void normalizesLargerVectorCorrectly_1() {
        checkNormalization(Lists.newArrayList(1.0, 0.0, 1.0, 0.0, -1.0, 0.0, 1.0), Lists.newArrayList(0.25, 0.0, 0.25, 0.0, -0.25, 0.0, 0.25));
    }

    @Test
    public void normalizesLargerVectorCorrectly_2() {
        checkNormalization(Lists.newArrayList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0), Lists.newArrayList(1./28, 2./28, 3./28, 4./28, 5./28, 6./28, 7./28));
    }

    private void checkNormalization(List<Double> vector, List<Double> expected) {
        List<Double> actual = VectorUtils.normalize(vector);
        TestUtils.checkVectorEquality(actual, expected);
    }
}
