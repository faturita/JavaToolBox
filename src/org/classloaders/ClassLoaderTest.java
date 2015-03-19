package org.classloaders;
import java.net.URLClassLoader;
import java.security.SecureClassLoader;

/**
 * Esto es para probar ClassLoaders
 * 
 * User: vexrarod Date: 23/04/2004 Time: 15:42:58
 * 
 */
class hola {
    hola() {
        System.out.println("Construido.");
    }
}

public class ClassLoaderTest extends SecureClassLoader {
    public ClassLoaderTest() {
        super();
    };

    public ClassLoaderTest(ClassLoader parent) {
        super(parent);
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        System.out.println("loading class:" + name);
        return super.loadClass(name);
    }

    public static void main(String[] args) throws Exception {
        hola l = new hola();
    }
}
