package org.meier.loader;

import org.meier.model.ClassMeta;

import java.io.IOException;

public interface ProjectLoader {

    ClassMeta loadFile(String filePath, String projectPath, String jarsDir) throws IOException;

    void loadProject(String dirPath, String jarsDir) throws IOException;

}
