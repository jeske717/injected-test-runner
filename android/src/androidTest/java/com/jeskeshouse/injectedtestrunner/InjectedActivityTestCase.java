package com.jeskeshouse.injectedtestrunner;

import android.content.Intent;

import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.injectables.InjectableThing;

import org.mockito.Mock;

import javax.inject.Named;

public class InjectedActivityTestCase extends InjectedActivityUnitTestCase<TestActivity> {

    @Mock
    private InjectableThing injectableThing;

    @Mock
    @Named("named")
    private AnotherInjectableThing namedThing;

    public InjectedActivityTestCase() {
        super(TestActivity.class);
    }

    public void testInjectableThingIsAutomaticallyInjected() throws Exception {
        startActivity(new Intent(getInstrumentation().getTargetContext(), TestActivity.class), null, null);

        InjectableThing injected = getActivity().injectableThing;

        assertSame(injectableThing, injected);
    }

    public void testNamedInjectableThingIsAutomaticallyInjected() throws Exception {
        startActivity(new Intent(getInstrumentation().getTargetContext(), TestActivity.class), null, null);

        AnotherInjectableThing injected = getActivity().namedThing;

        assertSame(namedThing, injected);
    }
}
