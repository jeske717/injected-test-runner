# Injected Test Runner
This project's goal is to use Mockito mocks to satisfy RoboGuice dependencies.  There are 2 flavors of injected test projects depending on your needs.  The "injected-test-runner" artifact from this repository uses Robolectric as the test runner, while the "injected-test-android" uses the native testing framework on Android as the test runner.  Since 2.0.0, RoboGuice version 3 is supported, the 1.x series supports RoboGuice 2.0 and does not contain the "injected-test-andoid" module.

## Robolectric Support (injected-test-runner)
This module allows Robolectric tests to automatically inject mocks created via Mockito into RoboGuice-aware classes under test.
It binds the mock in the following test:

    @RunWith(InjectedTestRunner.class)
    public class SomeActivityTest {
        @Mock
        private InjectableThing injectableThing;
    }
    
To the dependency in the following activity:

    public class SomeActivity extends RoboActivity {
        @Inject
        private InjectableThing injectableThing; // this will be the mock created by Mockito during the unit test run
    }
    
It also supports the use of binding annotations on mocks to satisfy the injection:

    @RunWith(InjectedTestRunner.class)
    public class SomeActivityTest {
        @Mock
        @Named("thing1")
        private InjectableThing injectableThing1;
        @Mock
        @Named("thing2")
        private InjectableThing injectableThing2;
    }
    
    public class SomeActivity extends RoboActivity {
        @Inject
        @Named("thing1")
        private InjectableThing injectableThing1;
        @Mock
        @Named("thing2")
        private InjectableThing injectableThing2;
    }
    
In addition, if you need RoboGuice to load an actual Module during the unit test run, add the @RequiredModules annotation:

    @RunWith(InjectedTestRunner.class)
    @RequiredModules(SomeProductionModule.class)
    public class SomeActivityTest {
    }
    
    public class SomeProductionModule extends AbstractModule {
        @Override
        protected void configure() {
        }
        
        @Provides
        public InjectableThing injectableThing() {
            return new InjectableThing();
        }
    }
    
    public class SomeActivity extends RoboActivity {
        @Inject
        private InjectableThing injectableThing; // this will come from the SomeProductionModule during the test run
    }
    
For objects that need to be configured in some way, you can use @Provides in the test case:

    @RunWith(InjectedTestRunner.class)
    public class SomeActivityTest {
        @Provides
        @Named("someConfiguredThing")
        public InjectableThing someConfiguredThing() {
            return new InjectableThing();
        }
    }
    
    public class SomeActivity extends RoboActivity {
        @Inject
        @Named("someConfiguredThing")
        private InjectableThing injectableThing1;
    }
    
### Installation
To use the test runner, simply add the following to your build:

    repositories {
        jcenter()
    }
    
    dependencies {
        <your robolectric configuration> 'com.jeskeshouse:injected-test-runner:2.0.3'
    }
    
Once the dependency resolution is complete, start annotating tests with @RunWith(InjectedTestRunner.class)!

## Native Android Support (injected-test-android)
This module allows Android tests to automatically inject mocks created via Mockito into RoboGuice-aware classes under test.
It binds the mock in the following test:

    public class SomeActivityTest extends InjectedActivityUnitTestCase<SomeActivity> {
        @Mock
        private InjectableThing injectableThing;
    }

To the dependency in the following activity:

    public class SomeActivity extends RoboActivity {
        @Inject
        private InjectableThing injectableThing; // this will be the mock created by Mockito during the unit test run
    }

It also supports the use of binding annotations on mocks to satisfy the injection:

    public class SomeActivityTest extends InjectedActivityUnitTestCase<SomeActivity> {
        @Mock
        @Named("thing1")
        private InjectableThing injectableThing1;
        @Mock
        @Named("thing2")
        private InjectableThing injectableThing2;
    }

    public class SomeActivity extends RoboActivity {
        @Inject
        @Named("thing1")
        private InjectableThing injectableThing1;
        @Mock
        @Named("thing2")
        private InjectableThing injectableThing2;
    }

In addition, if you need RoboGuice to load an actual Module during the unit test run, add the @RequiredModules annotation:

    @RequiredModules(SomeProductionModule.class)
    public class SomeActivityTest extends InjectedActivityUnitTestCase<SomeActivity> {
    }

    public class SomeProductionModule extends AbstractModule {
        @Override
        protected void configure() {
        }

        @Provides
        public InjectableThing injectableThing() {
            return new InjectableThing();
        }
    }

    public class SomeActivity extends RoboActivity {
        @Inject
        private InjectableThing injectableThing; // this will come from the SomeProductionModule during the test run
    }

For objects that need to be configured in some way, you can use @Provides in the test case:

    public class SomeActivityTest extends InjectedActivityUnitTestCase<SomeActivity> {
        @Provides
        @Named("someConfiguredThing")
        public InjectableThing someConfiguredThing() {
            return new InjectableThing();
        }
    }

    public class SomeActivity extends RoboActivity {
        @Inject
        @Named("someConfiguredThing")
        private InjectableThing injectableThing1;
    }

### Installation
To use the test runner, simply add the following to your build:

    repositories {
        jcenter()
    }

    dependencies {
        androidTest 'com.jeskeshouse:injected-test-android:2.0.3'
    }

Once the dependency resolution is complete, start extending the appropriate InjectedTestCase!
This module provides a base test case for activities, services and content providers. (InjectedActivityUnitTestCase, InjectedServiceTestCase and InjectedContentProviderTestCase)

## Important Note
Methods annotated with @Provides in the test class currently can NOT have any method parameters.  If you need this capability, consider creating a Module for your test and reference it with the @RequiredModules annotation.