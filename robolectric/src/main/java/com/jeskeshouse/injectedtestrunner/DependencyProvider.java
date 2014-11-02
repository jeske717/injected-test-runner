package main.java.com.jeskeshouse.injectedtestrunner;

import java.util.List;

interface DependencyProvider {

    List<Dependency> getDependencies(Object test) throws Exception;

}
