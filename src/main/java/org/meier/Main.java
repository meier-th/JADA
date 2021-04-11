package org.meier;

import org.meier.check.visitor.FieldsVisitor;
import org.meier.loader.FSProjectLoader;
import org.meier.model.ClassWrapper;
import org.meier.model.FieldMeta;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        try {
            FSProjectLoader loader = new FSProjectLoader();
            List<ClassWrapper> classes = loader.loadProject("/home/thom/IdeaProjects/communicator/src/main/java",
                    "/home/thom/.gradle/caches/modules-2/files-2.1");
                    List<FieldMeta> fields = classes
                            .stream()
                            .flatMap(cl -> {List<FieldMeta> classFields = cl.getClassNode()
                                .accept(new FieldsVisitor(), null);
                                if (classFields == null)
                                    return Stream.empty();
                                else return classFields.stream();
                            })
                            .collect(Collectors.toList());
                    fields.forEach(System.out::println);
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

}
