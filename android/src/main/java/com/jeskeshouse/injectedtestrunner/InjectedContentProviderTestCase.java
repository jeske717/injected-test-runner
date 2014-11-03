package com.jeskeshouse.injectedtestrunner;

import android.content.ContentProvider;
import android.test.AndroidTestCase;
import android.test.mock.MockContentResolver;

public class InjectedContentProviderTestCase<T extends ContentProvider> extends AndroidTestCase {

    private final Class<T> providerClass;
    private final String providerAuthority;

    private T provider;
    private MockContentResolver resolver;

    public InjectedContentProviderTestCase(Class<T> providerClass, String providerAuthority) {
        this.providerClass = providerClass;
        this.providerAuthority = providerAuthority;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GuiceInitializer.initialize(this, new AndroidTestCaseInitializationStrategy(this));
        resolver = new MockContentResolver();
        provider = providerClass.newInstance();
        provider.attachInfo(getContext().getApplicationContext(), null);
        resolver.addProvider(providerAuthority, getProvider());
    }

    public T getProvider() {
        return provider;
    }

    public MockContentResolver getMockContentResolver() {
        return resolver;
    }
}
