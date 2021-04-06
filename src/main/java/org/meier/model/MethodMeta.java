package org.meier.model;

import java.util.List;
import java.util.Set;

public class MethodMeta implements Meta {

    private final String name;
    private final Set<Modifier> modifiers;
    private final List<Class<?>> parameterTypes;
    private final List<FieldMeta> accessedFields;
    private final List<MethodMeta> calledMethods;
    private ClassWrapper ownerClass;

    public MethodMeta(String name, Set<Modifier> modifiers, List<Class<?>> parameterTypes, List<FieldMeta> accessedFields, List<MethodMeta> calledMethods) {
        this.name = name;
        this.modifiers = modifiers;
        this.parameterTypes = parameterTypes;
        this.accessedFields = accessedFields;
        this.calledMethods = calledMethods;
    }

    public void setOwnerClass(ClassWrapper cls) {
        this.ownerClass = cls;
    }

    public String getName() {
        return name;
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    public List<Class<?>> getParameterTypes() {
        return parameterTypes;
    }

    public List<FieldMeta> getAccessedFields() {
        return accessedFields;
    }

    public List<MethodMeta> getCalledMethods() {
        return calledMethods;
    }

    public ClassWrapper getOwnerClass() {
        return ownerClass;
    }

    @Override
    public boolean isStatic() {
        return modifiers.contains(Modifier.STATIC);
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
        return modifiers.contains(Modifier.SYNCHRONISED);
    }
}
