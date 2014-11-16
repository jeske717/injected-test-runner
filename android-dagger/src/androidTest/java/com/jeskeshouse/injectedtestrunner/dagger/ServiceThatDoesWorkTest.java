package com.jeskeshouse.injectedtestrunner.dagger;

import android.content.Intent;

import com.jeskeshouse.injectedtestrunner.dagger.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.InjectableThing;

import org.mockito.Mock;

import javax.inject.Named;

@MockModule(injects = ServiceThatDoesWork.class)
public class ServiceThatDoesWorkTest extends InjectedServiceTestCase<ServiceThatDoesWork> {

    @Mock
    private InjectableThing injectableThing;

    @Mock
    @Named("named")
    private AnotherInjectableThing namedThing;

    @Mock
    @Named("provided")
    private AnotherInjectableThing providedThing;

    public ServiceThatDoesWorkTest() {
        super(ServiceThatDoesWork.class);
    }

    public void testInjectableThingIsAutomaticallyInjected() throws Exception {
        startService(new Intent(getSystemContext(), ServiceThatDoesWork.class));

        InjectableThing injected = getService().injectableThing;

        assertSame(injectableThing, injected);
    }

    public void testNamedInjectableThingIsAutomaticallyInjected() throws Exception {
        startService(new Intent(getSystemContext(), ServiceThatDoesWork.class));

        AnotherInjectableThing injected = getService().namedThing;

        assertSame(namedThing, injected);
    }
}
