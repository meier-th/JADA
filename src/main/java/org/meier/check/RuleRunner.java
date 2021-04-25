package org.meier.check;

import org.meier.export.Exporter;
import org.meier.model.ClassMeta;

import java.util.Collection;

public interface RuleRunner {

    void executeRules();

    void setExporter(Exporter exporter);

    void setData(Collection<ClassMeta> classes);

}
