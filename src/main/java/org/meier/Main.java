package org.meier;

import org.meier.check.visitor.FieldVisitor;
import org.meier.check.visitor.MethodVisitor;
import org.meier.loader.FSProjectLoader;
import org.meier.model.ClassMeta;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            FSProjectLoader loader = new FSProjectLoader();
            List<ClassMeta> classes = loader.loadProject("/home/thom/IdeaProjects/communicator/src/main/java",
                    "/home/thom/.gradle/caches/modules-2/files-2.1");
            classes.forEach(classMeta -> {
                classMeta.getClassNode().accept(new FieldVisitor(), classMeta);
                classMeta.getClassNode().accept(new MethodVisitor(), classMeta);
            });
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

}
