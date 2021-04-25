package org.meier.check.bean;

import java.util.ArrayList;
import java.util.List;

public class AnalysisResult {

    private List<RuleResult> results = new ArrayList<>();

    public void addResult(RuleResult result) {
        results.add(result);
    }

    public void setResults(List<RuleResult> results) {
        this.results = results;
    }

    public List<RuleResult> getResults() {
        return results;
    }

}
