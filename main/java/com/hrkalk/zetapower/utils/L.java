package com.hrkalk.zetapower.utils;

public class L {
    public static int traceDepth = 2;

    private static int spamC = 0;

    public static void s(String spam) {
        if (spamC <= 0) {
            System.out.println(spam);
            spamC = 16;
        } else {
            spamC--;
        }
    }

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

    public static void d(String msg, int traceDepth) {
        System.out.println("Debug: " + msg);
        trace();
    }

    public static void i(String msg) {
        System.out.println("Info: " + msg);
        trace();
    }

    private static void trace() { //Don't worry love, the cavalry's here
        trace(traceDepth);
    }

    private static void trace(int depth) { //Cheers, love
        if (depth > 0) {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            System.out.println("Called from:");
            for (int i = 3; i < 3 + depth; i++) {
                System.out.format("%s.%s (%d)%n", trace[i].getClassName(), trace[i].getMethodName(), trace[i].getLineNumber());
            }
        }
    }
}
