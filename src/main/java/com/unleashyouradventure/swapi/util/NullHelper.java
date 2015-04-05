package com.unleashyouradventure.swapi.util;


public final class NullHelper {

    private NullHelper() {
        // Util class with static methods
    }

    public static <R> R get(R canBeNull, R defaultValue) {
        return canBeNull == null ? defaultValue : canBeNull;
    }

    @SafeVarargs
    public static <R> R getFirstNoneNull(R... rs) {
        for (R r : rs) {
            if (r != null) {
                return r;
            }
        }
        return null;
    }
}
