package org.meier.check.rule;

import org.meier.check.bean.DefectCase;
import org.meier.check.bean.RuleResult;
import org.meier.check.rule.util.ClassMetaInfo;
import org.meier.model.ClassMeta;
import org.meier.model.MetaHolder;
import org.meier.model.MethodMeta;

import java.util.*;
import java.util.stream.Collectors;

public class VisitorRule implements Rule {

    private final static int COMPLEXITY_FIELDS_NUM_THRESHOLD = 5;
    private final Map<ClassMeta, List<ClassMeta>> visitorsPerClass = new HashMap<>();

    @Override
    public RuleResult executeRule(Collection<ClassMeta> classes) {
        List<DefectCase> defects = new ArrayList<>();
        classes.stream()
                .filter(cls -> cls.getProjectImplementedInterfaces().isEmpty() && cls.getProjectExtendedClasses().isEmpty())
                .forEach(cls -> {
                    Set<ClassMeta> descendants = new HashSet<>(ClassMetaInfo.getAllDescendants(cls));
                    Map<ClassMeta, List<ClassMeta>> visitors = new HashMap<>();
                    descendants.forEach(visited ->
                            visitedBy(visited).forEach(visitor -> {
                                if (!visitorsPerClass.containsKey(visited))
                                    visitorsPerClass.put(visited, new ArrayList<>());
                                visitorsPerClass.get(visited).add(visitor);
                                if (!visitors.containsKey(visitor))
                                    visitors.put(visitor, new ArrayList<>());
                                visitors.get(visitor).add(visited);
                            })
                    );
                    visitors.entrySet().stream()
                            .filter(entry -> entry.getValue().size() >= 2 && entry.getValue().size() != descendants.size())
                            .forEach(entry -> {
                                Set<ClassMeta> commonAncestors = new HashSet<>();
                                for (ClassMeta child : entry.getValue())
                                    commonAncestors.addAll(ClassMetaInfo.getAllAncestors(child));
                                commonAncestors.removeAll(descendants);
                                if (commonAncestors.stream().noneMatch(ancestor -> visitedBy(ancestor).contains(entry.getKey()))) {
                                    defects.add(DefectCase.newInstance()
                                        .setDefectName("Inconsistent visitable classes hierarchy")
                                        .setClassName(cls.getFullName())
                                        .setDefectDescription(String.format("More than one descendant of class %s " +
                                                "implement a visitor pattern with %s as a visitor class, but some classes " +
                                                "in the hierarchy can not be visited. This is probably an inconsistency",
                                                cls.getFullName(), entry.getKey().getFullName())));
                                }
                            });
                    if (isClassTreeLikeAndComplexAndNotVisited(cls)) {
                        defects.add(DefectCase.newInstance()
                            .setDefectName("Visitor is advised")
                            .setClassName(cls.getFullName())
                            .setDefectDescription("Class %s represents a complex tree structure. It is advisable to " +
                                    "implement a Visitor to process this class' objects. That would help separate data " +
                                    "definition and business logic"));
                    }
                });
        return new RuleResult("Visitor check", defects);
    }

    private List<ClassMeta> visitedBy(ClassMeta visitedClass) {
        return visitedClass.getMethods().stream()
                .filter(meth -> {
                    if (!meth.getShortName().equalsIgnoreCase("accept"))
                        return false;
                    return meth.getParameters().stream()
                            .map(param -> MetaHolder.getClass(param.getTypeName()))
                            .filter(Objects::nonNull)
                            .anyMatch(visitorClass ->
                                    meth.getCalledMethods().stream()
                                            .anyMatch(calledMethod -> {
                                                if (calledMethod.getOwnerClass() != visitorClass)
                                                    return false;
                                                if (!calledMethod.getShortName().equalsIgnoreCase("visit"))
                                                    return false;
                                                return calledMethod.getParameters().stream()
                                                        .map(par -> MetaHolder.getClass(par.getTypeName()))
                                                        .filter(Objects::nonNull)
                                                        .anyMatch(paramType -> paramType == visitedClass);
                                            })
                            );
                }
                ).map(MethodMeta::getOwnerClass)
                                    .collect(Collectors.toList());
    }

    private boolean isClassTreeLikeAndComplexAndNotVisited(ClassMeta cls) {
        if (visitorsPerClass.containsKey(cls))
            return false;
        String clsName = cls.getFullName().substring(cls.getFullName().lastIndexOf("."));
        return cls.getFields().stream().anyMatch(field -> field.getFullClassName().endsWith("List<"+clsName+">") ||
                    field.getFullClassName().endsWith("Set<"+clsName+">") ||
                    field.getFullClassName().matches("\\w*Map<\\w+"+clsName+">")) && cls.getFields().size() > COMPLEXITY_FIELDS_NUM_THRESHOLD;
    }

}
