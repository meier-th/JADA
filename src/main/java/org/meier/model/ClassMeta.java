package org.meier.model;

import com.github.javaparser.ast.CompilationUnit;

import java.util.ArrayList;
import java.util.List;

public class ClassMeta {

    private final CompilationUnit classNode = null;
    private final String fullName;
    private List<MethodMeta> methods = new ArrayList<>();
    private List<FieldMeta> fields = new ArrayList<>();
    private List<ClassMeta> innerClasses = new ArrayList<>();

    public ClassMeta(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void addMethod(MethodMeta method) {
        methods.add(method);
    }

    public void addField(FieldMeta field) {
        fields.add(field);
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
