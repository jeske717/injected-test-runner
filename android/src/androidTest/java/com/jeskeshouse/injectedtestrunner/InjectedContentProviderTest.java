package com.jeskeshouse.injectedtestrunner;

import com.google.inject.Provides;
import com.jeskeshouse.injectedtestrunner.components.TestProvider;
import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.injectables.InjectableThing;

import org.mockito.Mock;

import javax.inject.Named;

@RequiredModules(TestModule.class)
public class InjectedContentProviderTest extends InjectedContentProviderTestCase<TestProvider> {

    public InjectedContentProviderTest() {
        super(TestProvider.class, "com.jeskeshouse.injectedtestrunner");
    }

    @Mock
    private InjectableThing injectableThing;

    @Mock
    @Named("named")
    private AnotherInjectableThing namedThing;

    @Mock
    @Named("provided")
    private AnotherInjectableThing providedThing;

    public void testInjectableThingIsAutomaticallyInjected() throws Exception {

        InjectableThing injected = getProvider().injectableThing;

        assertSame(injectableThing, injected);
    }

    public void testNamedInjectableThingIsAutomaticallyInjected() throws Exception {

        AnotherInjectableThing injected = getProvider().namedThing;

        assertSame(namedThing, injected);
    }

    public void testProvidedInjectableThingIsAutomaticallyInjected() throws Exception {

        AnotherInjectableThing injected = getProvider().providedThing;

        assertSame(providedThing, injected);
    }

    public void testThingProvidedByRequiredModuleIsAutomaticallyInjected() throws Exception {

        AnotherInjectableThing injected = getProvider().providedByModuleThing;

        assertSame(TestModule.provided, injected);
    }

    @Provides
    @Named("provided")
    public AnotherInjectableThing anotherInjectableThing() {
        return providedThing;
    }
}
