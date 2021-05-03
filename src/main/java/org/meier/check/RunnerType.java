package org.meier.check;

import org.meier.check.runner.FullReportUnorderedRuleRunner;

public enum RunnerType {
    FULL_REPORT_UNORDERED {
        @Override
        public Class<FullReportUnorderedRuleRunner> type() {
            return FullReportUnorderedRuleRunner.class;
        }
    };

    public abstract Class<? extends RuleRunner> type();
}
