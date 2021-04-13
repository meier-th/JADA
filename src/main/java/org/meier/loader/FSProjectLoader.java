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
import org.meier.model.ClassMeta;

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
        TypeSolver javaParserTypeSolver = new JavaParserTypeSolver(projectPath);
        CombinedTypeSolver combinedSolver = new CombinedTypeSolver();
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
    public ClassMeta loadFile(String filePath, String jarsDir) throws IOException {
            init(Paths.get(filePath), Paths.get(jarsDir));
            CompilationUnit headNode = StaticJavaParser.parse(Paths.get(filePath));
            return new ClassMeta(headNode, "");
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
                .map(cu -> new ClassMeta(cu, ""))
                .collect(Collectors.toList());
    }

/*
    @Override
    public List<ClassWrapper> loadProject(String dirPath, String jarPath) throws IOException {
        Path srcPath = Paths.get(dirPath);
        Path libPath = Paths.get(jarPath);
        init(srcPath, libPath);
        if (!Files.isDirectory(srcPath))
            return List.of(loadFile(dirPath, jarPath));
        return Files.walk(srcPath, Integer.MAX_VALUE).filter(this::isJavaFile)
                .map(file -> {
                    try {
                        return new ClassWrapper(StaticJavaParser.parse(file));
                    } catch (IOException error){
                        return null;}
                }).collect(Collectors.toList());
    }
*/
    private boolean isJavaFile(Path file) {
        return Files.isRegularFile(file) && file.toString().endsWith(".java");
    }

    private boolean isJar(Path file) {
        return file.toString().endsWith(".jar");
    }

}
