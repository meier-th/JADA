package org.meier.check.rule;

import org.meier.check.bean.DefectCase;
import org.meier.check.bean.RuleResult;
import org.meier.model.ClassMeta;
import org.meier.model.FieldMeta;
import org.meier.model.MetaHolder;
import org.meier.model.MethodMeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class DependencyInversionRule implements Rule {

    @Override
    public RuleResult executeRule(Collection<ClassMeta> classes) {
        List<DefectCase> defects = new ArrayList<>();
        classes.forEach(cls -> {
            getUsedClasses(cls).stream().filter(usedCl -> isInherited(cls, usedCl)).forEach(cl ->
                    defects.add(DefectCase.newInstance()
                    .setDefectName("Ancestor depends on child class")
                    .setClassName(cls.getFullName())
                    .setDefectDescription(String.format("Class %s is ancestor of class %s, yet it depends on it, thus breaking the dependency inversion principle.",
                        cls.getFullName(), cl.getFullName()))));
        });
        return new RuleResult("Dependency inversion check", defects);
    }

    private List<ClassMeta> getUsedClasses(ClassMeta cls) {
        List<ClassMeta> used = new ArrayList<>();
        cls.getFields().stream().map(FieldMeta::getFullClassName).map(MetaHolder::getClass).filter(Objects::nonNull).forEach(used::add);
        cls.getMethods().stream().map(MethodMeta::getFullQualifiedReturnType).map(MetaHolder::getClass).filter(Objects::nonNull).forEach(used::add);
        cls.getMethods().stream().flatMap(meth -> meth.getCalledMethods().stream()).map(MethodMeta::getOwnerClass).forEach(used::add);
        cls.getMethods().stream().flatMap(meth -> meth.getVariables().stream()).map(variable -> MetaHolder.getClass(variable.getFullClassName())).filter(Objects::nonNull).forEach(used::add);
        cls.getCodeBlocks().stream().flatMap(block -> block.getVariables().stream()).map(variable -> MetaHolder.getClass(variable.getFullClassName())).filter(Objects::nonNull).forEach(used::add);
        cls.getCodeBlocks().stream().flatMap(block -> block.getCalledMethods().stream()).map(MethodMeta::getOwnerClass).forEach(used::add);
        return used;
    }

    private boolean isInherited(ClassMeta ancestor, ClassMeta child) {
        if (child.getProjectExtendedClasses().contains(ancestor) || child.getProjectImplementedInterfaces().contains(ancestor))
            return true;
        for (ClassMeta parent : child.getProjectExtendedClasses()) {
            if (isInherited(ancestor, parent))
                return true;
        }
        for (ClassMeta parent : child.getProjectImplementedInterfaces()) {
            if (isInherited(ancestor, parent))
                return true;
        }
        return false;
    }

}
