package org.meier.model;

import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;

import java.util.List;

public class Parameter {

    private String name;
    private String typeName;
    private List<TypeParameter> genericParams;
    private Type type;

    public Parameter(String name, String typeName, List<TypeParameter> genericParams, Type type) {
        this.name = name;
        this.typeName = typeName;
        this.genericParams = genericParams;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<TypeParameter> getGenericParams() {
        return genericParams;
    }

    public void setGenericParams(List<TypeParameter> genericParams) {
        this.genericParams = genericParams;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }


}
