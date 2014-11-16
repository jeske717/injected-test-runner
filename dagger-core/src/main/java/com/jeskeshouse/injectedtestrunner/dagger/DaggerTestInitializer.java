package com.jeskeshouse.injectedtestrunner.dagger;

import org.mockito.Mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dagger.ObjectGraph;

public class DaggerTestInitializer {

    public static void injectTestSubject(Object test, Object testSubject, List<?> modules) {
        List<Object> customModules = new ArrayList<Object>(modules);
        if (test.getClass().getAnnotation(MockModule.class) != null) {
            customModules.add(instantiateMockModule(test));
        }
        ObjectGraph.create(customModules.toArray(new Object[customModules.size()])).inject(testSubject);
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
        List<Object> result = new ArrayList<Object>();
        for (Field field : test.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(Mock.class) != null) {
                result.add(field.get(test));
            }
        }
        Collections.sort(result, new SimpleObjectClassComparator());
        result.add(new Object());
        return result.toArray(new Object[result.size()]);
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

    private static class SimpleObjectClassComparator implements Comparator<Object> {

        @Override
        public int compare(Object o1, Object o2) {
            return o1.getClass().getName().compareTo(o2.getClass().getName());
        }
    }
}
