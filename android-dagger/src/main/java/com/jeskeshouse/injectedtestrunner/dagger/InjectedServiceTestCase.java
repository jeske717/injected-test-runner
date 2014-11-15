package com.jeskeshouse.injectedtestrunner.dagger;

import android.app.Service;
import android.content.Intent;
import android.test.ServiceTestCase;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.ObjectGraph;

public class InjectedServiceTestCase<T extends Service> extends ServiceTestCase<T> {

    public InjectedServiceTestCase(Class<T> serviceClass) {
        super(serviceClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getSystemContext().getCacheDir().getAbsolutePath());
        MockitoAnnotations.initMocks(this);
    }

    @Override
    protected void startService(Intent intent) {
        super.startService(intent);
        List<Object> customModules = new ArrayList<Object>(getCustomModules());
        if (getClass().getAnnotation(MockModule.class) != null) {
            customModules.add(instantiateMockModule());
        }
        ObjectGraph.create(customModules.toArray(new Object[customModules.size()])).inject(getService());
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
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object[] buildConstructorArgs() throws IllegalAccessException {
        List<Object> result = new ArrayList<Object>();
        for (Field field : getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(Mock.class) != null) {
                result.add(field.get(this));
            }
        }
        return result.toArray(new Object[result.size()]);
    }

    private Class<?>[] calculateMockModuleConstructor() {
        List<Class<?>> result = new ArrayList<Class<?>>();
        for (Field field : getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getAnnotation(Mock.class) != null) {
                result.add(field.getDeclaringClass());
            }
        }
        result.add(Object.class);
        return result.toArray(new Class<?>[result.size()]);
    }
}
