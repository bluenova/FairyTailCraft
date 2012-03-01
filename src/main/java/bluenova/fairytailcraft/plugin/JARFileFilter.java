/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluenova.fairytailcraft.plugin;

/**
 *
 * @author Sven
 */
import java.io.File;
import java.io.FileFilter;
 
public class JARFileFilter implements FileFilter {
 
  public boolean accept(File f) {
    return f.getName().toLowerCase().endsWith(".jar");
  }
}
