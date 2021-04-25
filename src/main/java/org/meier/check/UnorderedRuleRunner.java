package org.meier.check;

import org.meier.check.rule.Rule;

public interface UnorderedRuleRunner extends RuleRunner {

    void addRule(Rule rule);

}
