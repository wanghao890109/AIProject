package com.hao.core.env;

import android.os.Environment;

import com.hao.service.env.EnvironmentService;

import java.io.File;

/**
 * 路径的配置
 * Created by wanghao2 on 2017/5/13.
 */

public class StorageDirectory {

    private static final String DEFAULT_FOLDER = "Haiyaa";//这个声明在xml/file_paths里面，不要修改
    private static final String APP_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DEFAULT_FOLDER;

    private static final String DATA_DIR = APP_DIR + "/Data";

    private static final String VOICE_DIR = APP_DIR + "/Voice";

    private static final String LOG_DIR = APP_DIR + "/Log";
    private static final String VIDEO_DIR = APP_DIR + "/Video";

    private static final String MUSIC_DIR = APP_DIR + "/Music";
    private static final String HTTP_CACHE_NAME = "http_Cache";
    private static final String HTTP_CACHE_DIR = APP_DIR + "/" + HTTP_CACHE_NAME;

    private static final String IMAGE_DIR = APP_DIR + "/Image";

    private static final String FILE_CACHE_DIR = APP_DIR + "/FileCache";

    private static final String DOWNLOAD_DIR = APP_DIR + "/Download";//这个声明在xml/file_paths里面，不要修改

    private static final String VOICE_SEND_DIR = VOICE_DIR + "/Send";
    private static final String VOICE_RECEIVE_DIR = VOICE_DIR + "/Receive";

    private static final String VIDEO_SEND_DIR = VIDEO_DIR + "/Send";
    private static final String VIDEO_RECEIVE_DIR = VIDEO_DIR + "/Receive";

    private static final String VOICE_EXAM_DIR = VOICE_DIR + "/exam";

    private static final String IMAGE_SEND_DIR = IMAGE_DIR + "/Send";
    private static final String IMAGE_RECEIVE_DIR = IMAGE_DIR + "/Receive";

    private static final String VIDEO_THUMBNAIL_DIR = VIDEO_DIR + "/thumbnail";

    private static final String getPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                //手机不存在sdcard
                path = EnvironmentService.getInstance().getContext().getApplicationContext().getFilesDir() + "/" + DEFAULT_FOLDER;
                file = new File(path);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        return null;
                    }
                }
                return path;
            }
        }
        return path;
    }

    public static boolean existSDCard() {
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static final String getRootDirectory() {
        return getPath(APP_DIR);
    }

    public static final String getVideoDirectory() {
        return getPath(VIDEO_DIR);
    }

    public static final String getMusicDirectory() {
        return getPath(MUSIC_DIR);
    }

    public static final String getImageDirectory() {
        return getPath(IMAGE_DIR);
    }

    public static final String getFileCacheDirectory() {
        return getPath(FILE_CACHE_DIR);
    }

    public static final String getGlideCacheExtraName() {
        return "Image";
    }

    public static final String getVoiceDirectory() {
        return getPath(VOICE_DIR);
    }

    public static final String getDownloadDirectory() {
        return getPath(DOWNLOAD_DIR);
    }

    public static final String getVoiceSendDirectory() {
        return getPath(VOICE_SEND_DIR);
    }

    public static final String getLogDirectory() {
        return getPath(LOG_DIR);
    }

    public static final String getVoiceReceiveDirectory() {
        return getPath(VOICE_RECEIVE_DIR);
    }


    public static final String getVideoReceiveDirectory() {
        return getPath(VIDEO_RECEIVE_DIR);
    }
    public static final String getVoiceExamDirectory() {
        return getPath(VOICE_EXAM_DIR);
    }

    public static final String getImageSendDirectory() {
        return getPath(IMAGE_SEND_DIR);
    }

    public static final String getImageReceiveDirectory() {
        return getPath(IMAGE_RECEIVE_DIR);
    }

    public static final String getVideoThumbnailDirectory() {
        return getPath(VIDEO_THUMBNAIL_DIR);
    }

    public static final String getDbDirectory() {
        return getPath(DATA_DIR);
    }

    public static final String getCacheDirectory() {
        try {
            File cacheDirectory = EnvironmentService.getInstance().getContext().getCacheDir();
            if (cacheDirectory != null) {
                return new File(cacheDirectory, HTTP_CACHE_NAME).getAbsolutePath();
            }
        } catch (Exception e) {
        }
        try {
            File cacheDirectory = EnvironmentService.getInstance().getContext().getExternalCacheDir();
            if (cacheDirectory != null) {
                return new File(cacheDirectory, HTTP_CACHE_NAME).getAbsolutePath();
            }
        } catch (Exception e) {

        }

        return getPath(HTTP_CACHE_DIR);
    }

}
