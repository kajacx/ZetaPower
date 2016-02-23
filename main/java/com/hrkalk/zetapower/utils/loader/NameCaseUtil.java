package com.hrkalk.zetapower.utils.loader;

import java.util.regex.Matcher;

public class NameCaseUtil {
    public static String camelToHyphen(String name) {
        return RegexUtil.replaceAll(name, "[A-Z]|[0-9]+", new F1<Matcher, String>() {
            @Override
            public String e(Matcher m) {
                return (m.start() == 0 ? "" : "_") + m.group().toLowerCase();
            }
        });
    }

}
