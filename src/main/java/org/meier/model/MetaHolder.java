package org.meier.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class MetaHolder {

    private static final Map<String, ClassMeta> projectClasses = new LinkedHashMap<>();

    public static void addClass(ClassMeta cls) {
        projectClasses.put(cls.getFullName(), cls);
    }

    public static ClassMeta getClass(String fullName) {
        return projectClasses.get(fullName);
    }

    public static void forEach(Consumer<ClassMeta> action) {
        projectClasses.values().forEach(action);
    }

    public static Map<String, ClassMeta> getClasses() {
        return projectClasses;
    }

}
