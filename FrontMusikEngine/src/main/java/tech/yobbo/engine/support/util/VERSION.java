package tech.yobbo.engine.support.util;

/**
 * Created by xiaoJ on 6/8/2017.
 */
public final class VERSION {

    public final static int MajorVersion    = 1;
    public final static int MinorVersion    = 0;
    public final static int RevisionVersion = 0;

    public static String getVersionNumber() {
        return VERSION.MajorVersion + "." + VERSION.MinorVersion + "." + VERSION.RevisionVersion;
    }
}
