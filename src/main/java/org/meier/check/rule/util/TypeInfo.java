package org.meier.check.rule.util;

import com.github.javaparser.ast.type.Type;
import com.github.javaparser.resolution.types.ResolvedType;

public class TypeInfo {

    public static boolean isDescendant(Type ancestor, Type descendant) {
        ResolvedType ancestorResolved = getResolvedType(ancestor);
        ResolvedType descendantResolved = getResolvedType(descendant);
        if (ancestorResolved == null && (descendantResolved == null || !descendantResolved.isVoid()))
            return true;
        else if (ancestorResolved == null)
            return false;
        if (descendantResolved == null)
            return false;
        if (descendantResolved.isVoid() && ancestorResolved.isVoid())
            return true;
        if (ancestorResolved.isVoid() && !descendantResolved.isVoid() ||
                !ancestorResolved.isVoid() && descendantResolved.isVoid())
            return false;
        return ancestorResolved.isAssignableBy(descendantResolved);
    }

    private static ResolvedType getResolvedType(Type type) {
        try {
            return type.resolve();
        } catch (UnsupportedOperationException error) {
            try {
                return type.asTypeParameter().resolve();
            } catch (IllegalStateException err) {
                return null;
            }
        }
    }

}
