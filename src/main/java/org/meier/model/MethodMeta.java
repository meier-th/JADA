package org.meier.model;

import org.meier.bean.NameTypeBean;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodMeta implements Meta {

    private final String name;
    private final String fullQualifiedReturnType;
    private final Set<Modifier> modifiers;
    private final List<NameTypeBean> parameters;
    private final List<FieldMeta> accessedFields;
    private final List<String> calledMethods;
    private ClassMeta ownerClass;

    public MethodMeta(String name, Set<Modifier> modifiers, List<NameTypeBean> parameters, List<FieldMeta> accessedFields, List<String> calledMethods, String fullQualifiedReturnType) {
        this.name = name;
        this.modifiers = modifiers;
        this.parameters = parameters;
        this.accessedFields = accessedFields;
        this.calledMethods = calledMethods;
        this.fullQualifiedReturnType = fullQualifiedReturnType;
    }

    public void setOwnerClass(ClassMeta cls) {
        this.ownerClass = cls;
    }

    public String getUniqueName() {
        return name+"("+buildParamsString()+")";
    }

    private String buildParamsString() {
        return this.parameters.stream().map(NameTypeBean::getFullClassName).collect(Collectors.joining(", "));
    }

    public String getName() {
        return name;
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    public String getFullQualifiedReturnType() {
        return fullQualifiedReturnType;
    }

    public List<NameTypeBean> getParameters() {
        return parameters;
    }

    public List<FieldMeta> getAccessedFields() {
        return accessedFields;
    }

    public List<String> getCalledMethods() {
        return calledMethods;
    }

    public ClassMeta getOwnerClass() {
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
