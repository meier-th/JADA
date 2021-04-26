package org.meier.model;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.meier.bean.CalledMethodBean;
import org.meier.bean.NameTypeBean;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodMeta implements Meta {

    private final String name;
    private final String fullQualifiedReturnType;
    private final Set<Modifier> modifiers;
    private final List<NameTypeBean> parameters;
    private final List<FieldMeta> accessedFields;
    private List<MethodMeta> calledMethods;
    private final List<CalledMethodBean> calledMethodsNames;
    private ClassMeta ownerClass;
    private final MethodDeclaration content;

    public MethodMeta(MethodDeclaration content, String name, Set<Modifier> modifiers, List<NameTypeBean> parameters, List<FieldMeta> accessedFields, List<CalledMethodBean> calledMethods, String fullQualifiedReturnType, ClassMeta ownerClass) {
        this.name = name;
        this.modifiers = modifiers;
        this.parameters = parameters;
        this.accessedFields = accessedFields;
        this.calledMethodsNames = calledMethods;
        this.fullQualifiedReturnType = fullQualifiedReturnType;
        this.ownerClass = ownerClass;
        this.content = content;
    }

    public void resolveCalledMethods() {
        calledMethods = calledMethodsNames.stream()
                .map(meth -> {
                    ClassMeta ownerClass = MetaHolder.getClass(meth.getClassName());
                    if (ownerClass != null) {
                        return ownerClass.getMethods()
                                .stream()
                                .filter(mt -> mt.getName().equals(meth.getFullMethodName()))
                                .findFirst().orElse(null);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public int getStartLine() {
        return content.getBegin().get().line;
    }

    public String getShortName() {
        return name.substring(name.lastIndexOf(".")+1);
    }

    public List<CalledMethodBean> getCalledMethodsNames() {
        return calledMethodsNames;
    }

    public MethodDeclaration getContent() {
        return content;
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

    public void addCalledMethod(MethodMeta callee) {
        calledMethods.add(callee);
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

    public List<MethodMeta> getCalledMethods() {
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

    @Override
    public String toString() {
        return modifiers.stream().map(Modifier::toString).collect(Collectors.joining(" "))+" "+fullQualifiedReturnType+" "+name+
                "(" + buildParamsString() + ")";
    }
}
