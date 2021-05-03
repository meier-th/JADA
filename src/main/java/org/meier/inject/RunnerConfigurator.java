package org.meier.inject;

import org.meier.Main;
import org.meier.check.RuleRunner;
import org.meier.check.rule.CheckRule;
import org.meier.export.Exporter;
import org.meier.inject.annotation.InjectRunner;
import org.meier.inject.annotation.Rule;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RunnerConfigurator {

    private Reflections ruleReflections;

    RunnerConfigurator() {
        setUpReflections();
    }

    public void configureRunner() throws Exception {
        Field runnerField = Stream.of(Main.class.getDeclaredFields()).filter(field -> field.isAnnotationPresent(InjectRunner.class)).findFirst().orElse(null);
        if (runnerField != null) {
            runnerField.setAccessible(true);
            Constructor constructor = runnerField.getAnnotation(InjectRunner.class).runnerType().type().getDeclaredConstructor();
            Constructor exporterConstructor = runnerField.getAnnotation(InjectRunner.class).exporterType().type().getDeclaredConstructor();
            constructor.setAccessible(true);
            exporterConstructor.setAccessible(true);

            RuleRunner runner = (RuleRunner) constructor.newInstance();
            Exporter exporter = (Exporter) exporterConstructor.newInstance();

            runner.setExporter(exporter);
            runner.setRules(loadAllRules());

            runnerField.set(null, runner);
        }
    }

    private void setUpReflections() {
        ruleReflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner(), new TypeAnnotationsScanner())
                .setUrls(ClasspathHelper.forPackage("org.meier.check.rule")));
    }

    private List<CheckRule> loadAllRules() {
        return ruleReflections.getTypesAnnotatedWith(Rule.class).stream()
                .map(ruleCls -> {
                    try {
                        Constructor ruleConstructor = ruleCls.getDeclaredConstructor();
                        ruleConstructor.setAccessible(true);
                        return (CheckRule)ruleConstructor.newInstance();
                    } catch (Exception error) {
                        throw new RuntimeException(error);
                    }
                }).collect(Collectors.toList());
    }

}
