package com.jeskeshouse.injectedtestrunner.dagger;

import android.content.Intent;

import com.jeskeshouse.injectedtestrunner.dagger.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.InjectableThing;

import org.mockito.Mock;

import javax.inject.Named;

@MockModule
public class ActivityThatDoesWorkTest extends InjectedActivityUnitTestCase<ActivityThatDoesWork> {

    @Mock
    private InjectableThing injectableThing;

    @Mock
    @Named("named")
    private AnotherInjectableThing namedThing;

    @Mock
    @Named("provided")
    private AnotherInjectableThing providedThing;

    public ActivityThatDoesWorkTest() {
        super(ActivityThatDoesWork.class);
    }

    public void testInjectableThingIsAutomaticallyInjected() throws Exception {
        startActivity(new Intent(getInstrumentation().getTargetContext(), ActivityThatDoesWork.class), null, null);

        InjectableThing injected = getActivity().injectableThing;

        assertSame(injectableThing, injected);
    }

    public void testNamedInjectableThingIsAutomaticallyInjected() throws Exception {
        startActivity(new Intent(getInstrumentation().getTargetContext(), ActivityThatDoesWork.class), null, null);

        AnotherInjectableThing injected = getActivity().namedThing;

        assertSame(namedThing, injected);
    }
}