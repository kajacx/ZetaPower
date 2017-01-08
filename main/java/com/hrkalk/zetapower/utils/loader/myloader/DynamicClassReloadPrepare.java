package com.hrkalk.zetapower.utils.loader.myloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.commons.lang3.StringUtils;

import com.hrkalk.zetapower.client.render.vessel.ScaledRotator;
import com.hrkalk.zetapower.utils.L;
import com.hrkalk.zetapower.utils.loader.F1;
import com.hrkalk.zetapower.utils.loader.FilteredClassLoader;
import com.hrkalk.zetapower.utils.loader.ReflectUtil;

public class DynamicClassReloadPrepare {
    public List<ReloadTrigger> reloadWhen = new ArrayList<>();
    public List<Method> targetedMethods = new ArrayList<>();
    public String binFolder = "../bin";
    public String srcFolder = "src/main/java";
    public String extensionForCopy = ".java";
    public Class<?> watchedClass;
    public String watchedPath;
    public List<String> blacklistPrefix = new ArrayList<>();

    private Set<String> usedClasses = new HashSet<>();
    private PrintWriter[] writers = new PrintWriter[2];
    private List<Field> exposedFields = new ArrayList<>();
    private List<Method> exposedMethods = new ArrayList<>();

    public static void main(String[] args) {
        int response = JOptionPane.showConfirmDialog(null, "You are abount to create an autoloading class. Are you sure?");
        if (response != JOptionPane.YES_OPTION) {
            return;
        }

        DynamicClassReloadPrepare loader = new DynamicClassReloadPrepare(ScaledRotator.class);
        loader.addMethods("pushMatrixToGlStack");
        loader.doWork();
    }

    public DynamicClassReloadPrepare(Class<?> watchedClass) {
        this(watchedClass, 20);
    }

    public DynamicClassReloadPrepare(Class<?> watchedClass, int reloadTicks) {
        this.watchedClass = watchedClass;
        watchedPath = watchedClass.getCanonicalName().replace('.', '/');
        reloadWhen.add(new ReloadOnChange(watchedClass, binFolder));
        reloadWhen.add(new ReloadEveryNTicks(reloadTicks));
        usedClasses.add(ReflectUtil.class.getCanonicalName());
        blacklistPrefix.add("net.minecraft");
        blacklistPrefix.add("net.minecraftforge");
        blacklistPrefix.add("com.hrkalk.zetapower.dimension");
        blacklistPrefix.add("com.hrkalk.zetapower.client.input");
    }

    /**
     * Adds all overloaded methods with this name. Please be sure that the targeted method is eighter public,
     * or in case of protected method, overriden in your class.
     * @param methodName
     */
    public void addMethods(String methodName) {
        for (Method m : watchedClass.getMethods()) {
            if (m.getName().equals(methodName)) {
                targetedMethods.add(m);
            }
        }
        for (Method m : watchedClass.getDeclaredMethods()) {
            if (m.getName().equals(methodName) && !Modifier.isPublic(m.getModifiers())) {
                targetedMethods.add(m);
            }
        }
    }

    public void doWork() {
        try {
            makeCopy();
            prepareExpose();
            loadUsedClasses();
            prepareSourceFiles();
            prepareFields();
            prepareConstructors();
            exposeProtected();
            exposeSuper();
            printMethods();
            finishSourceFiles();
        } catch (IOException ex) {
            System.out.println("Exception when working");
            ex.printStackTrace(System.out);
        }

    }

    @SuppressWarnings("resource")
    private void makeCopy() throws IOException {
        String filename = srcFolder + '/' + watchedPath;

        File from = new File(filename + ".java");
        File to = new File(filename + "_Old" + extensionForCopy);

        //L.d(from.getAbsolutePath());

        if (!to.exists()) {
            to.createNewFile();
        }

        FileChannel in = new FileInputStream(from).getChannel();
        FileChannel out = new FileOutputStream(to).getChannel();

        out.transferFrom(in, 0, in.size());
    }

    private void prepareExpose() {

        Class<?> clazz = watchedClass.getSuperclass();
        while (!clazz.equals(Object.class)) {
            for (Field f : clazz.getDeclaredFields()) {
                if (Modifier.isProtected(f.getModifiers())) {
                    exposedFields.add(f);
                }
            }
            for (Method m : clazz.getDeclaredMethods()) {
                if (Modifier.isProtected(m.getModifiers()) && m.getDeclaringClass().equals(clazz)) {
                    exposedMethods.add(m);
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    private void loadUsedClasses() {
        usedClasses.add(watchedClass.getCanonicalName());
        usedClasses.add(watchedClass.getSuperclass().getCanonicalName());
        for (Method m : targetedMethods) {
            for (Class<?> c : m.getParameterTypes()) {
                if (!c.isPrimitive()) {
                    usedClasses.add(c.getCanonicalName());
                }
            }
            Class<?> c = m.getReturnType();
            if (!c.isPrimitive()) {
                usedClasses.add(c.getCanonicalName());
            }
        }

        usedClasses.add(DynamicReloader.class.getCanonicalName());
        usedClasses.add(ReloadTrigger.class.getCanonicalName());
        for (ReloadTrigger r : reloadWhen) {
            usedClasses.add(r.getClass().getCanonicalName());
        }

        for (Method m : exposedMethods) {
            for (Class<?> c : m.getParameterTypes()) {
                if (!c.isPrimitive()) {
                    usedClasses.add(c.getCanonicalName());
                }
            }
            Class<?> c = m.getReturnType();
            if (!c.isPrimitive()) {
                usedClasses.add(c.getCanonicalName());
            }
        }

        for (Field f : exposedFields) {
            Class<?> c = f.getType();
            if (!c.isPrimitive()) {
                usedClasses.add(c.getCanonicalName());
            }
        }
    }

    private void prepareSourceFiles() throws IOException {
        writers[0] = new PrintWriter(srcFolder + '/' + watchedPath + ".java");
        writers[1] = new PrintWriter(srcFolder + '/' + watchedPath + "_Reload.java");

        for (PrintWriter pw : writers) {
            pw.println("package " + watchedClass.getPackage().getName() + ";");
            pw.println();
            for (String clazz : this.usedClasses) {
                pw.println("import " + clazz + ";");
            }
            pw.println("import " + watchedClass.getCanonicalName() + "_Reload;");
            pw.println();
        }

        writers[0].println("public class " + watchedClass.getSimpleName() + " extends " + watchedClass.getSuperclass().getSimpleName() + " {");
        writers[1].println("public class " + watchedClass.getSimpleName() + "_Reload {");
    }

    private void prepareFields() {
        writers[0].println();
        writers[0].println("private DynamicReloader reloader = new DynamicReloader(" + watchedClass.getSimpleName() + ".class, \"" + binFolder + "\");");
        writers[0].println();
        writers[0].println('{');
        for (ReloadTrigger trigger : reloadWhen) {
            writers[0].println("reloader.reloadWhen.add(" + trigger.reverseConstructor() + ");");
        }
        writers[0].println();
        for (String clazz : usedClasses) {
            writers[0].println("reloader.addToBlacklist(\"" + clazz + "\");");
        }
        writers[0].println();
        for (String prefix : blacklistPrefix) {
            writers[0].println("reloader.addToBlacklistPrefix(\"" + prefix + "\");");
        }
        writers[0].println('}');

        writers[1].println();
        writers[1].println("public " + watchedClass.getSimpleName() + " thiz;");
    }

    private void prepareConstructors() {
        for (Constructor<?> c : watchedClass.getSuperclass().getDeclaredConstructors()) {
            List<String> params = new ArrayList<>();
            List<String> args = new ArrayList<>();

            int counter = 0;
            for (Parameter p : c.getParameters()) {
                String name = p.isNamePresent() ? p.getName() : ("arg" + (++counter));
                params.add(p.getType().getSimpleName() + " " + name);
                args.add(name);
            }

            String paramsAll = StringUtils.join(params, ", ");
            String argsAll = StringUtils.join(args, ", ");

            writers[0].println();
            writers[0].println("public " + watchedClass.getSimpleName() + "(" + paramsAll + ") { super(" + argsAll + "); }");
        }
    }

    private void exposeProtected() {
        for (Field f : exposedFields) {
            writers[0].println();
            writers[0].println("public " + f.getType().getSimpleName() + " get_" + f.getName() + "() { return " + f.getName() + "; }");
            if (!Modifier.isFinal(f.getModifiers())) {
                writers[0].println();
                writers[0].println("public void set_" + f.getName() + "(" + f.getType().getSimpleName() + " " + f.getName() + ") { this." + f.getName() + " = " + f.getName() + "; }");
            }
        }
        for (Method m : exposedMethods) {
            List<String> params = new ArrayList<>();
            List<String> args = new ArrayList<>();

            int counter = 0;
            for (Parameter p : m.getParameters()) {
                String name = p.isNamePresent() ? p.getName() : ("arg" + (++counter));
                params.add(p.getType().getSimpleName() + " " + name);
                args.add(name);
            }

            String paramsAll = StringUtils.join(params, ", ");
            String argsAll = StringUtils.join(args, ", ");
            String ret = m.getReturnType().getCanonicalName().equals("void") ? "" : "return ";

            writers[0].println();
            writers[0].println("public " + m.getReturnType().getSimpleName() + " call_" + m.getName() + "(" + paramsAll + ") { " + ret + m.getName() + "(" + argsAll + "); }");
        }

    }

    private void exposeSuper() {
        for (Method m : targetedMethods) {
            if (!isMethodOverridenInClass(m, watchedClass)) {
                //this method is not everriden, exposing the non-existing super method would acctually create an error
                continue;
            }

            List<String> params = new ArrayList<>();
            List<String> args = new ArrayList<>();

            int counter = 0;
            for (Parameter p : m.getParameters()) {
                String name = p.isNamePresent() ? p.getName() : ("arg" + (++counter));
                params.add(p.getType().getSimpleName() + " " + name);
                args.add(name);
            }

            String paramsAll = StringUtils.join(params, ", ");
            String argsAll = StringUtils.join(args, ", ");
            String ret = m.getReturnType().getCanonicalName().equals("void") ? "" : "return (" + m.getReturnType().getSimpleName() + ") ";

            writers[0].println();
            writers[0].println("public " + m.getReturnType().getSimpleName() + " super_" + m.getName() + "(" + paramsAll + ") {");
            writers[0].println("    " + ret + "super." + m.getName() + "(" + argsAll + ");");
            writers[0].println("}");
        }
    }

    private void printMethods() {
        for (Method m : targetedMethods) {
            List<String> params = new ArrayList<>();
            List<String> args = new ArrayList<>();
            args.add("\"" + m.getName() + "\"");
            args.add("reloader.getInstance(this)");

            int counter = 0;
            for (Parameter p : m.getParameters()) {
                String name = p.isNamePresent() ? p.getName() : ("arg" + (++counter));
                params.add(p.getType().getSimpleName() + " " + name);
                args.add(name);
            }

            String paramsAll = StringUtils.join(params, ", ");
            String argsAll = StringUtils.join(args, ", ");
            String ret = m.getReturnType().getCanonicalName().equals("void") ? "" : "return (" + m.getReturnType().getSimpleName() + ") ";

            writers[0].println();
            writers[0].println("public " + m.getReturnType().getSimpleName() + " " + m.getName() + "(" + paramsAll + ") {");
            writers[0].println("    try {");
            writers[0].println("        " + ret + "ReflectUtil.invoke(" + argsAll + ");");
            writers[0].println("    } catch(Throwable t) {");
            writers[0].println("        System.out.println(\"Exception while executing reloadable code.\");");
            writers[0].println("        t.printStackTrace(System.out);");
            writers[0].println("        " + getDefaultReturn(m.getReturnType()));
            writers[0].println("    }");
            writers[0].println("}");

            writers[1].println();
            writers[1].println("public " + m.getReturnType().getSimpleName() + " " + m.getName() + "(" + paramsAll + ") {");
            writers[1].println("    " + getDefaultReturn(m.getReturnType()));
            writers[1].println("}");
        }
    }

    private void finishSourceFiles() {
        writers[0].println();
        writers[1].println();

        writers[0].println("}");
        writers[1].println("}");

        writers[0].close();
        writers[1].close();
    }

    private String getDefaultReturn(Class<?> clazz) {
        switch (clazz.getSimpleName()) {
        case "void":
            return "//Thanks for using the Zeta Power Reloadable class generator.";
        case "byte":
        case "short":
        case "int":
        case "long":
        case "float":
        case "double":
            return "return 0;";
        case "boolean":
            return "return false;";
        default:
            return "return null;";
        }
    }

    public static interface ReloadTrigger {
        /**
         * Should the classloader reload the files?
         * @return
         * true if classes should be reloaded
         */
        public boolean shouldReload();

        /**
         * What code do you need to use to construct this Reload trigger?
         * Example: <code>ReloadTrigger trigger = `result here`;</code><br>
         * Your reload trigger will be importer, no need to use full name
         * @return
         * code needed to run to create this reload trigger
         */
        public String reverseConstructor();
    }

    public static class ReloadOnChange implements ReloadTrigger {
        private File watchedFile;
        private long lastLodaded = Long.MIN_VALUE;
        private Class<?> clazz;
        private String binFolder;

        public ReloadOnChange(Class<?> clazz, String binFolder) {
            this.clazz = clazz;
            this.binFolder = binFolder;
            String filename = binFolder + '/' + clazz.getCanonicalName().replace('.', '/') + "_Reload.class";
            //L.d(filename);
            watchedFile = new File(filename);
            //L.d(watchedFile.getAbsolutePath());
            //L.d(watchedFile.exists() + "");
        }

        @Override
        public boolean shouldReload() {
            if (watchedFile.lastModified() > lastLodaded) {
                lastLodaded = watchedFile.lastModified();
                return true;
            }
            return false;
        }

        @Override
        public String reverseConstructor() {
            return "new ReloadOnChange(" + clazz.getCanonicalName() + ".class, \"" + binFolder + "\")";
        }

    }

    public static class ReloadEveryNTicks implements ReloadTrigger {
        private int cap;
        private int cur;

        /**
         * Reloads all classes every N ticks.
         * @param ticks
         * use non-positive number to negate this effect
         */
        public ReloadEveryNTicks(int ticks) {
            cap = ticks;
        }

        @Override
        public boolean shouldReload() {
            if (cur <= 0) {
                cur = cap - 1;
                return true;
            }
            cur--;
            return false;
        }

        @Override
        public String reverseConstructor() {
            return "new ReloadEveryNTicks(" + cap + ")";
        }

    }

    public static class DynamicReloader {
        public List<ReloadTrigger> reloadWhen = new ArrayList<>();
        private List<String> blacklist = new ArrayList<>();
        private List<String> blacklistPrefix = new ArrayList<>();
        private Object instance;
        private Class<?> watchedClass;
        public String binFolder;

        public DynamicReloader(Class<?> watchedClass, String binFolder) {
            this.watchedClass = watchedClass;
            this.binFolder = binFolder;
        }

        public void addToBlacklist(String className) {
            if (!className.equals(watchedClass.getCanonicalName() + "_Reload")) {
                blacklist.add(className);
            }
        }

        public void addToBlacklistPrefix(String prefix) {
            blacklistPrefix.add(prefix);
        }

        public Object getInstance(Object thiz) throws Exception {
            boolean reload = false;
            for (ReloadTrigger trigger : reloadWhen) {
                reload |= trigger.shouldReload();
            }
            //L.s("Reload: " + reload);
            if (reload || instance == null) {
                F1<String, Boolean> reloadWhat = this::shouldReload;

                FilteredClassLoader classLoader = new FilteredClassLoader(reloadWhat, binFolder);
                // L.s("Loader: " + classLoader);
                Class<?> contextClass = classLoader.load(watchedClass.getCanonicalName() + "_Reload");
                //L.s("Class: " + contextClass);
                try {
                    instance = contextClass.newInstance();
                } catch (Throwable t) {
                    L.e("Error in dynamic loading", t);
                }
                //L.s("Instance: " + instance);
                contextClass.getField("thiz").set(instance, thiz);
            }
            return instance;
        }

        private boolean shouldReload(String className) {
            if (className.endsWith("_Reload")) {
                return true;
            }
            try {
                Class.forName(className + "_Reload");
                return false; //don't reload if _Reload class exists
            } catch (Exception ex) {
                //do nothing
            }
            return !blacklist.contains(className) && !blacklistPrefix.stream().anyMatch(prefix -> className.startsWith(prefix));
        }

    }

    private boolean isMethodOverridenInClass(Method m, Class<?> c) {
        do {
            c = c.getSuperclass();
            try {
                Method m1 = c.getDeclaredMethod(m.getName(), m.getParameterTypes());
                if (!Modifier.isPrivate(m1.getModifiers())) {
                    //overriden
                    return true;
                }
            } catch (NoSuchMethodException e) {
                // no method found
            } catch (SecurityException e) {
                // genuine exception
                e.printStackTrace(System.out);
            }
        } while (c != Object.class);
        //no overriden methods found
        return false;
    }
}
