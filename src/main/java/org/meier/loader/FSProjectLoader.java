package org.meier.loader;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import org.meier.build.visitor.*;
import org.meier.model.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FSProjectLoader implements ProjectLoader {

    private ParserConfiguration init(Path projectPath, Path jarDir) throws IOException {
        TypeSolver reflectionTypeSolver = new ReflectionTypeSolver();
        CombinedTypeSolver combinedSolver = new CombinedTypeSolver();
        JavaParserTypeSolver javaParserTypeSolver = new JavaParserTypeSolver(projectPath);
        combinedSolver.add(reflectionTypeSolver);
        combinedSolver.add(javaParserTypeSolver);
        Files.walk(jarDir, Integer.MAX_VALUE).filter(this::isJar).forEach(jar -> {
            try {
                combinedSolver.add(new JarTypeSolver(jar));
            } catch (IOException ignored) {}
        });
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedSolver);
        return StaticJavaParser.getConfiguration()
                .setSymbolResolver(symbolSolver);
    }

    @Override
    public void loadProject(String dirPath, String jarsDir) throws IOException {
        Path path = Paths.get(dirPath);
        ProjectRoot projectRoot =
                new SymbolSolverCollectionStrategy(init(path, Paths.get(jarsDir)))
                        .collect(path);

        List<SourceRoot> roots = projectRoot.getSourceRoots();
        List<CompilationUnit> cus = roots.stream().flatMap(root -> {
            try {
                return root.tryToParse().stream();
            } catch (IOException e) {
                return Stream.empty();
            }
        }).map(pr -> pr.getResult().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        LinkedHashMap<CompilationUnit, ClassMeta> classMetaForCUs = new LinkedHashMap<>();
        cus.forEach(cu -> {
            boolean[] isInterface = new boolean[1];
            String clsName = cu.accept(new ClassNameVisitor(), isInterface);
            if (clsName != null) {
                List<Modifier> modifiersList = cu.accept(new ModifierVisitor(), ModifierVisitor.ModifierLevel.CLASS);
                ClassMeta cls = new ClassMeta(clsName, modifiersList, isInterface[0]);
                cls.setStartLine(cu.getBegin().get().line);
                ClassOrInterfaceDeclaration clIntDecl = (ClassOrInterfaceDeclaration) cu.getChildNodes().stream().filter(node -> node instanceof ClassOrInterfaceDeclaration).findFirst().orElse(null);
                EnumDeclaration enumDecl = (EnumDeclaration) cu.getChildNodes().stream().filter(node -> node instanceof EnumDeclaration).findFirst().orElse(null);
                if (clIntDecl != null) {
                    try {
                        cls.setExtendedClasses(clIntDecl.getExtendedTypes().stream().map(type -> type.resolve().getQualifiedName()).collect(Collectors.toList()));
                        cls.setImplementedInterfaces(clIntDecl.getImplementedTypes().stream().map(type -> type.resolve().getQualifiedName()).collect(Collectors.toList()));
                    } catch (Exception ignored) {}
                } else if (enumDecl != null) {
                    cls.setImplementedInterfaces(enumDecl.getImplementedTypes().stream().map(type -> type.resolve().getQualifiedName()).collect(Collectors.toList()));
                }
                cu.accept(new InitializerBlocksVisitor(), cls);
                MetaHolder.addClass(cls);
                cu.accept(new FieldVisitor(), cls);
                cu.accept(new ConstructorVisitor(), cls);
                cu.accept(new InnerClassVisitor(), cls);
                classMetaForCUs.put(cu, cls);
            }
        });
        classMetaForCUs.forEach((cu, cls) -> { try {cu.accept(new MethodVisitor(), cls);} catch(Exception ignored){}});
        InnerClassVisitor.runInnerClassesMethodVisitors();
        MetaHolder.forEach(ClassMeta::resolveExtendedAndImplemented);
        MetaHolder.forEach(ClassMeta::resolveMethodCalls);
    }

    private boolean isJar(Path file) {
        return file.toString().endsWith(".jar");
    }
}
