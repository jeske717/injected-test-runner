package com.jeskeshouse.injectedtestrunner;

import android.app.Application;
import android.content.ContentProvider;
import android.test.AndroidTestCase;
import android.test.mock.MockContentResolver;

import com.google.inject.Module;
import com.google.inject.util.Modules;

import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import roboguice.RoboGuice;

public class InjectedContentProviderTestCase<T extends ContentProvider> extends AndroidTestCase {

    private final Class<T> providerClass;
    private final String providerAuthority;

    private T provider;

    public InjectedContentProviderTestCase(Class<T> providerClass, String providerAuthority) {
        this.providerClass = providerClass;
        this.providerAuthority = providerAuthority;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getContext().getCacheDir().getAbsolutePath());
        MockitoAnnotations.initMocks(this);

        Application application = (Application) getContext().getApplicationContext();

        List<Dependency> dependencies = new ArrayList<Dependency>();
        for(DependencyProvider provider : Arrays.asList(new MockitoFieldDependencyProvider(), new ProvidesMethodDependencyProvider())) {
            dependencies.addAll(provider.getDependencies(this));
        }

        List<Module> modules = ModuleFactory.instantiateModulesForTest(this, getContext(), dependencies);
        RoboGuice.overrideApplicationInjector(application,
                Modules.override(RoboGuice.newDefaultRoboModule(application)).with(modules.toArray(new Module[modules.size()])));

        MockContentResolver resolver = new MockContentResolver();

        provider = providerClass.newInstance();
        provider.attachInfo(getContext().getApplicationContext(), null);
        resolver.addProvider(providerAuthority, getProvider());
    }

    public T getProvider() {
        return provider;
    }
}
