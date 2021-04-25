package org.meier.check.rule;

import org.meier.check.bean.DefectCase;
import org.meier.check.bean.RuleResult;
import org.meier.model.ClassMeta;
import org.meier.model.FieldMeta;
import org.meier.model.MethodMeta;

import java.util.*;

public class NonDescriptiveNamesRule implements Rule {

    private final Set<String> shortDescriptiveNames = Set.of("me", "id", "no", "or");

    @Override
    public RuleResult executeRule(Collection<ClassMeta> classes) {
        List<DefectCase> defects = new ArrayList<>();
        classes.forEach(cls -> {
            List<MethodMeta> methods = cls.getMethods();
            List<FieldMeta> fields = cls.getFields();
            methods.stream().map(MethodMeta::getShortName).filter(this::isNonDescriptive).forEach(name ->
                    defects.add(DefectCase.newInstance()
                    .setClassName(cls.getFullName())
                    .setMethodName(name)
                    .setDefectName("Non-descriptive method name")
                    .setDefectDescription(String.format("\"%s\" is probably not descriptive enough", name))));
            fields.stream().map(FieldMeta::getName).filter(this::isNonDescriptive).forEach(name ->
                    defects.add(DefectCase.newInstance()
                    .setDefectName("Non-descriptive field name")
                    .setClassName(cls.getFullName())
                    .setDefectDescription(String.format("\"%s\" is probably not descriptive enough", name))));
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
