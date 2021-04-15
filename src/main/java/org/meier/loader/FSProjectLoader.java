package org.meier.loader;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.symbolsolver.utils.SymbolSolverCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import org.meier.check.visitor.ClassNameVisitor;
import org.meier.check.visitor.FieldVisitor;
import org.meier.check.visitor.InnerClassVisitor;
import org.meier.check.visitor.MethodVisitor;
import org.meier.model.ClassMeta;
import org.meier.model.MethodMeta;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
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
            } catch (IOException e) {
                // log
            }
        });
        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedSolver);
        return StaticJavaParser.getConfiguration()
                .setSymbolResolver(symbolSolver);
    }

    @Override
    public ClassMeta loadFile(String filePath, String projectPath, String jarsDir) throws IOException {
            init(Paths.get(projectPath), Paths.get(jarsDir));
            CompilationUnit headNode = StaticJavaParser.parse(Paths.get(filePath));
            return new ClassMeta(headNode.accept(new ClassNameVisitor(), null));
    }


    @Override
    public List<ClassMeta> loadProject(String dirPath, String jarsDir) throws IOException {
        Path path = Paths.get(dirPath);

        ProjectRoot projectRoot =
                new SymbolSolverCollectionStrategy(init(path, Paths.get(jarsDir)))
                        .collect(path);

        List<SourceRoot> roots = projectRoot.getSourceRoots();
        return roots.stream().flatMap(root -> {
            try {
                return root.tryToParse().stream();
            } catch (IOException e) {
                // some logs
                return Stream.empty();
            }
        }).map(pr -> pr.getResult().orElse(null))
                .filter(Objects::nonNull)
                .map(cu ->  {
                    String clsName = cu.accept(new ClassNameVisitor(), null);
                    ClassMeta cls = new ClassMeta(clsName);
                    cu.accept(new FieldVisitor(), cls);
                    cu.accept(new MethodVisitor(), cls);
                    cu.accept(new InnerClassVisitor(), cls);
                    cls.getMethods().forEach(MethodMeta::resolveCalledMethods);
                    return cls;
                })
                .collect(Collectors.toList());
    }

    private boolean isJar(Path file) {
        return file.toString().endsWith(".jar");
    }

}
