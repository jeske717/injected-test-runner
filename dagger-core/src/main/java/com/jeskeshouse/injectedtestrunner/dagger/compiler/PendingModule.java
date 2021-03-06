package com.jeskeshouse.injectedtestrunner.dagger.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PendingModule {
    private final String className;
    private final String packageName;
    private final String testClassName;
    private final List<Object> injectTargets;
    private final List<MockField> mocks = new ArrayList<MockField>();

    public PendingModule(String className, String packageName, String testClassName, List<Object> injectTargets) {
        this.className = className;
        this.packageName = packageName;
        this.testClassName = testClassName;
        this.injectTargets = injectTargets;
    }

    public String getClassName() {
        return className;
    }

    public List<MockField> getMocks() {
        Collections.sort(mocks, new SimpleMockFieldComparator());
        return mocks;
    }

    public String getPackageName() {
        return packageName;
    }

    public void addMock(MockField mock) {
        mocks.add(mock);
    }

    public String getTestSubject() {
        StringBuilder sb = new StringBuilder();
        for (Object injectTarget : injectTargets) {
            sb.append(injectTarget).append(",");
        }
        return sb.deleteCharAt(sb.lastIndexOf(",")).toString();
    }

    @Override
    public String toString() {
        return "PendingModule{" +
                "className='" + className + '\'' +
                ", packageName='" + packageName + '\'' +
                ", mocks=" + mocks +
                '}';
    }

    private static class SimpleMockFieldComparator implements Comparator<MockField> {

        @Override
        public int compare(MockField o1, MockField o2) {
            return o1.getQualifiedName().compareTo(o2.getQualifiedName());
        }
    }
}