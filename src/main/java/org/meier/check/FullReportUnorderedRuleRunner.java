package org.meier.check;

import org.meier.check.bean.AnalysisResult;
import org.meier.check.rule.Rule;
import org.meier.export.Exporter;
import org.meier.model.ClassMeta;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FullReportUnorderedRuleRunner implements UnorderedRuleRunner {

    private final List<Rule> rules = new LinkedList<>();
    private Exporter exporter;
    private Collection<ClassMeta> classes;

    @Override
    public void addRule(Rule rule) {
        rules.add(rule);
    }

    @Override
    public void setData(Collection<ClassMeta> classes) {
        this.classes = classes;
    }

    @Override
    public void setExporter(Exporter exporter) {
        this.exporter = exporter;
    }

    @Override
    public void executeRules() {
        if (exporter == null)
            throw new IllegalStateException("Exporter is not set!");
        AnalysisResult result = new AnalysisResult();
        result.setResults(rules.stream().map(rule -> rule.executeRule(classes)).collect(Collectors.toList()));
        exporter.exportResults(result);
    }
}
