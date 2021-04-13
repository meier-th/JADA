package org.meier.model;

import java.util.HashMap;
import java.util.Map;

public final class MetaHolder {

    private static final Map<String, ClassMeta> projectClasses = new HashMap<>();

    public static void addClass(ClassMeta cls) {
        projectClasses.put(cls.getFullName(), cls);
    }

    public static ClassMeta getClass(String fullName) {
        return projectClasses.get(fullName);
    }

}
