package com.jeskeshouse.injectedtestrunner.dagger;

import java.util.ArrayList;
import java.util.List;

public class PendingModule {
    private final String className;
    private final String packageName;
    private final List<MockField> mocks = new ArrayList<MockField>();

    public PendingModule(String className, String packageName) {
        this.className = className;
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public List<MockField> getMocks() {
        return mocks;
    }

    public String getPackageName() {
        return packageName;
    }

    public void addMock(MockField mock) {
        mocks.add(mock);
    }

    @Override
    public String toString() {
        return "PendingModule{" +
                "className='" + className + '\'' +
                ", packageName='" + packageName + '\'' +
                ", mocks=" + mocks +
                '}';
    }
}