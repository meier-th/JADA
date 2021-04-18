package org.meier.model;

import java.util.ArrayList;
import java.util.List;

public class ClassMeta implements Meta {

    private final String fullName;
    private List<MethodMeta> methods = new ArrayList<>();
    private List<FieldMeta> fields = new ArrayList<>();
    private final List<ClassMeta> innerClasses = new ArrayList<>();
    private final List<Modifier> modifiers = new ArrayList<>();
    private boolean nested = false;

    public ClassMeta(String fullName, List<Modifier> modifiers) {
        this.fullName = fullName;
        this.modifiers.addAll(modifiers);
    }

    public ClassMeta(String fullName, List<Modifier> modifiers, boolean nested) {
        this(fullName, modifiers);
        this.nested = nested;
    }

    public void resolveMethodCalls() {
        this.methods.forEach(MethodMeta::resolveCalledMethods);
        this.getInnerClasses().forEach(ClassMeta::resolveMethodCalls);
    }

    public boolean isNested() {
        return this.nested;
    }

    @Override
    public boolean isStatic() {
        return this.modifiers.contains(Modifier.STATIC);
    }

    @Override
    public Modifier accessModifier() {
        return modifiers.contains(Modifier.PUBLIC) ? Modifier.PUBLIC :
                modifiers.contains(Modifier.PROTECTED) ? Modifier.PROTECTED :
                        modifiers.contains(Modifier.PRIVATE) ? Modifier.PRIVATE :
                                Modifier.DEFAULT_ACCESS;
    }

    @Override
    public boolean isSynchronised() {
        return false;
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
