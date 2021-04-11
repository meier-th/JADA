package org.meier.bean;

public class NameTypeBean {

    private final String name;
    private final String fullClassName;

    public NameTypeBean(String name, String fullClassName) {
        this.name = name;
        this.fullClassName = fullClassName;
    }

    public String getName() {
        return name;
    }

    public String getFullClassName() {
        return fullClassName;
    }
}
