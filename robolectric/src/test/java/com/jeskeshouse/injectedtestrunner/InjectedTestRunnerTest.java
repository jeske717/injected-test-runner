package com.jeskeshouse.injectedtestrunner;

import android.content.Intent;

import com.google.inject.Provides;
import com.google.inject.name.Named;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

@RunWith(InjectedTestRunner.class)
@RequiredModules(TestModule.class)
@Config(manifest = Config.NONE)
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

    @Inject
    @Named("providedByTest1")
    private AnotherInjectableThing anotherInjectableThing;

    @Inject
    @Named("providedByTest2")
    private AnotherInjectableThing anotherInjectableThingWithName;

    @Inject
    private GenericThing<InjectableThing> providedGenericThing;

    @Mock
    private InjectableThing thing;

    @Mock
    private GenericThing<String> genericThing;

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

    @Test
    public void methodsInTestAnnotatedWithProvidesCanBeInjected() throws Exception {
        ActivityWithDependencies activityWithDependencies = ActivityController.of(ActivityWithDependencies.class).create().get();

        assertSame(anotherInjectableThing, activityWithDependencies.anotherInjectableThing);
    }

    @Test
    public void methodsInTestAnnotatedWithProvidesCanBeInjectedByName() throws Exception {
        ActivityWithDependencies activityWithDependencies = ActivityController.of(ActivityWithDependencies.class).create().get();

        assertSame(anotherInjectableThingWithName, activityWithDependencies.anotherInjectableThingWithName);
    }

    @Test
    public void typesWithGenericsCanBeInjected() throws Exception {
        ActivityWithDependencies activityWithDependencies = ActivityController.of(ActivityWithDependencies.class).create().get();

        assertSame(genericThing, activityWithDependencies.genericThing);
    }

    @Test
    public void typesWithGenericsCanBeInjectedViaProvidesMethods() throws Exception {
        ActivityWithDependencies activityWithDependencies = ActivityController.of(ActivityWithDependencies.class).create().get();

        assertSame(providedGenericThing, activityWithDependencies.providedGenericThing);
    }

    @Test
    public void extrasAreInjectedByRoboGuice() throws Exception {
        Intent intent = new Intent(Robolectric.application, ActivityWithDependencies.class);
        intent.putExtra(ActivityWithDependencies.EXTRA_NAME, "inject this!");
        ActivityWithDependencies activityWithDependencies = ActivityController.of(ActivityWithDependencies.class).withIntent(intent).create().get();

        assertEquals("inject this!", activityWithDependencies.injectedExtra);
    }

    private AnotherInjectableThing anotherInjectableThingForProvides = new AnotherInjectableThing();
    private AnotherInjectableThing anotherInjectableThingWithAName = new AnotherInjectableThing();
    private GenericThing<InjectableThing> genericThingForProvides = new GenericThing<InjectableThing>();

    @Provides
    @Named("providedByTest1")
    public AnotherInjectableThing anotherInjectableThing() {
        return anotherInjectableThingForProvides;
    }

    @Provides
    @Named("providedByTest2")
    public AnotherInjectableThing anotherInjectableThingWithAName() {
        return anotherInjectableThingWithAName;
    }

    @Provides
    public GenericThing<InjectableThing> genericThing() {
        return genericThingForProvides;
    }
}