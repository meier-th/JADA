package org.meier.loader;

import org.meier.model.ClassMeta;

import java.io.IOException;
import java.util.List;

public interface ProjectLoader {

    ClassMeta loadFile(String filePath, String projectPath, String jarsDir) throws IOException;

    List<ClassMeta> loadProject(String dirPath, String jarsDir) throws IOException;

}
