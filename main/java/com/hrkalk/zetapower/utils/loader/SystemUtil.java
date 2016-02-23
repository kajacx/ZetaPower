package com.hrkalk.zetapower.utils.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SystemUtil {
    static BufferedReader br;

    public static String readLine() {
        if (br == null) {
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        try {
            return br.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void onReturn(final P1<String> p1) {
        ThreadUtil.runStrong(new P0() {
            @Override
            public void e() {
                while (true) {
                    try {
                        String readLine = readLine();
                        p1.e(readLine);
                    } catch (Exception e1) {
                        return;
                    }
                }
            }
        });
    }
}
