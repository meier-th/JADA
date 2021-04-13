package org.meier.model;

import com.github.javaparser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.List;

public class ClassMeta {

    private final CompilationUnit classNode;
    private final String fullName;
    private List<MethodMeta> methods = new ArrayList<>();
    private List<FieldMeta> fields = new ArrayList<>();
    private List<ClassMeta> innerClasses = new ArrayList<>();

    public ClassMeta(CompilationUnit classNode, String fullName) {
        this.classNode = classNode;
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public CompilationUnit getClassNode() {
        return classNode;
    }

    public List<MethodMeta> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodMeta> methods) {
        this.methods = methods;
    }

    public List<FieldMeta> getFields() {
        return fields;
    }

    public void setFields(List<FieldMeta> fields) {
        this.fields = fields;
    }

    public List<ClassMeta> getInnerClasses() {
        return innerClasses;
    }
}
