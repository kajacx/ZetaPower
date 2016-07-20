package com.hrkalk.zetapower.utils.loader;

/**
 * This class loader will not load certain classes, instead delegate to parent
 * class loader to do the job
 */
public class FilteredClassLoader extends DynamicClassLoader {

    private F1<String, Boolean> filter;

    public FilteredClassLoader(F1<String, Boolean> filter, String... paths) {
        super(paths);
        this.filter = filter;
    }

    @Override
    protected byte[] loadNewClass(String name) {
        if (!filter.e(name)) {
            return null;
        }

        return super.loadNewClass(name);
    }
}
