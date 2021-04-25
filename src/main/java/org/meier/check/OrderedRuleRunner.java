package org.meier.check;

import org.meier.check.rule.Rule;

public interface OrderedRuleRunner extends RuleRunner {

    void addRule(Rule rule, int priority);

}
