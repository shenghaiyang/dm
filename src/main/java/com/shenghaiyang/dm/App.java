package com.shenghaiyang.dm;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by shenghaiyang on 2016/7/25.
 */
public class App {

    public static void main(String[] args) {
        DependencyDownloader downloader = new DependencyDownloader();
        String location = location();
        downloader.download(location);
    }

    private static String location() {
        String originalPath = App.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String path;
        try {
            path = URLDecoder.decode(originalPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            path = originalPath;
        }
        if (path.contains(".jar")) {
            int index = path.lastIndexOf('/');
            return path.substring(0, index);
        }
        return path;
    }

}