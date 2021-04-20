package org.meier;

import org.meier.loader.FSProjectLoader;
import org.meier.model.ClassMeta;
import org.meier.model.MetaHolder;

import java.io.IOException;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        try {
            FSProjectLoader loader = new FSProjectLoader();
            loader.loadProject("/home/thom/IdeaProjects/communicator/src/main/java",
                    "/home/thom/.gradle/caches/modules-2/files-2.1");
            Map<String, ClassMeta> classes = MetaHolder.getClasses();
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

}
