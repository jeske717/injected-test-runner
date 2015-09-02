package com.jeskeshouse.injectedtestrunner.dagger;

import dagger.Module;
import dagger.Provides;

@Module(injects = RealActivity.class)
public class ProductionModule {

    @Provides
    public InjectableThing injectableThing() {
        return new InjectableThing();
    }

}
