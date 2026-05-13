package com.hao.common.glide.progress;

import java.io.File;

public interface ProgressListener {
    void onProgress(int progress);

    void onLoadResult(boolean result, File file);
}