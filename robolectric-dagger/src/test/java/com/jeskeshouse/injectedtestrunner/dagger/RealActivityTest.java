package com.jeskeshouse.injectedtestrunner.dagger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static com.jeskeshouse.injectedtestrunner.dagger.DaggerInjectedTestRunner.injectMocks;
import static org.junit.Assert.assertSame;

@RunWith(DaggerInjectedTestRunner.class)
@Config(manifest = Config.NONE)
@MockModule
public class RealActivityTest {

    @Mock
    private InjectableThing injectableThing;

    @Test
    public void mocksCanBeInjected() throws Exception {
        RealActivity activity = ActivityController.of(RealActivity.class).create().get();
        injectMocks(this, activity);

        InjectableThing injected = activity.injectableThing;

        assertSame(injectableThing, injected);
    }
}
