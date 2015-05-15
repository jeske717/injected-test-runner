package com.jeskeshouse.injectedtestrunner;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.test.ActivityUnitTestCase;
import android.test.mock.MockApplication;
import android.util.Log;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class InjectedActivityUnitTestCase<T extends Activity> extends ActivityUnitTestCase<T> {

    private static final String TAG = InjectedActivityUnitTestCase.class.getName();

    public InjectedActivityUnitTestCase(Class<T> activityClass) {
        super(activityClass);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GuiceInitializer.initialize(this, new ActivityInitializationStrategy(this));
    }

    @Override
    public void setApplication(Application application) {
        if (application instanceof MockApplication) {
            boolean meetsGuiceRequirements = false;
            try {
                meetsGuiceRequirements = application.getPackageManager() != null;
                meetsGuiceRequirements = meetsGuiceRequirements && application.getPackageName() != null;
                meetsGuiceRequirements = meetsGuiceRequirements && application.getResources() != null;
            } catch (Exception ignored) {
                Log.w(TAG, "Overriding application passed to setApplication().  If you are using a custom instance of MockApplication, ensure that the following methods are implemented:\n" +
                        "\tpublic PackageManager getPackageManager()\n" +
                        "\tpublic String getPackageName()\n" +
                        "\tpublic Resources getResources()");
            }
            if (meetsGuiceRequirements) {
                super.setApplication(application);
            } else {
                super.setApplication(new MinimalApplication());
            }
        } else {
            super.setApplication(application);
        }
    }

    private static class MinimalApplication extends MockApplication {

        @Override
        public PackageManager getPackageManager() {
            PackageManager pm = mock(PackageManager.class);
            try {
                when(pm.getApplicationInfo(anyString(), anyInt())).thenReturn(mock(ApplicationInfo.class));
            } catch (PackageManager.NameNotFoundException e) {
                throw new AssertionError(e);
            }
            return pm;
        }

        @Override
        public String getPackageName() {
            return "test.app";
        }

        @Override
        public Resources getResources() {
            return mock(Resources.class);
        }
    }
}
