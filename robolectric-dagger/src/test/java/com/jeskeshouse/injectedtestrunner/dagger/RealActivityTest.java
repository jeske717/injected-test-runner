package com.jeskeshouse.injectedtestrunner.dagger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static org.junit.Assert.assertSame;

@RunWith(DaggerInjectedTestRunner.class)
@Config(manifest = Config.NONE)
@MockModule(injects = RealActivity.class)
public class RealActivityTest {

    @Mock
    private InjectableThing injectableThing;

    @Test
    public void mocksAreAutomaticallyInjected() throws Exception {
        RealActivity activity = ActivityController.of(RealActivity.class).create().get();

        InjectableThing injected = activity.injectableThing;

        assertSame(injectableThing, injected);
    }
}
