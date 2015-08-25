package com.jeskeshouse.injectedtestrunner;

import android.content.Intent;

import com.jeskeshouse.injectedtestrunner.components.TestSuperclassActivity;
import com.jeskeshouse.injectedtestrunner.injectables.AnotherInjectableThing;
import com.jeskeshouse.injectedtestrunner.injectables.InjectableThing;

import org.mockito.Mock;

import javax.inject.Named;

public class TestSubclass extends TestSuperclass {

    @Mock
    @Named("injectableThingFromSubclass")
    private InjectableThing injectableThingFromSubclass;

    @Mock
    private AnotherInjectableThing anotherInjectableThing;

    public TestSubclass() {
        super(TestSuperclassActivity.class);
    }

    public void testMocksFromSuperclassAreBound() throws Exception {

        TestSuperclassActivity testObject = startActivity(new Intent(getInstrumentation().getTargetContext(), TestSuperclassActivity.class), null, null);

        assertSame(injectableThingFromSuperclass, testObject.injectableThingFromSuperclass);
        assertSame(injectableThingFromSubclass, testObject.injectableThingFromSubclass);
    }

    public void testMocksCanBeOverriddenInSubclasses() throws Exception {

        TestSuperclassActivity testObject = startActivity(new Intent(getInstrumentation().getTargetContext(), TestSuperclassActivity.class), null, null);

        assertSame(anotherInjectableThing, testObject.anotherInjectableThing);
    }
}
