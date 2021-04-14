package org.meier.bean;

public class CalledMethodBean {

    private final String className;
    private final String fullMethodName;

    public CalledMethodBean(String className, String fullMethodName) {
        this.className = className;
        this.fullMethodName = fullMethodName;
    }

    public String getClassName() {
        return className;
    }

    public String getFullMethodName() {
        return fullMethodName;
    }
}
