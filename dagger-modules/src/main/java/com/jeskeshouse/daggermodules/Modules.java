package com.jeskeshouse.daggermodules;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

public class Modules {

    private static List<Object> modules = new ArrayList<Object>();
    private static List<Object> savedState = new ArrayList<Object>();
    private static ObjectGraph currentGraph;
    private static boolean createGraph = true;

    private Modules() {
    }

    public synchronized static void install(Object module) {
        modules.add(module);
        createGraph = true;
    }

    public synchronized static void uninstall(Object module) {
        modules.remove(module);
        createGraph = true;
    }

    public synchronized static ObjectGraph asObjectGraph() {
        if (createGraph) {
            currentGraph = ObjectGraph.create(modules.toArray());
            createGraph = false;
        }
        return currentGraph;
    }

    public synchronized static void save() {
        savedState.clear();
        savedState.addAll(modules);
    }

    public synchronized static void restore() {
        modules.clear();
        modules.addAll(savedState);
        createGraph = true;
    }
}
