package com.jeskeshouse.injectedtestrunner;

import android.content.Intent;

import org.mockito.Mock;

public class InjectedActivityTestCase extends InjectedActivityUnitTestCase<TestActivity> {

    @Mock
    private InjectableThing injectableThing;

    public InjectedActivityTestCase() {
        super(TestActivity.class);
    }

    public void testInjectableThingIsAutomaticallyInjected() throws Exception {
        startActivity(new Intent(getInstrumentation().getTargetContext(), TestActivity.class), null, null);

        InjectableThing injected = getActivity().injectableThing;

        assertSame(injectableThing, injected);
    }
}
