package com.hao.common.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;

import com.hao.common.utils.LogUtil;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okio.Buffer;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * Created by wanghao2 on 2017/5/15.
 */

public class FileUtil {
    private final static String TAG = "FileUtil";

    static final String VERSION_EXT = ".v";

    public static synchronized void appendString2File(String filePath, String content) {
        RandomAccessFile randomFile = null;
        File f = new File(filePath);
        if (f == null) {
            return;
        }
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
            }
        }
        FileChannel channel = null;
        FileLock lock = null;
        try {
            randomFile = new RandomAccessFile(filePath, "rw");
            channel = randomFile.getChannel();
            do {
                lock = channel.tryLock();
            } while (null == lock);
            randomFile.seek(randomFile.length());
            randomFile.write(content.getBytes("UTF-8"));
            randomFile.writeBytes("\n");
        } catch (Throwable e) {
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (Exception e) {
                }
            }
            if (lock != null) {
                try {
                    lock.release();
                } catch (Throwable e) {
                }
            }
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static String readStringFromFile(String file) {
        File myfile = new File(file);
        if (myfile.exists()) {
            FileInputStream fis = null;
            InputStreamReader isr = null;
            try {
                fis = new FileInputStream(myfile);
                isr = new InputStreamReader(fis, "utf-8");
                char input[] = new char[fis.available()];
                isr.read(input);
                String str = new String(input);
                return str;
            } catch (Exception e) {
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Exception e) {
                    }
                }
                if (isr != null) {
                    try {
                        isr.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return null;
    }

    public static boolean saveString2File(String filePath, String content) {
        RandomAccessFile randomFile = null;
        File f = new File(filePath);
        File parent = f.getParentFile();
        if (!parent.exists()) {
            parent.mkdirs();
        }
        if (f.exists()) {
            f.delete();
        }
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
            }
        }
        FileChannel channel = null;
        FileLock lock = null;
        try {
            randomFile = new RandomAccessFile(filePath, "rw");
            channel = randomFile.getChannel();
            do {
                lock = channel.tryLock();
            } while (null == lock);
            randomFile.seek(randomFile.length());
            randomFile.write(content.getBytes("UTF-8"));
            return true;
        } catch (Exception e) {
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (Exception e) {
                }
            }
            if (lock != null) {
                try {
                    lock.release();
                } catch (Exception e) {
                }
            }
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e) {
                }
            }
        }
        return false;

    }

    // files目录下
    public static long getFileVersion(Context c, String fileName) {
        try {
            FileInputStream fis = c.openFileInput(fileName + VERSION_EXT);
            return getVersionFromStream(fis);
        } catch (Throwable e) {
            LogUtil.e(TAG, e);
        }
        return 0;
    }

    public static void removeFileVersion(Context c, String fileName) {
        File v = new File(fileName + VERSION_EXT);
        v.delete();
    }

    // assets目录下
    public static long getAssetsVersion(Context c, String fileName) {
        try {
            InputStream fis = c.getAssets().open(fileName + VERSION_EXT);
            return getVersionFromStream(fis);
        } catch (Throwable e) {
            LogUtil.e(TAG, e);
        }
        return 0;
    }

    public static void writeVersion(Context c, String fileName, long version) {
        FileWriter writer = null;
        try {

            File f = new File(fileName + VERSION_EXT);
            writer = new FileWriter(f, false);
            writer.write(String.valueOf(version));
            writer.flush();
        } catch (Throwable e) {
            LogUtil.e(TAG, e);

        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private static long getVersionFromStream(InputStream fis) {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(fis);
            String s = dis.readLine();
            return Long.parseLong(s);
        } catch (Throwable e) {
            LogUtil.e(TAG, e);
        } finally {
            closeInputStream(dis);
            closeInputStream(fis);
        }
        return 0;
    }

    public static boolean copyAssetsFile(Context c, String filename, String des) {
        boolean isSuccess = true;
        AssetManager assetManager = c.getAssets();

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            String newFileName = des + "/" + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            out.flush();
        } catch (Exception e) {
            isSuccess = false;
        } finally {
            FileUtil.closeInputStream(in);
            FileUtil.closeOutputStream(out);
        }

        return isSuccess;

    }

    public static void closeOutputStream(OutputStream os) {
        try {
            if (os != null) {
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            LogUtil.e(TAG, e);
        }
    }

    public static void closeInputStream(InputStream is) {
        try {
            if (is != null) {
                is.close();
            }
        } catch (IOException e) {
            LogUtil.e(TAG, e);
        }
    }

    public static String getRandomFileName() {
        String rel = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        rel = formatter.format(curDate);
        rel = rel + new Random().nextInt(1000);
        return rel;
    }

    public static byte[] file2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                LogUtil.e("文件不存在-----filePath:" + filePath);
            }
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 读取assets下的txt文件，返回utf-8 String
     *
     * @param context
     * @return
     */
    public static String readAssetsTxt(Context context, String fileName) {
        try {
            //Return an AssetManager instance for your application's package
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            // Convert the buffer into a string.
            String text = new String(buffer, "utf-8");
            // Finally stick the string into the text view.
            return text;
        } catch (IOException e) {
            // Should never happen!
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] readAssetsData(Context context, String fileName) throws IOException {
        InputStream is = context.getAssets().open(fileName);
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        return buffer;
    }

    public static void byte2file(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(filePath, fileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveFile(Bitmap bmp, String filePath, String fileName) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.mkdirs()) {//若创建文件夹不成功
                    System.out.println("Unable to create external cache directory");
                }
            }
            filePath = filePath + "/" + fileName;

            FileOutputStream fileout = new FileOutputStream(filePath);
            BufferedOutputStream bufferOutStream = new BufferedOutputStream(fileout);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bufferOutStream);
            bufferOutStream.flush();
            bufferOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File fileOrDirectory = new File(filePath);
        deleteFile(fileOrDirectory);
    }

    public static void deleteFile(File fileOrDirectory) {
        if (fileOrDirectory == null) {
            return;
        }
        if (fileOrDirectory.isDirectory()) {
            File[] files = fileOrDirectory.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    deleteFile(file);
                }
            }
        }
        fileOrDirectory.delete();
    }

    public static File saveFile(String filePath, String fileName, InputStream in) {
        return saveFile(new File(filePath + File.separator + fileName), in);
    }

    public static File saveFile(File file, InputStream is) {
        if (file.exists()) {
            boolean res = file.delete();
            if (!res) {
                LogUtil.e("删除文件失败 " + file.getName());
                return null;
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                boolean res = file.mkdirs();
                if (!res) {//若创建文件夹不成功
                    LogUtil.e("创建文件夹失败 " + file.getName());
                    return null;
                }
            }
        }
        try {
            Source source = Okio.source(is);
            Sink sink = Okio.sink(file);
            Buffer buffer = new Buffer();
            long l;
            while ((l = source.read(buffer, 1024L)) != -1) {
                sink.write(buffer, l);
                sink.flush();
            }
            return file;


        } catch (Exception e) {
            LogUtil.e(TAG, e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    public static File saveFile(String filePath, InputStream is) {
        return saveFile(new File(filePath), is);
    }

    public static final Bitmap getVideoThumbnailPath(String videoLocalPath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoLocalPath);
        Bitmap bitmap = retriever.getFrameAtTime(-1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        try {
            retriever.release();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bitmap;
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static List<String> getFilesName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            LogUtil.e("空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            s.add(files[i].getName());
        }
        return s;
    }
}
