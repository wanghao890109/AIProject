package com.hao.common.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.media.ExifInterface
import android.provider.MediaStore
import java.io.*

/**
 * @author 李敬卫 2021/02/17
 *
 */
object PictureCompressUtils {

    /**
     * 计算缩放比
     * @param bitWidth 当前图片宽度
     * @param bitHeight 当前图片高度
     * @return int 缩放比
     */
    private fun getRatioSize(bitWidth: Int, bitHeight: Int): Int {
        // 图片最大分辨率
        val imageHeight = 1280
        val imageWidth = 960
        // 缩放比
        var ratio = 1
        // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (bitWidth > bitHeight && bitWidth > imageWidth) {
            // 如果图片宽度比高度大,以宽度为基准
            ratio = bitWidth / imageWidth
        } else if (bitWidth < bitHeight && bitHeight > imageHeight) {
            // 如果图片高度比宽度大，以高度为基准
            ratio = bitHeight / imageHeight
        }
        // 最小比率为1
        if (ratio <= 0) ratio = 1
        return ratio
    }

    /**
     * @Description: 图片压缩把Bitmap保存到指定目录
     * @param image
     * bitmap对象
     * @param filePath
     * 要保存的指定目录
     */
    private fun compressBitmap(image: Bitmap, filePath: String?) {
        // 最大图片大小 150KB
        val maxSize = 500
        // 获取尺寸压缩倍数
        val ratio: Int = getRatioSize(image.width, image.height)
        // 压缩Bitmap到对应尺寸
        val result =
            Bitmap.createBitmap(image.width / ratio, image.height / ratio, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)
        val rect = Rect(0, 0, image.width / ratio, image.height / ratio)
        canvas.drawBitmap(image, null, rect, null)
        val bos = ByteArrayOutputStream()
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到bos中
        var options = 100
        result.compress(Bitmap.CompressFormat.JPEG, options, bos)
        // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (bos.toByteArray().size / 1024 > maxSize) {
            // 重置bos即清空bos
            bos.reset()
            // 每次都减少10
            options -= 10
            // 这里压缩options%，把压缩后的数据存放到bos中
            result.compress(Bitmap.CompressFormat.JPEG, options, bos)
        }
        // 保存图片到SD卡 这个关键
        if (filePath?.isNotEmpty() == true) {
            saveBitmap(bos, filePath)
        }
        // 释放Bitmap
        if (!result.isRecycled) {
            result.recycle()
        }
    }

    /**
     * @Description: 图片压缩把Bitmap保存到指定目录
     * @param curFilePath
     * 当前图片文件地址
     * @param targetFilePath
     * 要保存的图片文件地址
     */
    fun compressBitmap(curFilePath: String, targetFilePath: String): String {
        // 最大图片大小 500KB
        val maxSize = 500
        //根据地址获取bitmap
        val result = getBitmapFromFile(curFilePath)
        val bos = ByteArrayOutputStream()
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到bos中
        var quality = 100
        result!!.compress(Bitmap.CompressFormat.JPEG, quality, bos)
        // 循环判断如果压缩后图片是否大于500kb,大于继续压缩
        while (bos.toByteArray().size / 1024 > maxSize) {
            // 重置bos即清空bos
            bos.reset()
            // 每次都减少10
            quality -= 10
            // 这里压缩quality，把压缩后的数据存放到bos中
            result.compress(Bitmap.CompressFormat.JPEG, quality, bos)
        }
        // 保存图片到SD卡 这个关键
        saveBitmap(bos, targetFilePath)
        // 释放Bitmap
        if (!result.isRecycled) {
            result.recycle()
        }

        return targetFilePath
    }

    /**
     * 图片压缩
     */
    fun compressBitmap(result: Bitmap?): ByteArray {
//        计算图片实际的大小两种方式
//        val file: File = File(Environment.getExternalStorageDirectory().getAbsolutePath()
//            .toString() + "/DCIM/Camera/test.jpg") Log . i "wechat", "file.length()="+file.length() / 1024)

//        FileInputStream fis = null; try { fis = new FileInputStream(file); } catch (FileNotFoundException e) { e.printStackTrace(); } try { Log.i("wechat", "fis.available()=" + fis.available() / 1024); } catch (IOException e) { // TODO Auto-generated catch block e.printStackTrace(); }
        // 最大图片大小 500K 占用内存大小
        val maxSize = 500
        val m = 1024
        //根据地址获取bitmap
        val bos = ByteArrayOutputStream()
        // 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到bos中
        var quality = 100
        result?.compress(Bitmap.CompressFormat.JPEG, quality, bos)
        // 循环判断如果压缩后图片是否大于5M,大于继续压缩
        LogUtil.e("PictureCompressUtils:图片Bitmap大小=" + result?.byteCount?.div(m) + "K")
        var isCompress = false
        LogUtil.e("PictureCompressUtils:图片大小=" + bos.toByteArray().size / m + "K")

        while (quality > 10 && bos.toByteArray().size / m > maxSize) {
            // 重置bos即清空bos
            bos.reset()
            // 每次都减少10
            quality -= 10
            LogUtil.e("PictureCompressUtils:quality=" + quality)
            // 这里压缩quality，把压缩后的数据存放到bos中
            result?.compress(Bitmap.CompressFormat.JPEG, quality, bos)
            LogUtil.e("PictureCompressUtils:quality=" + "quality" + "图片压缩之后大小=" + bos.toByteArray().size / m + "K")
            isCompress = true
        }


        //未压缩
        if (!isCompress) {
            bos.reset()
            result?.compress(Bitmap.CompressFormat.JPEG, 100, bos)
        }
        isCompress = true
        if (isCompress) {
//        //压缩完成再次检查图片大小，进行大小压缩，最大上传5M
            var bitmap = BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.toByteArray().size)
//            var matrix = Matrix()
            var zoom = 100f
            result?.let {
                var width = it.width
                var height = it.height
                LogUtil.e("PictureCompressUtils:width+" + width + "height=" + height)
                if (width > 0 && height > 0) {
                    while (bos.toByteArray().size / m > maxSize && zoom > 10) {
                        if (zoom <= 10) {
                            LogUtil.e("PictureCompressUtils:zoom <=   " + zoom)
                            break
                        }
                        zoom -= 10
                        var scale = zoom / 100
                        var widthTem = (width * scale).toInt()
                        var heightTem = (height * scale).toInt()
                        LogUtil.e("PictureCompressUtils:widthTem+" + widthTem + " heightTem=" + heightTem + " zoom =   " + zoom + " scale=" + scale)
//                        matrix.setScale(scale, scale)
//                        bitmap = Bitmap.createBitmap(bitmap, 0, 0,
//                            width, height, matrix, true)
                        bos.reset()
                        bitmap = Bitmap.createScaledBitmap(bitmap, widthTem, heightTem, true)
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, quality, bos)
                        LogUtil.e("PictureCompressUtils:quality=" + "quality" + "图片大小压缩之后大小=" + bos.toByteArray().size / m + "M")
                    }
                }
            }
            bitmap?.let {
                if (!it.isRecycled) {
                    it.recycle()
                }
            }
        }
        LogUtil.e("PictureCompressUtils:quality last=" + bos.toByteArray().size / m)
        result?.let {
            if (!it.isRecycled) {
                it.recycle()
            }
        }
        return bos.toByteArray()
    }

    /**
     * 头像和背景压缩
     */
    fun compressPng(curFilePath: String): ByteArray {
        val result = getBitmapFromFile(curFilePath)
        return compressBitmap(result)
    }

    fun startGalleryIntent(context: Context, isMultImage: Boolean): Intent {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (isMultImage) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        var packageManager = context.packageManager
        var resolveInfos =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (resolveInfos.size != 0) {
            LogUtil.e("PictureCompressUtils:startGalleryIntent 第一次尝试启动=")
            return intent
        } else {
            intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            if (isMultImage) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            }
            resolveInfos =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

            if (resolveInfos.size != 0) {
                LogUtil.e("PictureCompressUtils:startGalleryIntent 第二次尝试启动=")
                return Intent.createChooser(intent, "Select Picture")
            } else {
                intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                if (isMultImage) {
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
                val pickIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                pickIntent.type = "image/*"

                val chooserIntent = Intent.createChooser(intent, "Select Image")

                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))
                LogUtil.e("PictureCompressUtils:startGalleryIntent 第三次尝试启动=")
                return chooserIntent
            }
        }
    }

    private fun saveBitmap(bos: ByteArrayOutputStream, targetFilePath: String) {
        val file = File(targetFilePath)
        val parent = file.parent
        val fileDir = File(parent)
        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }

        val fos = FileOutputStream(targetFilePath)
        bos.writeTo(fos)
        fos.close()
        bos.close()
    }

    /**
     * 通过文件路径读获取Bitmap防止OOM以及解决图片旋转问题
     * @param filePath
     * @return
     */
    public fun getBitmapFromFile(filePath: String): Bitmap? {
        val newOpts = BitmapFactory.Options()
        newOpts.inJustDecodeBounds = true //只读边,不读内容
        BitmapFactory.decodeFile(filePath, newOpts)
        val w = newOpts.outWidth
        val h = newOpts.outHeight
        // 获取尺寸压缩倍数
        newOpts.inSampleSize = getRatioSize(w, h)
        newOpts.inJustDecodeBounds = false //读取所有内容
        newOpts.inDither = false
        newOpts.inPurgeable = true
        newOpts.inInputShareable = true
        newOpts.inTempStorage = ByteArray(32 * 1024)
        var bitmap: Bitmap? = null
        val file = File(filePath)
        var fs: FileInputStream? = null
        try {
            fs = FileInputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            if (fs != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(fs.fd, null, newOpts)
                //旋转图片
                val photoDegree = readPictureDegree(filePath)
                if (photoDegree != 0) {
                    val matrix = Matrix()
                    matrix.postRotate(photoDegree.toFloat())
                    // 创建新的图片
                    bitmap = Bitmap.createBitmap(
                        bitmap, 0, 0,
                        bitmap.width, bitmap.height, matrix, true
                    )
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fs != null) {
                try {
                    fs.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return bitmap
    }

    /**
     *
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    private fun readPictureDegree(path: String?): Int {
        var degree = 0
        if (path?.isNotEmpty() == true) {
            try {
                val exifInterface = ExifInterface(path)
                val orientation: Int = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
                when (orientation) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                    ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                    ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return degree
    }


}