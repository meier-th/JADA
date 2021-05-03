package org.meier.check.rule;

import org.meier.check.bean.RuleResult;
import org.meier.model.ClassMeta;

import java.util.Collection;

public interface CheckRule {

    RuleResult executeRule(Collection<ClassMeta> classes);

}
