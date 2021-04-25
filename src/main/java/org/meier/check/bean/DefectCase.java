package org.meier.check.bean;

public class DefectCase {

    private String className;
    private String methodName;
    private Integer lineNumber;
    private String defectName;
    private String defectDescription;

    private DefectCase(){}

    public static DefectCase newInstance() {
        return new DefectCase();
    }

    public String getClassName() {
        return className;
    }

    public DefectCase setClassName(String className) {
        this.className = className;
        return this;
    }

    public String getMethodName() {
        return methodName;
    }

    public DefectCase setMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public Integer getLineNumber() {
        return lineNumber;
    }

    public DefectCase setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
        return this;
    }

    public String getDefectName() {
        return defectName;
    }

    public DefectCase setDefectName(String defectName) {
        this.defectName = defectName;
        return this;
    }

    public String getDefectDescription() {
        return defectDescription;
    }

    public DefectCase setDefectDescription(String defectDescription) {
        this.defectDescription = defectDescription;
        return this;
    }
}
