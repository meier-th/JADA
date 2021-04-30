package org.meier.model;

import com.github.javaparser.ast.body.ConstructorDeclaration;

import java.util.List;

public class ConstructorMeta implements Meta {

    private final ClassMeta ownerClass;
    private final List<Modifier> modifiers;
    private final List<Parameter> parameters;
    private final ConstructorDeclaration contents;

    public ConstructorMeta(ClassMeta ownerClass, List<Modifier> modifiers, List<Parameter> parameters, ConstructorDeclaration contents) {
        this.ownerClass = ownerClass;
        this.modifiers = modifiers;
        this.parameters = parameters;
        this.contents = contents;
    }

    public ClassMeta getOwnerClass() {
        return ownerClass;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public ConstructorDeclaration getContents() {
        return contents;
    }

    @Override
    public boolean isStatic() {
        return false;
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

    @Override
    public int getStartLine() {
        return contents.getBegin().get().line;
    }
}
