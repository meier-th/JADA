package org.meier.model;

public interface Meta {

    boolean isStatic();
    Modifier accessModifier();
    boolean isSynchronised();
    int getStartLine();

}
