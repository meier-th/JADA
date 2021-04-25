package org.meier.build.util;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.types.ResolvedType;

import java.util.Optional;
import java.util.stream.Collectors;

public class TypeResolver {

    public static String getQualifiedName(Type type) {
        return getQualifiedNameFromResolved(type, type.resolve());
    }

    private static String getQualifiedNameFromResolved(Type type, ResolvedType resType) {
        if (resType.isArray())
            return getArrayType(type, resType);
        String genericPart = getGenericType(type);
        return getNonArrayTypeName(resType)+genericPart;
    }

    private static String getArrayType(Type type, ResolvedType arrayType) {
        return getQualifiedNameFromResolved(type, arrayType.asArrayType().getComponentType()) + "[]";
    }

    private static String getNonArrayTypeName(ResolvedType type) {
        if (type.isWildcard())
            return type.asWildcard().describe();
        if (type.isPrimitive())
            return type.asPrimitive().describe();
        if (type.isReference())
            return type.asReferenceType().getQualifiedName();
        if (type.isVoid())
            return "void";
        return type.describe();
    }

    private static String getGenericType(Type type) {
        Optional<NodeList<Type>> genericTypes = Optional.empty();
        if (type.isClassOrInterfaceType())
            genericTypes = type.asClassOrInterfaceType().getTypeArguments();
        String generics = genericTypes.orElse(NodeList.nodeList())
                .stream()
                .map(TypeResolver::getQualifiedName)
                .collect(Collectors.joining(", "));
        if (!generics.isEmpty())
            return "<"+generics+">";
        return "";
    }

}
