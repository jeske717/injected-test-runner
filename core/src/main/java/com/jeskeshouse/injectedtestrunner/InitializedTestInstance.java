package com.jeskeshouse.injectedtestrunner;

class InitializedTestInstance {

    private static Object test;

    static void set(Object test) {
        InitializedTestInstance.test = test;
    }

    static Object get() {
        return test;
    }

}
