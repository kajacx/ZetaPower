package com.hrkalk.zetapower.utils;

public class L {
    public static int traceDepth = 2;

    public static void e(String msg) {
        System.out.println("Error: " + msg);
        trace();
    }

    public static void e(Exception ex) {
        ex.printStackTrace(System.out);
    }

    public static void w(String msg) {
        System.out.println("Warning: " + msg);
        trace();
    }

    public static void d(String msg) {
        System.out.println("Debug: " + msg);
        trace();
    }

    public static void i(String msg) {
        System.out.println("Info: " + msg);
        trace();
    }

    private static void trace() {
        if (traceDepth > 0) {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            System.out.println("Called from:");
            for (int i = 3; i < 3 + traceDepth; i++) {
                System.out.format("%s.%s (%d)%n", trace[i].getClassName(), trace[i].getMethodName(), trace[i].getLineNumber());
            }
        }
    }
}
