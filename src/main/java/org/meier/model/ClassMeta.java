package org.meier.model;

import com.github.javaparser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.List;

public class ClassMeta {

    private final CompilationUnit classNode;
    private List<MethodMeta> methods = new ArrayList<>();
    private List<FieldMeta> fields = new ArrayList<>();
    private List<ClassMeta> innerClasses = new ArrayList<>();

    public ClassMeta(CompilationUnit classNode) {
        this.classNode = classNode;
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
