package org.meier.export;

public enum ExporterType {
    CLI_EXPORTER {
        @Override
        public Class<? extends Exporter> type() {
            return CliExporter.class;
        }
    };
    public abstract Class<? extends Exporter> type();
}
