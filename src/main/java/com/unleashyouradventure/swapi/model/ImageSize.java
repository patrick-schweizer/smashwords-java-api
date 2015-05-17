package com.unleashyouradventure.swapi.model;

public enum ImageSize {
    tiny, thumb, full("");
    private final String ending;

    ImageSize() {
        this.ending = "-" + name();
    }

    ImageSize(String ending) {
        this.ending = ending;
    }

    public String getEnding() {
        return this.ending;
    }
}
