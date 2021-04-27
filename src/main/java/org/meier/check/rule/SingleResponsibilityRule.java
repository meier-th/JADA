package org.meier.check.rule;

import com.github.javaparser.utils.Pair;
import org.meier.check.bean.DefectCase;
import org.meier.check.bean.RuleResult;
import org.meier.model.ClassMeta;
import org.meier.model.FieldMeta;
import org.meier.model.Meta;
import org.meier.model.MethodMeta;

import java.util.*;
import java.util.stream.Collectors;

public class SingleResponsibilityRule implements Rule {

    @Override
    public RuleResult executeRule(Collection<ClassMeta> classes) {
        List<DefectCase> defects = new ArrayList<>();
        for (ClassMeta meta : classes) {
            if (!isClassStaticSingleResponsible(meta)) {
                defects.add(DefectCase.newInstance()
                .setClassName(meta.getFullName())
                .setDefectName("Suspicious set of static and non-static methods in class")
                .setDefectDescription("Static methods' ratio is high, yet class contains non-static methods as well. " +
                        "Static and non-static methods probably serve different purposes, which breaks the" +
                        " Single Responsibility principle"));
            }
            List<Set<MethodMeta>> methodGroups = getPurposeGroups(meta);
            if (methodGroups.size() > 1) {
                defects.add(DefectCase.newInstance()
                    .setClassName(meta.getFullName())
                    .setDefectName("Several logic groups of methods in a single class")
                    .setDefectDescription("Class contains several almost independent groups of methods, which could be " +
                            "a sign of breaking the Single Responsibility principle. Groups:\n"+
                            buildPurposeGroupsDescription(methodGroups)));
            }
        }
        return new RuleResult("Single Responsibility check", defects);
    }

    private boolean isClassStaticSingleResponsible(ClassMeta meta) {
        int methodsNum = meta.getMethods().size();
        if (methodsNum > 2) {
            long staticMethodsNum = meta.getMethods()
                    .stream()
                    .filter(Meta::isStatic)
                    .filter(method -> !method.getFullQualifiedReturnType().equals(meta.getFullName()))
                    .count();
            return !(staticMethodsNum != methodsNum && (double)staticMethodsNum/(double)methodsNum > 0.3);
        }
        return true;
    }

    private String buildPurposeGroupsDescription(List<Set<MethodMeta>> purposeGroups) {
        StringBuilder description = new StringBuilder();
        int i = 0;
        for (Set<MethodMeta> group : purposeGroups) {
            description.append("group ").append(i).append(":\n");
            description.append(group.stream().map(MethodMeta::getShortName).collect(Collectors.joining(", ")));
            description.append("\n");
            ++i;
        }
        return description.toString();
    }

    private List<Set<MethodMeta>> getPurposeGroups(ClassMeta meta) {
        List<FieldMeta> fields = meta.getFields();
        List<Set<MethodMeta>> methodGroups = new ArrayList<>();
        List<MethodMeta> methods = meta.getMethods().stream().filter(method -> !method.isStatic() && !isGetterOrSetter(method, meta)).collect(Collectors.toList());
        if (!methods.isEmpty()) {
            List<Pair<MethodMeta, Set<FieldMeta>>> thisClassUsedFields = new ArrayList<>();
            methods.forEach(method -> thisClassUsedFields.add(new Pair<>(method,
                    method.getAccessedFields().stream().filter(fields::contains).collect(Collectors.toSet()))));
            Map<MethodMeta, Map<MethodMeta, Integer>> commonAccessedFields = new LinkedHashMap<>();
            int largestCommonFieldsValue = 0;
            for (int i = 0; i < thisClassUsedFields.size() - 1; ++i) {
                commonAccessedFields.put(thisClassUsedFields.get(i).a, new HashMap<>());
                for (int j = i + 1; j < thisClassUsedFields.size(); ++j) {
                    Set<FieldMeta> commonSet = new HashSet<>(thisClassUsedFields.get(i).b);
                    commonSet.retainAll(thisClassUsedFields.get(j).b);
                    int commonFields = commonSet.size();
                    if (commonFields > largestCommonFieldsValue)
                        largestCommonFieldsValue = commonFields;
                    commonAccessedFields.get(thisClassUsedFields.get(i).a).put(thisClassUsedFields.get(j).a, commonFields);
                }
            }
            Stack<MethodMeta> methodGroup = new Stack<>();

            while (!methods.isEmpty()) {
                methodGroup.push(methods.get(0));
                Set<MethodMeta> methodGroupSet = new HashSet<>();
                int finalLargestCommonFieldsValue = largestCommonFieldsValue;
                while (!methodGroup.empty()) {
                    MethodMeta method = methodGroup.pop();
                    methods.remove(method);
                    methodGroupSet.add(method);

                    if (commonAccessedFields.get(method) != null) {
                        commonAccessedFields.get(method).forEach((key, value) -> {
                            if (value >= finalLargestCommonFieldsValue / 3) {
                                methodGroup.push(key);
                            }
                        });
                    }
                }
                methodGroups.add(methodGroupSet);
            }
        }
        return methodGroups;
    }

    private boolean isGetterOrSetter(MethodMeta method, ClassMeta cls) {
        return (method.getShortName().startsWith("get") || method.getShortName().startsWith("set"))
                && cls.getFields().stream().anyMatch(field -> field.getName().equalsIgnoreCase(method.getShortName().substring(3)));
    }

}
