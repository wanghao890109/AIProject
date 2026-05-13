package com.hao.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by wanghao 2021/1/26 15:59
 */
public class ZipUtil {

    public static void unzip(File file, String dir) throws IOException {
        if (file.exists() && file.isFile()) {
            FileInputStream is = new FileInputStream(file);
            unzip(is, dir);
        }
    }

    public static void unzip(InputStream is, String dir) throws IOException {
        File dest = new File(dir);
        if (!dest.exists()) {
            dest.mkdirs();
        }

        if (!dest.isDirectory()) {
            throw new IOException("Invalid Unzip destination " + dest);
        } else if (null == is) {
            throw new IOException("InputStream is null");
        } else {
            ZipInputStream zip = new ZipInputStream(is);

            File file;
            label42:
            do {
                ZipEntry ze;
                while ((ze = zip.getNextEntry()) != null) {
                    String path = dest.getAbsolutePath() + File.separator + ze.getName();
                    String zeName = ze.getName();
                    char cTail = zeName.charAt(zeName.length() - 1);
                    if (cTail == File.separatorChar) {
                        file = new File(path);
                        continue label42;
                    }

                    FileOutputStream fout = new FileOutputStream(path);
                    byte[] bytes = new byte[1024];

                    int c;
                    while ((c = zip.read(bytes)) != -1) {
                        fout.write(bytes, 0, c);
                    }

                    zip.closeEntry();
                    fout.close();
                }

                return;
            } while (file.exists() || file.mkdirs());

            throw new IOException("Unable to create folder " + file);
        }
    }
}
