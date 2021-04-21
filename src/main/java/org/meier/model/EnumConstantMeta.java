package org.meier.model;

import java.util.List;

public class EnumConstantMeta extends EnumMeta {


    public EnumConstantMeta(String fullName, List<Modifier> modifiers) {
        super(fullName, modifiers);
    }

    public EnumConstantMeta(String fullName, List<Modifier> modifiers, boolean nested) {
        super(fullName, modifiers, nested);
    }

}
