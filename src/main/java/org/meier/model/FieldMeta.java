package org.meier.model;

import java.util.Set;

public class FieldMeta implements Meta {

    private final String name;
    private final Class<?> type;
    private final Set<Modifier> modifiers;
    private ClassWrapper ownerClass;

    public FieldMeta(String name, Class<?> type, Set<Modifier> modifiers) {
        this.name = name;
        this.type = type;
        this.modifiers = modifiers;
    }

    public void setOwnerClass(ClassWrapper owner) {
        this.ownerClass = owner;
    }

    public void addModifier(Modifier mod) {
        this.modifiers.add(mod);
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
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
