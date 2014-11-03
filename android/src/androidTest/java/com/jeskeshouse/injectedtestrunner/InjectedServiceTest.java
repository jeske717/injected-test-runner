package com.jeskeshouse.injectedtestrunner;

import android.content.Intent;

import com.google.inject.Provides;
import com.jeskeshouse.injectedtestrunner.components.TestService;
import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.injectables.InjectableThing;

import org.mockito.Mock;

import javax.inject.Named;

@RequiredModules(TestModule.class)
public class InjectedServiceTest extends InjectedServiceTestCase<TestService> {

    @Mock
    private InjectableThing injectableThing;

    @Mock
    @Named("named")
    private AnotherInjectableThing namedThing;

    @Mock
    @Named("provided")
    private AnotherInjectableThing providedThing;

    public InjectedServiceTest() {
        super(TestService.class);
    }

    public void testInjectableThingIsAutomaticallyInjected() throws Exception {
        startService(new Intent(getSystemContext(), TestService.class));

        InjectableThing injected = getService().injectableThing;

        assertSame(injectableThing, injected);
    }

    public void testNamedInjectableThingIsAutomaticallyInjected() throws Exception {
        startService(new Intent(getSystemContext(), TestService.class));

        AnotherInjectableThing injected = getService().namedThing;

        assertSame(namedThing, injected);
    }

    public void testProvidedInjectableThingIsAutomaticallyInjected() throws Exception {
        startService(new Intent(getSystemContext(), TestService.class));

        AnotherInjectableThing injected = getService().providedThing;

        assertSame(providedThing, injected);
    }

    public void testThingProvidedByRequiredModuleIsAutomaticallyInjected() throws Exception {
        startService(new Intent(getSystemContext(), TestService.class));

        AnotherInjectableThing injected = getService().providedByModuleThing;

        assertSame(TestModule.provided, injected);
    }

    @Provides
    @Named("provided")
    public AnotherInjectableThing anotherInjectableThing() {
        return providedThing;
    }
}
