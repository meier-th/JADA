package org.meier.model;

import com.github.javaparser.ast.CompilationUnit;

import java.util.List;

public class ClassWrapper {

    private final CompilationUnit classNode;
    private List<MethodMeta> methods;
    private List<FieldMeta> fields;

    public ClassWrapper(CompilationUnit classNode) {
        this.classNode = classNode;
    }

    public CompilationUnit getClassNode() {
        return classNode;
    }
}
