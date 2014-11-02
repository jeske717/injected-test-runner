package com.jeskeshouse.injectedtestrunner;

import android.content.Context;
import com.google.inject.Module;

import java.util.ArrayList;
import java.util.List;

class ModuleFactory {

    static List<Module> instantiateModulesForTest(Object test, Context context, List<Dependency> objects) {
        List<Module> modules = new ArrayList<Module>();
        modules.add(new MockitoModule(objects));
        if (test.getClass().isAnnotationPresent(RequiredModules.class)) {
            try {
                for (Class<? extends Module> moduleClass : test.getClass().getAnnotation(RequiredModules.class).value()) {
                    try {
                        modules.add(moduleClass.getDeclaredConstructor(Context.class).newInstance(context));
                    } catch (NoSuchMethodException ignored) {
                        modules.add(moduleClass.newInstance());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Unable to instantiate configured modules!", e);
            }
        }
        return modules;
    }
}
