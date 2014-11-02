package com.jeskeshouse.injectedtestrunner;

import java.util.List;

interface DependencyProvider {

    List<Dependency> getDependencies(Object test) throws Exception;

}
