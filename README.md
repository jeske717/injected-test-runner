injected-test-runner
====================
This purpose of this project is to glue together a handful of popular testing frameworks to facilitate easier test writing/maintenance on Android.

What does it do?
----------------
It allows Robolectric tests to automatically inject mocks created via Mockito into RoboGuice-aware classes under test.
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
    
In addition, if you need RoboGuice to load an actual Module during the unit test run, add the @RequiredModules annotation (slated for 1.2):

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
    
Installation
------------
To use the test runner, simply add the following to your build:

    repositories {
        jcenter()
    }
    
    dependencies {
        <your robolectric configuration> 'com.jeskeshouse:injected-test-runner:1.0'
    }
    
Once the dependency resolution is complete, start annotating tests with @RunWith(InjectedTestRunner.class)!