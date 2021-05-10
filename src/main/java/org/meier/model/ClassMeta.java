package org.meier.model;

import java.util.*;
import java.util.stream.Collectors;

public class ClassMeta implements Meta {

    private final String fullName;
    private Set<MethodMeta> methods = new HashSet<>();
    private Map<String, FieldMeta> fields = new HashMap<>();
    private final List<ClassMeta> innerClasses = new ArrayList<>();
    private final List<Modifier> modifiers = new ArrayList<>();
    private boolean nested = false;
    private final List<ClassMeta> projectExtendedClasses = new ArrayList<>();
    private final List<ClassMeta> projectImplementedInterfaces = new ArrayList<>();
    private final List<ClassMeta> projectExtendedBy = new ArrayList<>();
    private final List<ClassMeta> projectImplementedBy = new ArrayList<>();
    private Set<String> extendedClasses = new HashSet<>();
    private Set<String> implementedInterfaces = new HashSet<>();
    private final List<CodeBlockMeta> codeBlocks = new ArrayList<>();
    private int startLine;
    private List<ConstructorMeta> constructors = new ArrayList<>();
    private final boolean isInterface;

    public ClassMeta(String fullName, List<Modifier> modifiers, boolean isInterface) {
        this.fullName = fullName;
        this.modifiers.addAll(modifiers);
        this.isInterface = isInterface;
    }

    public ClassMeta(String fullName, List<Modifier> modifiers, boolean nested, boolean isInterface) {
        this(fullName, modifiers, isInterface);
        this.nested = nested;
    }

    public void resolveMethodCalls() {
        this.methods.forEach(MethodMeta::resolveCalledMethods);
        this.codeBlocks.forEach(CodeBlockMeta::resolveCalledMethods);
        this.getInnerClasses().forEach(ClassMeta::resolveMethodCalls);
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void addExtendedBy(ClassMeta cls) {
        this.projectExtendedBy.add(cls);
    }

    public void addConstructor(ConstructorMeta constructor) {
        constructors.add(constructor);
    }

    public List<ConstructorMeta> getConstructors() {
        return constructors;
    }

    public void setConstructors(List<ConstructorMeta> constructors) {
        this.constructors = constructors;
    }

    public void addImplementedBy(ClassMeta cls) {
        this.projectImplementedBy.add(cls);
    }

    public List<ClassMeta> getProjectExtendedBy() {
        return projectExtendedBy;
    }

    public List<ClassMeta> getProjectImplementedBy() {
        return projectImplementedBy;
    }

    public List<CodeBlockMeta> getCodeBlocks() {
        return codeBlocks;
    }

    public void addCodeBlock(CodeBlockMeta codeBlock) {
        this.codeBlocks.add(codeBlock);
    }

    public boolean isNested() {
        return this.nested;
    }

    @Override
    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int line) {
        this.startLine = line;
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

    public void setExtendedClasses(List<String> extendedClasses) {
        this.extendedClasses.addAll(extendedClasses);
    }

    public void setImplementedInterfaces(List<String> implementedInterfaces) {
        this.implementedInterfaces.addAll(implementedInterfaces);
    }

    public void resolveExtendedAndImplemented() {
        if (extendedClasses != null)
            this.projectExtendedClasses.addAll(this.extendedClasses.stream().map(MetaHolder::getClass).filter(Objects::nonNull).collect(Collectors.toList()));
        if (implementedInterfaces != null)
            this.projectImplementedInterfaces.addAll(this.implementedInterfaces.stream().map(MetaHolder::getClass).filter(Objects::nonNull).collect(Collectors.toList()));
        this.extendedClasses = null;
        this.implementedInterfaces = null;
        this.projectExtendedClasses.forEach(cls -> cls.addExtendedBy(this));
        this.projectImplementedInterfaces.forEach(cls -> cls.addImplementedBy(this));
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public List<ClassMeta> getProjectExtendedClasses() {
        return projectExtendedClasses;
    }

    public List<ClassMeta> getProjectImplementedInterfaces() {
        return projectImplementedInterfaces;
    }

    public String getFullName() {
        return fullName;
    }

    public void addMethod(MethodMeta method) {
        methods.add(method);
    }

    public void addField(FieldMeta field) {
        fields.put(field.getName(), field);
    }

    public Set<MethodMeta> getMethods() {
        return methods;
    }

    public void setMethods(Set<MethodMeta> methods) {
        this.methods = methods;
    }

    public FieldMeta getField(String fieldName) {
        return this.fields.get(fieldName);
    }

    public Map<String, FieldMeta> getFields() {
        return fields;
    }

    public void setFields(Map<String, FieldMeta> fields) {
        this.fields = fields;
    }

    public List<ClassMeta> getInnerClasses() {
        return innerClasses;
    }
}
