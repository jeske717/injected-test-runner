package com.jeskeshouse.injectedtestrunner.dagger;

import android.content.Intent;

import com.jeskeshouse.injectedtestrunner.dagger.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.BasicServiceConnection;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.EmployeeService;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.GenericThing;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.InjectableThing;

import org.mockito.Mock;

import javax.inject.Named;

@MockModule(injects = ActivityThatDoesWork.class)
public class ActivityThatDoesWorkTest extends InjectedActivityUnitTestCase<ActivityThatDoesWork> {

    @Mock
    private InjectableThing injectableThing;

    @Mock
    @Named("named")
    private AnotherInjectableThing namedThing;

    @Mock
    @Named("provided")
    private AnotherInjectableThing providedThing;

    @Mock
    private GenericThing<String> genericThing;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private BasicServiceConnection basicServiceConnection;

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

    public void testGenericTypesAreAutomaticallyInjected() throws Exception {
        startActivity(new Intent(getInstrumentation().getTargetContext(), ActivityThatDoesWork.class), null, null);

        GenericThing<String> injected = getActivity().genericThing;

        assertSame(genericThing, injected);
    }

    public void testTheMockModuleIsInstantiatedWithTheCorrectArgumentsWhenClassNamesAreOutOfOrder() throws Exception {
        startActivity(new Intent(getInstrumentation().getTargetContext(), ActivityThatDoesWork.class), null, null);

        EmployeeService injected = getActivity().employeeService;
        BasicServiceConnection injected2 = getActivity().basicServiceConnection;

        assertSame(employeeService, injected);
        assertSame(basicServiceConnection, injected2);
    }
}