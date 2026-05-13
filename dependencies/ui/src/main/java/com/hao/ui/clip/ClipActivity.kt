package com.hao.ui.clip

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.result.contract.ActivityResultContracts
import com.hao.common.utils.CameraUtils
import com.hao.common.utils.LogUtil
import com.hao.common.utils.PictureCompressUtils
import com.hao.ui.R
import com.hao.ui.base.BaseActivity
import com.hao.ui.databinding.ActivityClipBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClipActivity : com.hao.ui.base.BaseActivity<ActivityClipBinding>() {
    override fun getViewBinding(): ActivityClipBinding {
        return ActivityClipBinding.inflate(layoutInflater)
    }

    companion object {
        private var imageClipCallback: ImageClipCallback? = null
        private var clipShape: com.hao.ui.clip.ClipImageBorderView.ClipShap = com.hao.ui.clip.ClipImageBorderView.ClipShap.Circle

        /**
         * 跳转
         */
        fun start(
            context: Context?,
            clipShape: com.hao.ui.clip.ClipImageBorderView.ClipShap,
            imageClipCallback: ImageClipCallback
        ) {
            this.imageClipCallback = imageClipCallback
            this.clipShape = clipShape
            val i = Intent(context, ClipActivity::class.java)
            if (context !is Activity) {
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context?.startActivity(i)
        }
    }

    override fun initView() {
        binding?.toolbar?.tvDetail?.text = resources.getString(R.string.confirm)
        binding?.clipImageLayout?.setShap(clipShape)

        var openAlbumLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    if (data != null) {
                        var uri = data.data
                        GlobalScope.launch(Dispatchers.Main) {
                            var compressString: Bitmap?
                            val result = withContext(Dispatchers.IO) {
                                compressString = CameraUtils.getImageBitMap(
                                    uri,
                                    this@ClipActivity.applicationContext
                                )
                            }

                            if (compressString != null) {
                                binding?.clipImageLayout?.setImageBitmap(compressString)
                            } else {
                                finish()
                            }
                            LogUtil.e("PictureCompressUtils result ${(result)}")
                        }
                    } else {
                        finish()
                    }
                } else {
                    finish()
                }
            }
//        val intent = Intent(Intent.ACTION_PICK,
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        //intent.putExtra(Intent., true)
        openAlbumLauncher.launch(PictureCompressUtils.startGalleryIntent(applicationContext, false))
    }

    override fun initListener() {
        binding?.toolbar?.ivBack?.setOnClickListener {
            finish()
        }

        binding?.toolbar?.tvDetail?.setOnClickListener {
            val bitmap = binding?.clipImageLayout?.clip()
            LogUtil.e("PictureCompressUtils clip bitmap.width=" + bitmap?.width + " bitmap.height=" + bitmap?.height)

            if (null != bitmap) {
                imageClipCallback?.onImageClip(PictureCompressUtils.compressBitmap(bitmap))
                finish()
            } else {
                finish()
            }
        }

    }

    interface ImageClipCallback {
        fun onImageClip(data: ByteArray?)
    }
}