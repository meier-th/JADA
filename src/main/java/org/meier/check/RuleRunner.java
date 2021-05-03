package org.meier.check;

import org.meier.check.rule.CheckRule;
import org.meier.export.Exporter;
import org.meier.model.ClassMeta;

import java.util.Collection;
import java.util.List;

public interface RuleRunner {

    void executeRules();

    void setRules(List<CheckRule> rule);

    void setExporter(Exporter exporter);

    void setData(Collection<ClassMeta> classes);

}
