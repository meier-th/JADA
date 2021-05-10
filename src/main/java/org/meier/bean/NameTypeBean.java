package org.meier.bean;

import com.github.javaparser.resolution.types.ResolvedType;

public class NameTypeBean {

    private final String name;
    private final String fullClassName;
    private final String className;
    private ResolvedType type;
    private boolean isLoopVariable = false;

    public NameTypeBean(String name, String fullClassName, String className) {
        this.name = name;
        this.fullClassName = fullClassName;
        this.className = className;
    }

    public boolean isNotLoopVariable() {
        return !this.isLoopVariable;
    }

    public void setLoopVariable(boolean loopVariable) {
        this.isLoopVariable = loopVariable;
    }

    public void setType(ResolvedType type) {
        this.type = type;
    }

    public ResolvedType getType() {
        return type;
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
