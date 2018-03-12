package raven;

import java.util.Random;

private var rand = Random();

fun seed(seed) {
    rand.setSeed(seed);
}

fun nextInt(bound) {
    return rand.nextInt(bound);
}

fun nextInt(min, max) {
    return min + rand.nextInt(max - min);
}

fun nextReal() {
    return rand.nextDouble();
}

fun nextReal(min, max) {
    return min + (max - min) * nextReal();
}

fun nextBoolean() {
    return rand.nextBoolean();
}

fun nextGaussian() {
    return rand.nextGaussian();
}
