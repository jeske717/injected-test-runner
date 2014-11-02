package com.jeskeshouse.injectedtestrunner;

import android.content.Intent;

import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.injectables.InjectableThing;

import org.mockito.Mock;

import javax.inject.Named;

public class InjectedServiceTestCase extends InjectedServiceUnitTestCase<TestService> {

    @Mock
    private InjectableThing injectableThing;

    @Mock
    @Named("named")
    private AnotherInjectableThing namedThing;

    public InjectedServiceTestCase() {
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
}
