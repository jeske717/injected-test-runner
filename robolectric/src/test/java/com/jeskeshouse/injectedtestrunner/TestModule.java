package test.java.com.jeskeshouse.injectedtestrunner;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Named;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    @Named("objectFromModule")
    public InjectableThing objectWithName() {
        return new InjectableThing();
    }
}
