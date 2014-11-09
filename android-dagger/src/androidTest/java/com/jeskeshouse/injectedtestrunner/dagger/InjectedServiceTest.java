package com.jeskeshouse.injectedtestrunner.dagger;

import android.content.Intent;

import com.jeskeshouse.injectedtestrunner.dagger.components.TestService;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.dagger.injectables.InjectableThing;

import org.mockito.Mock;

import javax.inject.Named;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

public class InjectedServiceTest extends InjectedServiceTestCase<TestService> {

    @Mock
    private InjectableThing injectableThing;

    @Mock
    @Named("named")
    private AnotherInjectableThing namedThing;

    @Mock
    @Named("provided")
    private AnotherInjectableThing providedThing;

    public InjectedServiceTest() {
        super(TestService.class);
    }

    public void testInjectableThingIsAutomaticallyInjected() throws Exception {
        startService(new Intent(getSystemContext(), TestService.class));
        ObjectGraph.create(new MockitoModule(injectableThing, namedThing, providedThing)).inject(getService());

        InjectableThing injected = getService().injectableThing;

        assertSame(injectableThing, injected);
    }

    public void testNamedInjectableThingIsAutomaticallyInjected() throws Exception {
        startService(new Intent(getSystemContext(), TestService.class));
        ObjectGraph.create(new MockitoModule(injectableThing, namedThing, providedThing)).inject(getService());

        AnotherInjectableThing injected = getService().namedThing;

        assertSame(namedThing, injected);
    }

    @Module(injects = TestService.class, overrides = true)
    public static class MockitoModule {

        private final InjectableThing injectableThing;
        private final AnotherInjectableThing namedThing;
        private final AnotherInjectableThing providedThing;

        public MockitoModule(InjectableThing injectableThing, AnotherInjectableThing namedThing, AnotherInjectableThing providedThing) {
            this.injectableThing = injectableThing;
            this.namedThing = namedThing;
            this.providedThing = providedThing;
        }

        @Provides
        public InjectableThing injectableThing() {
            return injectableThing;
        }

        @Provides
        @Named("named")
        public AnotherInjectableThing namedThing() {
            return namedThing;
        }

        @Provides
        @Named("provided")
        public AnotherInjectableThing providedThing() {
            return providedThing;
        }
    }
}