package org.meier;

import org.meier.check.RuleRunner;
import org.meier.inject.Application;
import org.meier.inject.annotation.InjectRunner;
import org.meier.loader.FSProjectLoader;
import org.meier.model.ClassMeta;
import org.meier.model.MetaHolder;

import java.io.IOException;
import java.util.Collection;

public class Main {

    @InjectRunner
    private static RuleRunner runner;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Provide project src directory and dependencies jar directory");
            System.out.println("Format: java -jar jarname.jar project_source jar_source");
        } else {
            try {
                Application.run();
                FSProjectLoader loader = new FSProjectLoader();
                loader.loadProject(args[0], args[1]);
                Collection<ClassMeta> classes = MetaHolder.getClasses().values();
                runner.setData(classes);
                runner.executeRules();
            } catch (IOException error) {
                System.out.println(error.getMessage());
            }
        }
    }

}
