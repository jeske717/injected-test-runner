package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.ObjectGraph;

public class InjectedActivityUnitTestCase<T extends Activity> extends ActivityUnitTestCase<T> {
    public InjectedActivityUnitTestCase(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getAbsolutePath());
        MockitoAnnotations.initMocks(this);
    }

    @Override
    protected T startActivity(Intent intent, Bundle savedInstanceState, Object lastNonConfigurationInstance) {
        T result = super.startActivity(intent, savedInstanceState, lastNonConfigurationInstance);
        List<Object> customModules = new ArrayList<Object>(getCustomModules());
        if (getClass().getAnnotation(MockModule.class) != null) {
            customModules.add(instantiateMockModule());
        }
        ObjectGraph.create(customModules.toArray(new Object[customModules.size()])).inject(result);
        return result;
    }

    protected List<?> getCustomModules() {
        return Collections.emptyList();
    }

    private Object instantiateMockModule() {
        Class<?> moduleClass = null;
        try {
            moduleClass = Class.forName(getClass().getName() + "MockModule");
            Constructor<?> constructor = moduleClass.getDeclaredConstructor(calculateMockModuleConstructor());
            return constructor.newInstance(buildConstructorArgs());
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

    private Object[] buildConstructorArgs() throws IllegalAccessException {
        List<Object> result = new ArrayList<Object>();
        for (Field field : getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(Mock.class) != null) {
                result.add(field.get(this));
            }
        }
        result.add(new Object());
        return result.toArray(new Object[result.size()]);
    }

    private Class<?>[] calculateMockModuleConstructor() {
        List<Class<?>> result = new ArrayList<Class<?>>();
        for (Field field : getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(Mock.class) != null) {
                result.add(field.getType());
            }
        }
        result.add(Object.class);
        return result.toArray(new Class<?>[result.size()]);
    }
}
