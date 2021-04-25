package org.meier.check.bean;

import java.util.ArrayList;
import java.util.List;

public class RuleResult {

    private final String ruleName;
    private List<DefectCase> foundDefects;

    public RuleResult(String ruleName, List<DefectCase> defects) {
        this.ruleName = ruleName;
        this.foundDefects = defects;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void addDefectCase(DefectCase defCase) {
        foundDefects.add(defCase);
    }

    public List<DefectCase> getFoundDefects() {
        return foundDefects;
    }

}
