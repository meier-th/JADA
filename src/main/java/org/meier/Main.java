package org.meier;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import org.meier.check.FullReportUnorderedRuleRunner;
import org.meier.check.UnorderedRuleRunner;
import org.meier.check.rule.*;
import org.meier.export.CliExporter;
import org.meier.export.Exporter;
import org.meier.loader.FSProjectLoader;
import org.meier.model.ClassMeta;
import org.meier.model.MetaHolder;

import java.io.IOException;
import java.util.Collection;

public class Main {

    public static void main(String[] args) {
        try {
            FSProjectLoader loader = new FSProjectLoader();
            loader.loadProject("/home/thom/IdeaProjects/communicator/src/main/java",
                    "/home/thom/.gradle/caches/modules-2/files-2.1");
            Exporter cliExporter = new CliExporter();
            UnorderedRuleRunner runner = new FullReportUnorderedRuleRunner();
            Collection<ClassMeta> classes = MetaHolder.getClasses().values();
            runner.setData(MetaHolder.getClasses().values());
            runner.setExporter(cliExporter);
            runner.addRule(new NonDescriptiveNamesRule());
            runner.addRule(new SingleResponsibilityRule());
            runner.addRule(new DependencyInversionRule());
            runner.addRule(new EncapsulationRule());
            runner.addRule(new FactoryMethodRule());
            runner.executeRules();
        } catch (IOException error) {
            System.out.println(error.getMessage());
        }
    }

}
