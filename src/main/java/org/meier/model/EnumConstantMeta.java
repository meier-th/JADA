package org.meier.model;

import com.github.javaparser.ast.body.EnumConstantDeclaration;

import java.util.List;

public class EnumConstantMeta extends EnumMeta {

    private final EnumConstantDeclaration contents;

    public EnumConstantDeclaration getContents() {
        return contents;
    }

    public EnumConstantMeta(String fullName, List<Modifier> modifiers, EnumConstantDeclaration contents, boolean nested) {
        super(fullName, modifiers, nested);
        this.contents = contents;
    }

}
