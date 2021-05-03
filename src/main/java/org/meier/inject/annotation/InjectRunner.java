package org.meier.inject.annotation;

import org.meier.check.RunnerType;
import org.meier.export.ExporterType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InjectRunner {
    RunnerType runnerType() default RunnerType.FULL_REPORT_UNORDERED;
    ExporterType exporterType() default ExporterType.CLI_EXPORTER;
}
