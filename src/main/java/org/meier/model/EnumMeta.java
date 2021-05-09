package org.meier.model;

import java.util.ArrayList;
import java.util.List;

public class EnumMeta extends ClassMeta {

    private final List<EnumConstantMeta> enumConstants = new ArrayList<>();

    public EnumMeta(String fullName, List<Modifier> modifiers) {
        super(fullName, modifiers, false);
    }

    public EnumMeta(String fullName, List<Modifier> modifiers, boolean nested) {
        super(fullName, modifiers, nested);
    }

    public List<EnumConstantMeta> getEnumConstants() {
        return enumConstants;
    }

    public void addEnumConstant(EnumConstantMeta constant) {
        this.enumConstants.add(constant);
    }

}
