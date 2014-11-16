package com.jeskeshouse.injectedtestrunner.dagger;

import android.content.ContentProvider;
import android.test.AndroidTestCase;
import android.test.mock.MockContentResolver;

import java.util.Collections;
import java.util.List;

public class InjectedContentProviderTestCase<T extends ContentProvider> extends AndroidTestCase {

    private final Class<T> providerClass;
    private final String providerAuthority;

    private T provider;
    private MockContentResolver resolver;

    public InjectedContentProviderTestCase(Class<T> providerClass, String authority) {
        this.providerClass = providerClass;
        this.providerAuthority = authority;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        AndroidMockitoInitializer.setupMockito(this, getContext());
        resolver = new MockContentResolver();
        provider = providerClass.newInstance();
        provider.attachInfo(getContext().getApplicationContext(), null);
        resolver.addProvider(providerAuthority, getProvider());
        DaggerTestInitializer.injectTestSubject(this, provider, getCustomModules());
    }

    protected List<?> getCustomModules() {
        return Collections.emptyList();
    }

    public T getProvider() {
        return provider;
    }

    public MockContentResolver getMockContentResolver() {
        return resolver;
    }
}
