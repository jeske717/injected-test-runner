package com.jeskeshouse.injectedtestrunner.dagger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertSame;

@RunWith(DaggerInjectedTestRunner.class)
@MockModule(injects = RealActivity.class)
@Config(constants = BuildConfig.class)
public class RealActivityTest {

    @Mock
    private InjectableThing injectableThing;

    @Test
    public void mocksAreAutomaticallyInjected() throws Exception {
        RealActivity activity = Robolectric.setupActivity(RealActivity.class);

        InjectableThing injected = activity.injectableThing;

        assertSame(injectableThing, injected);
    }
}
