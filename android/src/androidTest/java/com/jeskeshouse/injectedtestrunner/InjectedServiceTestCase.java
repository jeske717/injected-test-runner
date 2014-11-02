package com.jeskeshouse.injectedtestrunner;

import android.content.Intent;

import org.mockito.Mock;

public class InjectedServiceTestCase extends InjectedServiceUnitTestCase<TestService> {

    @Mock
    private InjectableThing injectableThing;

    public InjectedServiceTestCase() {
        super(TestService.class);
    }

    public void testInjectableThingIsAutomaticallyInjected() throws Exception {
        startService(new Intent(getSystemContext(), TestService.class));

        InjectableThing injected = getService().injectableThing;

        assertSame(injectableThing, injected);
    }
}
