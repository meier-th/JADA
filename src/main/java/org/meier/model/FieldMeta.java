package org.meier.model;

import java.util.Set;
import java.util.stream.Collectors;

public class FieldMeta implements Meta {

    private final String name;
    private final String fullClassName;
    private final Set<Modifier> modifiers;
    private ClassMeta ownerClass;

    public FieldMeta(String name, String fullClassName, Set<Modifier> modifiers) {
        this.name = name;
        this.fullClassName = fullClassName;
        this.modifiers = modifiers;
    }

    public void setOwnerClass(ClassMeta owner) {
        this.ownerClass = owner;
    }

    public void addModifier(Modifier mod) {
        this.modifiers.add(mod);
    }

    public String getName() {
        return name;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
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
        return "name: " + name + "\n" +
                "type: " + fullClassName + "\n" +
                "modifiers: " + modifiers.stream().map(Modifier::toString).collect(Collectors.joining());
    }
}
