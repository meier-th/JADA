package org.meier.check.rule;

import org.meier.bean.NameTypeBean;
import org.meier.check.bean.DefectCase;
import org.meier.check.bean.RuleResult;
import org.meier.inject.annotation.Rule;
import org.meier.model.ClassMeta;
import org.meier.model.CodeBlockMeta;
import org.meier.model.FieldMeta;
import org.meier.model.MethodMeta;

import java.util.*;

@Rule
public class NonDescriptiveNamesRule implements CheckRule {

    private final Set<String> shortDescriptiveNames = Set.of("me", "id", "no", "or", "f", "is", "cl", "i");

    @Override
    public RuleResult executeRule(Collection<ClassMeta> classes) {
        List<DefectCase> defects = new ArrayList<>();
        classes.forEach(cls -> {
            List<MethodMeta> methods = cls.getMethods();
            List<FieldMeta> fields = cls.getFields();
            List<CodeBlockMeta> initializerBlocks = cls.getCodeBlocks();
            for (MethodMeta meth : methods) {
                String methName = meth.getShortName();
                if (isNonDescriptive(methName)) {
                    defects.add(DefectCase.newInstance()
                            .setClassName(cls.getFullName())
                            .setMethodName(methName)
                            .setDefectName("Non-descriptive method name")
                            .setDefectDescription(String.format("\"%s\" is probably not descriptive enough", methName))
                            .setLineNumber(meth.getStartLine()));
                }
                for (NameTypeBean variable : meth.getVariables()) {
                    if (!variable.isLoopVariable() && isNonDescriptive(variable.getName())) {
                        defects.add(DefectCase.newInstance()
                                .setDefectName("Non-descriptive variable name")
                                .setClassName(cls.getFullName())
                                .setDefectDescription(String.format("\"%s\" is probably not descriptive enough", variable.getName()))
                                .setLineNumber(meth.getStartLine()));
                    }
                }
            }
            for (FieldMeta field: fields) {
                String name = field.getName();
                if (isNonDescriptive(name)) {
                    defects.add(DefectCase.newInstance()
                            .setDefectName("Non-descriptive field name")
                            .setClassName(cls.getFullName())
                            .setDefectDescription(String.format("\"%s\" is probably not descriptive enough", name))
                            .setLineNumber(field.getStartLine()));
                }
            }
            for (CodeBlockMeta block : initializerBlocks) {
                for (NameTypeBean variable : block.getVariables()) {
                    if (!variable.isLoopVariable()) {
                        String name = variable.getName();
                        if (isNonDescriptive(name)) {
                            defects.add(DefectCase.newInstance()
                                    .setDefectName("Non-descriptive variable name")
                                    .setClassName(cls.getFullName())
                                    .setDefectDescription(String.format("\"%s\" is probably not descriptive enough", name))
                                    .setLineNumber(block.getStartLine()));
                        }
                    }
                }
            }
        });
        return new RuleResult("Descriptive naming test", defects);
    }

    private boolean isNonDescriptive(String name) {
        if (name.length() < 3 && !shortDescriptiveNames.contains(name))
            return true;
        long letters = name.chars().mapToObj(chr -> (char)chr).filter(Character::isLetter).count();
        double ratio = ((double)(name.length() - letters))/((double)(name.length()));
        return ratio >= 0.4;
    }

}
