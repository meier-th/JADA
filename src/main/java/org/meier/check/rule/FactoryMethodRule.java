package org.meier.check.rule;

import org.meier.check.bean.DefectCase;
import org.meier.check.bean.RuleResult;
import org.meier.check.rule.util.ClassMetaInfo;
import org.meier.check.rule.util.TypeInfo;
import org.meier.check.rule.visitor.IfSwitchVisitor;
import org.meier.check.rule.visitor.ObjectCreationVisitor;
import org.meier.inject.annotation.Rule;
import org.meier.model.ClassMeta;
import org.meier.model.MetaHolder;
import org.meier.model.MethodMeta;
import org.meier.model.Modifier;

import java.util.*;
import java.util.stream.Collectors;

@Rule
public class FactoryMethodRule implements CheckRule {

    @Override
    public RuleResult executeRule(Collection<ClassMeta> classes) {
        List<DefectCase> defects = new ArrayList<>();
        for (ClassMeta cls : classes) {
            cls.getMethods().forEach(method -> {
                ClassMeta createdType = MetaHolder.getClass(method.getFullQualifiedReturnType());
                if (createdType != null && returnsProjectClass(method) && isFirstVersion(method) && hasNoIfsAndSwitchesDeepSearch(method) &&
                        (createsObject(method, createdType) || method.getCalledMethods().stream().anyMatch(meth -> createsObject(meth, createdType))) &&
                        (method.getModifiers().contains(Modifier.FINAL) || getAllOverriddenVersions(method).stream().allMatch(this::hasNoIfsAndSwitchesDeepSearch)) &&
                        !ClassMetaInfo.hasBuilderSetter(createdType)) {
                    defects.add(DefectCase.newInstance()
                        .setDefectName("Unnecessary Factory method")
                        .setClassName(cls.getFullName())
                        .setLineNumber(method.getStartLine())
                        .setMethodName(method.getShortName())
                        .setDefectDescription("This factory method serves no purpose - it is not a part of Singleton or Builder pattern, " +
                                "all implementations create the object of same type and it is always set up the same way. Using constructor should probably be considered"));
                }
            });
        }
        return new RuleResult("Factory methods check", defects);
    }

    private boolean hasNoIfsAndSwitchesDeepSearch(MethodMeta method) {
        Set<MethodMeta> processedMethods = new HashSet<>();
        Stack<MethodMeta> methodsToProcess = new Stack<>();
        methodsToProcess.push(method);
        while (!methodsToProcess.isEmpty()) {
            MethodMeta meth = methodsToProcess.pop();
            if (processedMethods.contains(meth))
                continue;
            processedMethods.add(meth);
            if (!hasNoIfsAndSwitches(meth))
                return false;
            meth.getCalledMethods().forEach(methodsToProcess::push);
        }
        return true;
    }

    private boolean returnsProjectClass(MethodMeta method) {
        return MetaHolder.getClasses().containsKey(method.getFullQualifiedReturnType());
    }

    private boolean overriddenMethod(MethodMeta parentMethod, MethodMeta childMethod) {
        if (!parentMethod.getShortName().equals(childMethod.getShortName()))
            return false;
        if (parentMethod.getParameters().size() != childMethod.getParameters().size())
            return false;
        if (returnsProjectClass(parentMethod) ^ returnsProjectClass(childMethod))
            return false;
        if (!TypeInfo.isDescendant(parentMethod.getReturnType(), childMethod.getReturnType())) {
            return false;
        }
        for (int i = 0; i < parentMethod.getParameters().size(); ++i) {
            if (!TypeInfo.isDescendant(parentMethod.getParameters().get(i).getType(), childMethod.getParameters().get(i).getType()))
                return false;
        }
        return true;
    }

    private List<MethodMeta> getAllOverriddenVersions(MethodMeta method) {
        return ClassMetaInfo.getAllDescendants(method.getOwnerClass()).stream().flatMap(cls -> cls.getMethods().stream())
                .filter(meth -> overriddenMethod(method, meth)).collect(Collectors.toList());
    }

    private boolean isFirstVersion(MethodMeta method) {
        return ClassMetaInfo.getAllAncestors(method.getOwnerClass()).stream().flatMap(meth -> meth.getMethods().stream()).noneMatch(parentMeth -> overriddenMethod(parentMeth, method));
    }

    private boolean hasNoIfsAndSwitches(MethodMeta method) {
        return method.getContent().accept(new IfSwitchVisitor(), null).size() == 0;
    }

    private boolean createsObject(MethodMeta method, ClassMeta type) {
        return method.getContent().accept(new ObjectCreationVisitor(), type).size() > 0;
    }

}
