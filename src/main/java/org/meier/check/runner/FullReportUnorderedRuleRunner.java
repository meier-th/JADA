package org.meier.check.runner;

import org.meier.check.RuleRunner;
import org.meier.check.bean.AnalysisResult;
import org.meier.check.rule.CheckRule;
import org.meier.export.Exporter;
import org.meier.model.ClassMeta;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FullReportUnorderedRuleRunner implements RuleRunner {

    private List<CheckRule> rules;
    private Exporter exporter;
    private Collection<ClassMeta> classes;

    @Override
    public void setRules(List<CheckRule> rules) {
        this.rules = rules;
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
