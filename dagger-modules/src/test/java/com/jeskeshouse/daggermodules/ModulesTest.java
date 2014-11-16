package com.jeskeshouse.daggermodules;

import org.junit.After;
import org.junit.Test;

import javax.inject.Inject;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class ModulesTest {

    @Inject
    Dependency dependency;

    private TestModule1 module1 = new TestModule1();
    private TestModule2 module2 = new TestModule2();

    @After
    public void tearDown() throws Exception {
        Modules.uninstall(module1);
        Modules.uninstall(module2);
    }

    @Test
    public void afterInstallingAModuleDependenciesCanBeInjected() throws Exception {
        Modules.install(module1);

        Modules.asObjectGraph().inject(this);

        assertSame(dependency, TestModule1.INSTANCE);
    }

    @Test
    public void asObjectGraphReturnsSameGraphWhenModulesHaveNotBeenChanged() throws Exception {
        Modules.install(module1);

        ObjectGraph objectGraph1 = Modules.asObjectGraph();
        ObjectGraph objectGraph2 = Modules.asObjectGraph();

        assertSame(objectGraph1, objectGraph2);
    }

    @Test
    public void asObjectGraphReturnsNewGraphWhenModulesHaveChanged() throws Exception {
        Modules.install(module1);
        ObjectGraph objectGraph1 = Modules.asObjectGraph();

        Modules.install(module2);
        ObjectGraph objectGraph2 = Modules.asObjectGraph();

        assertNotSame(objectGraph1, objectGraph2);
        assertSame(TestModule2.INSTANCE, objectGraph2.inject(this).dependency);
    }

    @Test
    public void afterUninstallingModuleItsDependenciesAreNoLongerUsed() throws Exception {
        Modules.install(module1);
        Modules.asObjectGraph();
        Modules.install(module2);
        Modules.asObjectGraph();
        Modules.uninstall(module2);

        ObjectGraph objectGraph = Modules.asObjectGraph();

        assertSame(TestModule1.INSTANCE, objectGraph.inject(this).dependency);
    }

    @Test
    public void afterSaveAndRestoreOnlyModulesInstalledAtSaveTimeAreUsed() throws Exception {
        Modules.install(module1);
        Modules.save();
        Modules.install(module2);
        Modules.restore();

        ObjectGraph objectGraph = Modules.asObjectGraph();

        assertSame(TestModule1.INSTANCE, objectGraph.inject(this).dependency);
    }

    @Test
    public void afterMultipleSavesLastSavePointIsUsed() throws Exception {
        Modules.install(module1);
        Modules.save();
        Modules.install(module2);
        Modules.save();
        Modules.restore();

        ObjectGraph objectGraph = Modules.asObjectGraph();

        assertSame(TestModule2.INSTANCE, objectGraph.inject(this).dependency);
    }

    @Module(injects = ModulesTest.class, library = true)
    public static class TestModule1 {
        public static final Dependency INSTANCE = new Dependency("Module ONE");

        @Provides
        public Dependency dependency() {
            return INSTANCE;
        }
    }

    @Module(injects = ModulesTest.class, library = true, overrides = true)
    public static class TestModule2 {

        public static final Dependency INSTANCE = new Dependency("Module TWO");

        @Provides
        public Dependency dependency() {
            return INSTANCE;
        }
    }
}
