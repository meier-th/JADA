package org.meier.model;

import com.github.javaparser.ast.body.InitializerDeclaration;
import org.meier.bean.NameTypeBean;

import java.util.ArrayList;
import java.util.List;

public class CodeBlockMeta implements Meta {

    private List<NameTypeBean> variables;
    private InitializerDeclaration code;
    private boolean staticBlock;
    private int startLine;

    public CodeBlockMeta setStaticBlock(boolean staticBlock) {
        this.staticBlock = staticBlock;
        return this;
    }

    public static CodeBlockMeta newInstance() {
        return new CodeBlockMeta();
    }

    public void addVariable(NameTypeBean variable) {
        if (variables == null)
            variables = new ArrayList<>();
        this.variables.add(variable);
    }

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
