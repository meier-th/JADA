package org.meier.check.visitor;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedType;
import org.meier.bean.NameTypeBean;
import org.meier.model.ClassMeta;
import org.meier.model.FieldMeta;
import org.meier.model.MethodMeta;
import org.meier.model.Modifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class MethodVisitor extends VoidVisitorAdapter<ClassMeta> {

    @Override
    public void visit(MethodDeclaration n, ClassMeta classMeta) {
        List<Modifier> modifiersList = new ArrayList<>();
        n.accept(new ModifierVisitor(), modifiersList);

        String fullName = n.resolve().getQualifiedName();

        Type retType = n.getType();
        Optional<NodeList<Type>> genericTypes = Optional.empty();
        if (n.getType().isClassOrInterfaceType())
            genericTypes = n.getType().asClassOrInterfaceType().getTypeArguments();
        String generics = genericTypes.orElse(NodeList.nodeList())
                .stream()
                .map(type -> type.resolve().asReferenceType().getQualifiedName())
                .collect(Collectors.joining(", "));
        String returnType = retType.isPrimitiveType() ? retType.asPrimitiveType().asString() :
                retType.isVoidType() ? "void" : retType.resolve().asReferenceType().getQualifiedName();
        if (!generics.isEmpty())
            returnType += "<"+generics+">";

        List<NameTypeBean> parameters = n.getParameters().stream().map(param ->  {
            ResolvedType type = param.getType().resolve();
            if (type.isPrimitive())
                return new NameTypeBean(param.getNameAsString(), type.asPrimitive().toString());
            return new NameTypeBean(param.getNameAsString(), type.asReferenceType().getQualifiedName());
        }).collect(Collectors.toList());

        List<FieldMeta> accessedFields = n.accept(new FieldsAccessVisitor(), classMeta);

        // methods
        MethodMeta method = new MethodMeta(fullName, Set.copyOf(modifiersList), parameters, accessedFields, List.of(), returnType);
        classMeta.getMethods().add(method);
    }
}
