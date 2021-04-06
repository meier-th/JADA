package org.meier.loader;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.meier.model.ClassWrapper;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FSProjectLoader implements ProjectLoader {

    @Override
    public ClassWrapper loadFile(String filePath) throws IOException {
            URI fileUri = URI.create(filePath);
            CompilationUnit headNode = StaticJavaParser.parse(Paths.get(fileUri));
            return new ClassWrapper(headNode);
    }

    @Override
    public List<ClassWrapper> loadProject(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.isDirectory(path))
            return List.of(loadFile(dirPath));
        return Files.walk(path, Integer.MAX_VALUE).filter(this::isJavaFile)
                .map(file -> {
                    try {
                        return new ClassWrapper(StaticJavaParser.parse(file));
                    } catch (IOException error){
                        return null;}
                }).collect(Collectors.toList());
    }

    private boolean isJavaFile(Path file) {
        return Files.isRegularFile(file) && file.toString().endsWith(".java");
    }

}
