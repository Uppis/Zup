package com.vajasoft.zup;

import javax.swing.filechooser.*;
import java.io.File;

public class ArchiveFilter extends FileFilter {

    @Override
    public String getDescription() {
        return "Archives (*.zip, *.jar, *.war, *.ear)";
    }

    @Override
    public boolean accept(File f) {
        String name = f.getName().toUpperCase();
        return f.isDirectory() || (name.endsWith(".ZIP") || name.endsWith(".JAR") || name.endsWith(".WAR") || name.endsWith(".EAR"));
    }
}
