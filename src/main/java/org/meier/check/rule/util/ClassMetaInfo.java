package org.meier.check.rule.util;

import org.meier.model.ClassMeta;

import java.util.HashSet;
import java.util.Set;

public final class ClassMetaInfo {

    public static Set<ClassMeta> getAllDescendants(ClassMeta cls) {
        Set<ClassMeta> descendants = new HashSet<>();
        descendants.addAll(cls.getProjectExtendedBy());
        descendants.addAll(cls.getProjectImplementedBy());
        cls.getProjectExtendedBy().forEach(ext -> descendants.addAll(getAllDescendants(ext)));
        cls.getProjectImplementedBy().forEach(ext -> descendants.addAll(getAllDescendants(ext)));
        return descendants;
    }

    public static Set<ClassMeta> getAllAncestors(ClassMeta cls) {
        Set<ClassMeta> ancestors = new HashSet<>();
        ancestors.addAll(cls.getProjectExtendedClasses());
        ancestors.addAll(cls.getProjectImplementedInterfaces());
        cls.getProjectExtendedClasses().forEach(parent -> ancestors.addAll(getAllAncestors(parent)));
        cls.getProjectImplementedInterfaces().forEach(parent -> ancestors.addAll(getAllAncestors(parent)));
        return ancestors;
    }

    public static boolean hasBuilderSetter(ClassMeta cls) {
        return cls.getMethods().stream().anyMatch(meth -> {
            boolean returnsItself = meth.getFullQualifiedReturnType().equals(cls.getFullName());
            if (!returnsItself)
                return false;
            return cls.getMethods().stream().anyMatch(field -> meth.getShortName().equalsIgnoreCase("set"+field.getShortName()));
        });
    }

}
