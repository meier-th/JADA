package org.meier.check.rule;

import org.meier.check.bean.DefectCase;
import org.meier.check.bean.RuleResult;
import org.meier.inject.annotation.Rule;
import org.meier.model.ClassMeta;
import org.meier.model.FieldMeta;
import org.meier.model.MethodMeta;
import org.meier.model.Modifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Rule
public class EncapsulationRule implements CheckRule {

    @Override
    public RuleResult executeRule(Collection<ClassMeta> classes) {
        List<DefectCase> defects = new ArrayList<>();
        for (ClassMeta cls : classes) {
            getVulnerableFields(cls).forEach(field -> defects.add(DefectCase.newInstance()
                .setDefectName("Field is not encapsulated")
                .setClassName(cls.getFullName())
                .setLineNumber(field.getStartLine())
                .setDefectDescription(String.format("Field %s is not private and not final and therefore is not encapsulated", field.getName()))));
            getUnusedNonPrivateMethods(cls).forEach(method -> defects.add(DefectCase.newInstance()
                .setDefectName("Method is only used in its own class and is not private")
                .setClassName(cls.getFullName())
                .setLineNumber(method.getStartLine())
                .setMethodName(method.getShortName())
                .setDefectDescription(String.format("Method %s, which is only used by its own class, is not private. This could be a sign of lack of encapsulation", method.getName()))));
        }
        return new RuleResult("Encapsulation check", defects);
    }

    private List<FieldMeta> getVulnerableFields(ClassMeta cls) {
        return cls.getFields().values().stream().filter(field -> field.accessModifier() != Modifier.PRIVATE && !field.getModifiers().contains(Modifier.FINAL)
                && !field.getFullClassName().equals("java.lang.String")).collect(Collectors.toList());
    }

    private List<MethodMeta> getUnusedNonPrivateMethods(ClassMeta cls) {
        return cls.getMethods().stream().filter(meth -> !meth.getModifiers().contains(Modifier.PRIVATE))
                .filter(meth -> meth.getCalledBy().size() == 1 && meth.getCalledBy().contains(cls)).collect(Collectors.toList());
    }

}
