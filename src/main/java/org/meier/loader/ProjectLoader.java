package org.meier.loader;

import org.meier.model.ClassWrapper;

import java.io.IOException;
import java.util.List;

public interface ProjectLoader {

    ClassWrapper loadFile(String filePath) throws IOException;

    List<ClassWrapper> loadProject(String dirPath) throws IOException;

}
