package org.meier.model;

import com.github.javaparser.ast.body.InitializerDeclaration;
import org.meier.bean.CalledMethodBean;
import org.meier.bean.NameTypeBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CodeBlockMeta implements Meta, CodeContainer {

    private List<NameTypeBean> variables;
    private ClassMeta ownerClass;
    private InitializerDeclaration code;
    private boolean staticBlock;
    private int startLine;
    private List<MethodMeta> calledMethods;
    private List<CalledMethodBean> calledMethodsNames;

    public CodeBlockMeta setStaticBlock(boolean staticBlock) {
        this.staticBlock = staticBlock;
        return this;
    }

    public static CodeBlockMeta newInstance() {
        return new CodeBlockMeta();
    }

    public void resolveCalledMethods() {
        calledMethods = calledMethodsNames.stream()
                .map(meth -> {
                    ClassMeta ownerClass = MetaHolder.getClass(meth.getClassName());
                    if (ownerClass != null) {
                        return ownerClass.getMethods()
                                .stream()
                                .filter(mt -> mt.getName().equals(meth.getFullMethodName()))
                                .findFirst().orElse(null);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        calledMethods.forEach(meth -> meth.addCalledBy(this.ownerClass));
    }

    public ClassMeta getOwnerClass() {
        return ownerClass;
    }

    public CodeBlockMeta setOwnerClass(ClassMeta ownerClass) {
        this.ownerClass = ownerClass;
        return this;
    }

    public List<MethodMeta> getCalledMethods() {
        return calledMethods;
    }

    public CodeBlockMeta setCalledMethods(List<MethodMeta> calledMethods) {
        this.calledMethods = calledMethods;
        return this;
    }

    public List<CalledMethodBean> getCalledMethodsNames() {
        return calledMethodsNames;
    }

    public CodeBlockMeta setCalledMethodsNames(List<CalledMethodBean> calledMethodsNames) {
        this.calledMethodsNames = calledMethodsNames;
        return this;
    }

    @Override
    public void addVariable(NameTypeBean variable) {
        if (variables == null)
            variables = new ArrayList<>();
        this.variables.add(variable);
    }

    @Override
    public List<NameTypeBean> getVariables() {
        return variables;
    }

    public CodeBlockMeta setVariables(List<NameTypeBean> variables) {
        this.variables = variables;
        return this;
    }

    @Override
    public boolean isStatic() {
        return this.staticBlock;
    }

    @Override
    public Modifier accessModifier() {
        return null;
    }

    @Override
    public boolean isSynchronised() {
        return false;
    }

    @Override
    public int getStartLine() {
        return startLine;
    }

    public CodeBlockMeta setStartLine(int line) {
        this.startLine = line;
        return this;
    }

    public InitializerDeclaration getCode() {
        return code;
    }

    public CodeBlockMeta setCode(InitializerDeclaration code) {
        this.code = code;
        return this;
    }
}
