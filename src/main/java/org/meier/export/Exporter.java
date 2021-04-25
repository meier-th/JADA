package org.meier.export;

import org.meier.check.bean.AnalysisResult;

public interface Exporter {

    void exportResults(AnalysisResult result);

}
