package org.meier.check.rule;

import org.meier.check.bean.DefectCase;
import org.meier.check.bean.RuleResult;
import org.meier.check.rule.util.ClassMetaInfo;
import org.meier.check.rule.util.TypeInfo;
import org.meier.inject.annotation.Rule;
import org.meier.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Rule
public class DecoratorRule implements CheckRule {

    private final static double DECORATOR_RATIO_THRESHOLD = 0.6;

    @Override
    public RuleResult executeRule(Collection<ClassMeta> classes) {
        List<DefectCase> defects = new ArrayList<>();
        classes.forEach(cls ->
                cls.getFields().values().stream().filter(field -> isFieldUsedToDecorate(field, cls))
                .filter(field -> !ClassMetaInfo.getAllAncestors(cls).contains(MetaHolder.getClass(field.getFullClassName())))
                .forEach(field ->
                        defects.add(DefectCase.newInstance()
                    .setDefectName("Missed decorator opportunity")
                    .setClassName(cls.getFullName())
                    .setLineNumber(field.getStartLine())
                    .setDefectDescription(String.format("There is a \"has-a\" relationship between %s and %s. Field %s in class %s is " +
                            "used to decorate class %s, but there is no inheritance between classes and therefore " +
                            "polymorphism is disabled. That is probably a missed opportunity to use Decorator pattern.",
                                cls.getFullName(),
                                field.getFullClassName(),
                                field.getName(),
                                cls.getFullName(),
                                field.getFullClassName())))));
        return new RuleResult("Decorator check", defects);
    }

    private boolean isFieldUsedToDecorate(FieldMeta field, ClassMeta decoratorCls) {
        ClassMeta decoratedCls = MetaHolder.getClass(field.getFullClassName());
        if (decoratedCls == null)
            return false;
        long decoratorMethodsNum = decoratorCls.getMethods().stream().filter(decoratorMeth -> !getDecoratedMethods(decoratorMeth, decoratedCls).isEmpty()).count();
        return decoratorMethodsNum / (double)decoratorCls.getMethods().size() >= DECORATOR_RATIO_THRESHOLD;
    }

    private List<MethodMeta> getDecoratedMethods(MethodMeta callerMethod, ClassMeta decoratedCls) {
        return callerMethod.getCalledMethods().stream()
                .filter(meth -> meth.getOwnerClass().equals(decoratedCls) && TypeInfo.isDescendant(meth.getReturnType(), callerMethod.getReturnType()))
                .collect(Collectors.toList());
    }

}
