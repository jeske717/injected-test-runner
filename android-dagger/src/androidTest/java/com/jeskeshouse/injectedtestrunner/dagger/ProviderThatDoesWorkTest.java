package com.jeskeshouse.injectedtestrunner.dagger;

import com.jeskeshouse.injectedtestrunner.dagger.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.InjectableThing;

import org.mockito.Mock;

import javax.inject.Named;

@MockModule
public class ProviderThatDoesWorkTest extends InjectedContentProviderTestCase<ProviderThatDoesWork> {

    public ProviderThatDoesWorkTest() {
        super(ProviderThatDoesWork.class, "com.jeskeshouse.injectedtestrunner");
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
}
