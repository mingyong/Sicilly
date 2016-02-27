package xyz.shaohui.sicilly.data.services.auth;

import java.io.File;
import java.util.List;

/**
 * Created by kpt on 16/2/23.
 */
public final class SimpleRequestParam implements
        Comparable<SimpleRequestParam> {
    public static boolean hasFile(final List<SimpleRequestParam> params) {
        if (params.size() == 0) {
            return false;
        }
        boolean containsFile = false;
        for (final SimpleRequestParam param : params) {
            if (param.isFile()) {
                containsFile = true;
                break;
            }
        }
        return containsFile;
    }

    private String name = null;
    private String value = null;

    private File file = null;

    public SimpleRequestParam(final String name, final boolean value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public SimpleRequestParam(final String name, final double value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public SimpleRequestParam(final String name, final File file) {
        assert (file != null);
        this.name = name;
        this.file = file;
        this.value = file.getName();
    }

    public SimpleRequestParam(final String name, final int value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public SimpleRequestParam(final String name, final long value) {
        this.name = name;
        this.value = String.valueOf(value);
    }

    public SimpleRequestParam(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int compareTo(final SimpleRequestParam that) {
        int compared;
        compared = this.name.compareTo(that.getName());
        if (0 == compared) {
            compared = this.value.compareTo(that.getValue());
        }
        return compared;
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        int result = this.name.hashCode();
        result = (31 * result)
                + (this.value != null ? this.value.hashCode() : 0);
        result = (31 * result) + (this.file != null ? this.file.hashCode() : 0);
        return result;
    }

    public boolean isFile() {
        return this.file != null;
    }

    @Override
    public String toString() {
        return "[" + this.name + ":" + this.value + "]";
    }
}

