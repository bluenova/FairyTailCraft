/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bluenova.fairytailcraft.plugin;

import bluenova.fairytailcraft.FairyTailCraft;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
 
public class PluginLoader {
 
  public static List<MagePlugin> loadPlugins(File plugDir, FairyTailCraft plugin) throws IOException {
 
    File[] plugJars = plugDir.listFiles(new JARFileFilter());
    ClassLoader cl = new URLClassLoader(PluginLoader.fileArrayToURLArray(plugJars), plugin.getClass().getClassLoader());
    List<Class<MagePlugin>> plugClasses = PluginLoader.extractClassesFromJARs(plugJars, cl);
    return PluginLoader.createMagePluginObjects(plugClasses);
  }
 
  private static URL[] fileArrayToURLArray(File[] files) throws MalformedURLException {
 
    URL[] urls = new URL[files.length];
    for (int i = 0; i < files.length; i++) {
      urls[i] = files[i].toURI().toURL();
    }
    return urls;
  }
 
  private static List<Class<MagePlugin>> extractClassesFromJARs(File[] jars, ClassLoader cl) throws IOException {
 
    List<Class<MagePlugin>> classes = new ArrayList<Class<MagePlugin>>();
    for (File jar : jars) {
      classes.addAll(PluginLoader.extractClassesFromJAR(jar, cl));
    }
    return classes;
  }
 
  @SuppressWarnings("unchecked")
  private static List<Class<MagePlugin>> extractClassesFromJAR(File jar, ClassLoader cl) throws IOException {
 
    List<Class<MagePlugin>> classes = new ArrayList<Class<MagePlugin>>();
    JarInputStream jaris = new JarInputStream(new FileInputStream(jar));
    JarEntry ent = null;
    while ((ent = jaris.getNextJarEntry()) != null) {
      if (ent.getName().toLowerCase().endsWith(".class")) {
        try {
          Class<?> cls = cl.loadClass(ent.getName().substring(0, ent.getName().length() - 6).replace('/', '.'));
          if (PluginLoader.isMagePluginClass(cls)) {
            classes.add((Class<MagePlugin>)cls);
          }
        }
        catch (ClassNotFoundException e) {
          System.err.println("Can't load Class " + ent.getName());
          e.printStackTrace();
        }
      }
    }
    jaris.close();
    return classes;
  }
 
  private static boolean isMagePluginClass(Class<?> cls) {
 
    for (Class<?> i : cls.getInterfaces()) {
      if (i.equals(MagePlugin.class)) {
        return true;
      }
    }
    return false;
  }
 
  private static List<MagePlugin> createMagePluginObjects(List<Class<MagePlugin>> MagePlugins) {
 
    List<MagePlugin> plugs = new ArrayList<MagePlugin>(MagePlugins.size());
    for (Class<MagePlugin> plug : MagePlugins) {
      try {
        plugs.add(plug.newInstance());
      }
      catch (InstantiationException e) {
        System.err.println("Can't instantiate plugin: " + plug.getName());
        e.printStackTrace();
      }
      catch (IllegalAccessException e) {
        System.err.println("IllegalAccess for plugin: " + plug.getName());
        e.printStackTrace();
      }
    }
    return plugs;
  }
}
