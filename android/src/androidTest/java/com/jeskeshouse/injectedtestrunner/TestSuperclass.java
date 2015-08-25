package com.jeskeshouse.injectedtestrunner;

import com.google.inject.Provides;
import com.jeskeshouse.injectedtestrunner.components.TestSuperclassActivity;
import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.injectables.InjectableThing;

import org.mockito.Mock;

import javax.inject.Named;

import static org.mockito.Mockito.mock;

public class TestSuperclass extends InjectedActivityUnitTestCase<TestSuperclassActivity> {

    @Mock
    @Named("injectableThingFromSuperclass")
    protected InjectableThing injectableThingFromSuperclass;

    @Mock
    protected AnotherInjectableThing anotherInjectableThing;

    protected InjectableThing providerMethodInjectableThing;

    public TestSuperclass(Class<TestSuperclassActivity> activityClass) {
        super(activityClass);
    }

    @Provides
    @Named("providerMethodInjectableThing")
    public InjectableThing providerMethodInjectableThing() {
        providerMethodInjectableThing = mock(InjectableThing.class);
        return providerMethodInjectableThing;
    }
}
