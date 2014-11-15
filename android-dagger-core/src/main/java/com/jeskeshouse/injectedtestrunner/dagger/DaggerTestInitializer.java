package com.jeskeshouse.injectedtestrunner.dagger;

import org.mockito.Mock;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

class DaggerTestInitializer {

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
        result.add(Object.class);
        return result.toArray(new Class<?>[result.size()]);
    }
}
