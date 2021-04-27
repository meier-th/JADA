package org.meier.build.visitor;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.bean.CalledMethodBean;
import org.meier.bean.NameTypeBean;
import org.meier.build.util.TypeResolver;
import org.meier.model.ClassMeta;
import org.meier.model.FieldMeta;
import org.meier.model.MethodMeta;
import org.meier.model.Modifier;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodVisitor extends VoidVisitorAdapter<ClassMeta> {

    @Override
    public void visit(MethodDeclaration n, ClassMeta classMeta) {
        List<Modifier> modifiersList = n.accept(new ModifierVisitor(), ModifierVisitor.ModifierLevel.METHOD);

        String fullName = n.resolve().getQualifiedName();
        if (fullName.substring(0, fullName.lastIndexOf('.')).equals(classMeta.getFullName())) {

            Type retType = n.getType();
            String returnType = TypeResolver.getQualifiedName(retType);

            List<NameTypeBean> parameters = n.getParameters().stream().map(param ->
                    new NameTypeBean(param.getNameAsString(), TypeResolver.getQualifiedName(param.getType()), null)
            ).collect(Collectors.toList());

            List<FieldMeta> accessedFields = n.accept(new FieldsAccessVisitor(), null);

            List<CalledMethodBean> calledMethods = n.accept(new MethodCallVisitor(), null);

            MethodMeta method = new MethodMeta(n, fullName, Set.copyOf(modifiersList), parameters, accessedFields, calledMethods, returnType, classMeta);
            n.accept(new VariablesVisitor(), method);
            classMeta.addMethod(method);
        }
    }
}
