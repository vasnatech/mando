package com.vasnatech.mando.expression.function;

import com.vasnatech.commons.random.Randoms;

public interface RandomFunctions {

    static String randomId() {
        return Randoms.hex();
    }

    static String randomIdOfSize(Number number) {
        return Randoms.hex(number.intValue());
    }
}
