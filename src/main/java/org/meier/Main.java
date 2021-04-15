package org.meier;

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
            System.out.println("done");
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

}
