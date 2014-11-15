package com.jeskeshouse.injectedtestrunner;

import android.content.Intent;

import com.google.inject.Provides;
import com.jeskeshouse.injectedtestrunner.components.TestActivity;
import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.injectables.InjectableThing;

import org.mockito.Mock;

import javax.inject.Inject;
import javax.inject.Named;

@RequiredModules(TestModule.class)
public class InjectedActivityTest extends InjectedActivityUnitTestCase<TestActivity> {

    @Mock
    private InjectableThing injectableThing;

    @Mock
    @Named("named")
    private AnotherInjectableThing namedThing;

    @Mock
    @Named("provided")
    private AnotherInjectableThing providedThing;

    @Inject
    @Named("providedByModule")
    private AnotherInjectableThing providedByModuleThing;

    public InjectedActivityTest() {
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

        assertSame(providedByModuleThing, injected);
    }

    public void testRoboGuiceInjectsExtras() throws Exception {
        Intent intent = new Intent(getInstrumentation().getTargetContext(), TestActivity.class);
        intent.putExtra(TestActivity.EXTRA_NAME, "inject this!");
        startActivity(intent, null, null);

        String injected = getActivity().injectedExtra;

        assertEquals("inject this!", injected);
    }

    @Provides
    @Named("provided")
    public AnotherInjectableThing anotherInjectableThing() {
        return providedThing;
    }
}
