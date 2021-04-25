package org.meier.export;

import org.meier.check.bean.AnalysisResult;

public class CliExporter implements Exporter {

    @Override
    public void exportResults(AnalysisResult result) {
        result.getResults().forEach(ruleResult -> {
            System.out.printf("%s:\n", ruleResult.getRuleName());
            if (ruleResult.getFoundDefects().isEmpty()) {
                System.out.println("No defects found");
            }
            else {
                ruleResult.getFoundDefects().forEach(defect -> {
                    System.out.printf("%s:\n", defect.getDefectName());
                    if (defect.getClassName() != null)
                        System.out.printf("in %s", defect.getClassName());
                    if (defect.getLineNumber() != null)
                        System.out.printf("; at %d:\n", defect.getLineNumber());
                    else
                        System.out.println();
                    if (defect.getMethodName() != null)
                        System.out.printf("in %s:\n", defect.getMethodName());
                    System.out.println(defect.getDefectDescription());
                    System.out.println();
                });
            }
            System.out.print("\n\n\n");
        });
    }
}
