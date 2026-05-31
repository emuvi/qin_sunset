package com.vidlus.qin_sunset.work;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;

import com.vidlus.qin_sunset.swap.PathKind;
import com.vidlus.qin_sunset.swap.PathKindName;
import com.vidlus.qin_sunset.swap.PathList;
import com.vidlus.qin_sunset.swap.Transfer;
import com.vidlus.qin_sunset.swap.Where;

import com.vidlus.jarch.data.DataListArray;

public class OrdersDir {

    private OrdersDir() {}

    public static PathList dirList(File path) {
        var list = new DataListArray<PathKindName>();
        for (var inside : path.listFiles()) {
            list.add(new PathKindName(inside.isDirectory() ? PathKind.Folder : PathKind.File, inside.getName()));
        }
        return new PathList(path.getAbsolutePath(), list);
    }

    public static Where dirNew(File path) throws IOException {
        Files.createDirectories(path.toPath());
        return new Where(path.getAbsolutePath());
    }

    public static Transfer dirCopy(File origin, File destiny) throws IOException {
        FileUtils.copyDirectory(origin, destiny);
        return new Transfer(origin.getAbsolutePath(), destiny.getAbsolutePath());
    }

    public static Transfer dirMove(File origin, File destiny) throws IOException {
        FileUtils.moveDirectory(origin, destiny);
        return new Transfer(origin.getAbsolutePath(), destiny.getAbsolutePath());
    }

    public static Where dirDel(File path) throws IOException {
        FileUtils.deleteDirectory(path);
        return new Where(path.getAbsolutePath());
    }

    public static String fileRead(File path, boolean base64, Integer rangeStart,
                    Integer rangeLength) throws IOException {
        if (rangeStart != null) {
            try (var input = new FileInputStream(path);
                var output = new ByteArrayOutputStream();) {
                IOUtils.copyLarge(input, output, rangeStart, rangeLength);
                if (base64) {
                    return Base64.getEncoder().encodeToString(output.toByteArray());
                } else {
                    return new String(output.toByteArray());
                }
            }
        } else {
            if (base64) {
                return Base64.getEncoder().encodeToString(Files.readAllBytes(path.toPath()));
            } else {
                return Files.readString(path.toPath());
            }
        }
    }

    public static Where fileWrite(File path, boolean base64, String data,
                    Integer rangeStart) throws IOException {
        if (rangeStart != null) {
            try (var writer = new RandomAccessFile(path, "rw")) {
                writer.seek(rangeStart);
                if (base64) {
                    writer.write(Base64.getDecoder().decode(data));
                } else {
                    writer.writeUTF(data);
                }
            }
        } else {
            if (base64) {
                FileUtils.writeByteArrayToFile(path, Base64.getDecoder().decode(data),
                                false);
            } else {
                FileUtils.writeStringToFile(path, data, StandardCharsets.UTF_8, false);
            }
        }
        return new Where(path.getAbsolutePath());
    }

    public static Where fileAppend(File path, boolean base64, String data)
                    throws IOException {
        if (base64) {
            FileUtils.writeByteArrayToFile(path, Base64.getDecoder().decode(data), true);
        } else {
            FileUtils.writeStringToFile(path, data, StandardCharsets.UTF_8, true);
        }
        return new Where(path.getAbsolutePath());
    }

    public static Transfer fileCopy(File origin, File destiny) throws IOException {
        FileUtils.copyFile(origin, destiny);
        return new Transfer(origin.getAbsolutePath(), destiny.getAbsolutePath());
    }

    public static Transfer fileMove(File origin, File destiny) throws IOException {
        FileUtils.moveFile(origin, destiny);
        return new Transfer(origin.getAbsolutePath(), destiny.getAbsolutePath());
    }

    public static Where fileDel(File path) throws IOException {
        FileUtils.delete(path);
        return new Where(path.getAbsolutePath());
    }

}
