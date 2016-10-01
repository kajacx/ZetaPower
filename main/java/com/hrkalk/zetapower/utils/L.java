package com.hrkalk.zetapower.utils;

public class L {
    public static int traceDepth = 1;

    private static int spamC = 0;

    public static void s(String spam) {
        if (spamC <= 0) {
            System.out.println("Spam: " + spam);
            spamC = 126;
        } else {
            spamC--;
        }
    }

    public static void e(String msg, Throwable t) {
        e(msg);
        e(t);
    }

    public static void e(String msg) {
        System.out.println("Error: " + msg);
        trace();
    }

    public static void e(Throwable ex) {
        if (ex == null)
            e("Throwable null poassed into log error function");
        else
            ex.printStackTrace(System.out);
    }

    public static void w(String msg) {
        System.out.println("Warning: " + msg);
        trace();
    }

    public static void d(String msg) {
        System.out.println("Debug: " + msg);
        //trace();
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
            System.out.print("  --  Called from: ");
            for (int i = 4; i < 4 + depth; i++) {
                System.out.format("%s.%s (%d)%n", trace[i].getClassName(), trace[i].getMethodName(), trace[i].getLineNumber());
            }
        }
    }
}
