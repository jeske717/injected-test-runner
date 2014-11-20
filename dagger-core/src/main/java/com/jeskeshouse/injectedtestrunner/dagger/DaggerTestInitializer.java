package com.jeskeshouse.injectedtestrunner.dagger;

import com.jeskeshouse.daggermodules.Modules;

import org.mockito.Mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DaggerTestInitializer {

    public static void addModulesToObjectGraph(Object test, List<?> modules) {
        List<Object> customModules = new ArrayList<Object>(modules);
        if (test.getClass().getAnnotation(MockModule.class) != null) {
            customModules.add(instantiateMockModule(test));
        }
        Modules.save();
        for (Object customModule : customModules) {
            Modules.install(customModule);
        }
    }

    public static void resetModules() {
        Modules.restore();
    }

    private static Object instantiateMockModule(Object test) {
        Class<?> moduleClass = null;
        try {
            moduleClass = Class.forName(test.getClass().getName() + "MockModule");
            Constructor<?> constructor = moduleClass.getDeclaredConstructor(calculateMockModuleConstructor(test));
            return constructor.newInstance(buildConstructorArgs(test));
        } catch (ClassNotFoundException e) {
            throw new AssertionError(e);
        } catch (NoSuchMethodException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
    }

    private static Object[] buildConstructorArgs(Object test) throws IllegalAccessException {
        Map<Class<?>, List<Object>> result = new TreeMap<Class<?>, List<Object>>(new SimpleClassComparator());
        for (Field field : test.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(Mock.class) != null) {
                if (result.get(field.getType()) == null) {
                    result.put(field.getType(), new ArrayList<Object>());
                }
                result.get(field.getType()).add(field.get(test));
            }
        }
        List<Object> combined = new ArrayList<Object>();
        for (List<Object> objects : result.values()) {
            combined.addAll(objects);
        }
        combined.add(new Object());
        return combined.toArray(new Object[result.size()]);
    }

    private static Class<?>[] calculateMockModuleConstructor(Object test) {
        List<Class<?>> result = new ArrayList<Class<?>>();
        for (Field field : test.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(Mock.class) != null) {
                result.add(field.getType());
            }
        }
        Collections.sort(result, new SimpleClassComparator());
        result.add(Object.class);
        return result.toArray(new Class<?>[result.size()]);
    }

    private static class SimpleClassComparator implements Comparator<Class<?>> {

        @Override
        public int compare(Class<?> o1, Class<?> o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
