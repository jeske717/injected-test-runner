package com.jeskeshouse.daggermodules;

public class Dependency {
    private final String toString;

    public Dependency(String toString) {
        this.toString = toString;
    }

    @Override
    public String toString() {
        return toString;
    }
}
