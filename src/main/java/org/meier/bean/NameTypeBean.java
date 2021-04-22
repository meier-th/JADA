package org.meier.bean;

public class NameTypeBean {

    private final String name;
    private final String fullClassName;
    private final String className;

    public NameTypeBean(String name, String fullClassName, String className) {
        this.name = name;
        this.fullClassName = fullClassName;
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public String getName() {
        return name;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getFullName() {
        return fullClassName + " " + name;
    }
}
