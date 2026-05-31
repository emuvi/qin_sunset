package com.vidlus.qin_sunset.work;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import com.vidlus.jarch.mage.WizArray;

public class Utils {
    public static String newRandomToken() {
        return java.util.UUID.randomUUID().toString();
    }

    public static String listFolder(File onDir, boolean listDir, boolean listFile, String endsWith) {
        var list = new ArrayList<String>();
        if (onDir.exists()) {
            for (var inside : onDir.listFiles()) {
                var shouldList = false;
                if (listDir && inside.isDirectory()) {
                    shouldList = true;
                }
                if (listFile && !inside.isDirectory()) {
                    shouldList = true;
                }
                if (shouldList && endsWith != null) {
                    shouldList = inside.getName().endsWith(endsWith);
                }
                if (shouldList) {
                    list.add(inside.getName());
                }
            }
        }
        Collections.sort(list);
        var result = new StringBuilder();
        for (var inside : list) {
            result.append(inside);
            result.append("\n");
        }
        return result.toString();
    }

    public static String listFilesWithExtension(File onDir, String extension) {
        var result = new StringBuilder();
        listFilesWithExtension(result, onDir.getAbsolutePath().length(), onDir, extension
                        .toLowerCase());
        return result.toString();
    }

    private static void listFilesWithExtension(StringBuilder making, int rootSize,
                    File onDir, String extension) {
        if (onDir.exists()) {
            for (var inside : onDir.listFiles()) {
                if (!inside.isDirectory()) {
                    if (inside.getName().toLowerCase().endsWith(extension)) {
                        making.append(inside.getAbsolutePath().substring(rootSize));
                        making.append("\n");
                    }
                }
            }
            for (var inside : onDir.listFiles()) {
                if (inside.isDirectory()) {
                    listFilesWithExtension(making, rootSize, new File(onDir, inside
                                    .getName()), extension);
                }
            }
        }
    }

    public static File resolveFile(String path, String parentIfRelative) {
        var parent = Paths.get(parentIfRelative);
        var child = Paths.get(path);
        var result = parent.resolve(child);
        return result.toFile();
    }

    public static void close(Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException ignore) {
            }
        }
    }

    private static final String[] TEXT_EXTENSIONS = new String[] {"txt", "htm", "html", "css", "log"};
    private static final String[] IMAGE_EXTENSIONS = new String[] {"jpg", "jpeg", "gif", "png", "ico", "bmp", "svg"};
    private static final String[] AUDIO_EXTENSIONS = new String[] {"mp3", "ogg", "wav", "midi", "mid"};
    private static final String[] VIDEO_EXTENSIONS = new String[] {"mp4", "ogv", "avi", "mpg", "webm", "flv", "mov"};

    public static String getMimeType(String fileName) {
        var dot = fileName.lastIndexOf(".");
        if (dot == -1) {
            return "application/octet-stream";
        }
        var extension = fileName.substring(dot + 1);
        if (extension.equals("js")) {
            return "text/javascript";
        }
        if (WizArray.has(extension, Utils.TEXT_EXTENSIONS)) {
            return "text/" + extension;
        }
        if (WizArray.has(extension, Utils.IMAGE_EXTENSIONS)) {
            return "image/" + extension;
        }

        if (WizArray.has(extension, Utils.AUDIO_EXTENSIONS)) {
            return "audio/" + extension;
        }

        if (WizArray.has(extension, Utils.VIDEO_EXTENSIONS)) {
            return "video/" + extension;
        }
        return "application/" + extension;
    }
}
