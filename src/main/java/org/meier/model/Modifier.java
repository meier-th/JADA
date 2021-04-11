package org.meier.model;

import com.github.javaparser.ast.Modifier.Keyword;

public enum Modifier {
    ABSTRACT,
    PRIVATE,
    PUBLIC,
    PROTECTED,
    DEFAULT_ACCESS,
    STATIC,
    SYNCHRONISED,
    FINAL,
    VOLATILE,
    TRANSIENT;

    public static Modifier toModifier(Keyword key) {
        return switch (key) {
            case ABSTRACT:
                yield Modifier.ABSTRACT;
            case PRIVATE:
                yield Modifier.PRIVATE;
            case PUBLIC:
                yield Modifier.PUBLIC;
            case FINAL:
                yield Modifier.FINAL;
            case STATIC:
                yield Modifier.STATIC;
            case DEFAULT:
                yield Modifier.DEFAULT_ACCESS;
            case VOLATILE:
                yield Modifier.VOLATILE;
            case PROTECTED:
                yield Modifier.PROTECTED;
            case SYNCHRONIZED:
                yield Modifier.SYNCHRONISED;
            case TRANSIENT:
                yield Modifier.TRANSIENT;
            default: yield null;
        };
    }
}
