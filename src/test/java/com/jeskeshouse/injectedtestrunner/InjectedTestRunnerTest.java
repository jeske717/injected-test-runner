package com.jeskeshouse.injectedtestrunner;

import com.google.inject.name.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.util.ActivityController;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

@RunWith(InjectedTestRunner.class)
@RequiredModules(TestModule.class)
public class InjectedTestRunnerTest {

    @Mock
    @Named("objectOne")
    private InjectableThing objectOne;

    @Mock
    @Named("objectTwo")
    private InjectableThing objectTwo;

    @Inject
    @Named("objectFromModule")
    private InjectableThing objectFromModule;

    @Mock
    private InjectableThing thing;

    @Test
    public void mocksAreInitialized() throws Exception {
        assertNotNull(objectOne);
        assertNotNull(objectTwo);
        assertNotNull(thing);
    }

    @Test
    public void sameTypeCanBeMockedByName() throws Exception {
        assertNotSame(objectOne, objectTwo);
    }

    @Test
    public void dependenciesProvidedByModulesCanBeInjected() throws Exception {
        assertNotNull(objectFromModule);
    }

    @Test
    public void injectedDependenciesAreSatisfiedByMocks() throws Exception {
        ActivityWithDependencies activityWithDependencies = ActivityController.of(ActivityWithDependencies.class).create().get();

        assertSame(thing, activityWithDependencies.dependency);
    }

    @Test
    public void namedDependenciesAreSatisfiedByMocksWithTheSameName() throws Exception {
        ActivityWithDependencies activityWithDependencies = ActivityController.of(ActivityWithDependencies.class).create().get();

        assertSame(objectOne, activityWithDependencies.objectOne);
        assertSame(objectTwo, activityWithDependencies.objectTwo);
    }

}