package com.jeskeshouse.injectedtestrunner;

import android.content.Intent;

import com.google.inject.Provides;
import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.injectables.InjectableThing;

import org.mockito.Mock;

import javax.inject.Named;

@RequiredModules(TestModule.class)
public class InjectedActivityTestCase extends InjectedActivityUnitTestCase<TestActivity> {

    @Mock
    private InjectableThing injectableThing;

    @Mock
    @Named("named")
    private AnotherInjectableThing namedThing;

    @Mock
    @Named("provided")
    private AnotherInjectableThing providedThing;

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

    public void testProvidedInjectableThingIsAutomaticallyInjected() throws Exception {
        startActivity(new Intent(getInstrumentation().getTargetContext(), TestActivity.class), null, null);

        AnotherInjectableThing injected = getActivity().providedThing;

        assertSame(providedThing, injected);
    }

    public void testThingProvidedByRequiredModuleIsAutomaticallyInjected() throws Exception {
        startActivity(new Intent(getInstrumentation().getTargetContext(), TestActivity.class), null, null);

        AnotherInjectableThing injected = getActivity().providedByModuleThing;

        assertSame(TestModule.provided, injected);
    }

    @Provides
    @Named("provided")
    public AnotherInjectableThing anotherInjectableThing() {
        return providedThing;
    }
}
