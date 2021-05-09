package org.meier.loader;

import java.io.IOException;

public interface ProjectLoader {

    void loadProject(String dirPath, String jarsDir) throws IOException;

}
