package com.hao.ui.widget.bottomnavbar;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

import com.hao.ui.widget.UnreadTextView;
import com.hao.ui.widget.bottomnavbar.BottomNavigationItem;
import com.hao.ui.widget.bottomnavbar.BottomNavigationItemViewHolder;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.hao.ui.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;


public class SvgaBottomNavigationHolder extends BottomNavigationItemViewHolder {
    private static final String TAG = "SvgaBottomNavigationHolder";
    public SVGAParser svgaParser;

    protected TextView labelView;
    protected SVGAImageView iconView;
    protected UnreadTextView unreadTextView;

    protected Drawable mCompactIcon;
    protected Drawable mCompactReplaceIcon;

    private OnSvgaBottomNavigationHolderBindListener holderListener;

    public SvgaBottomNavigationHolder(@NotNull BottomNavigationItem itemInfo, @Nullable OnSvgaBottomNavigationHolderBindListener holderListener) {
        super(itemInfo);
        this.holderListener = holderListener;
    }

    @Override
    public int getLayoutId() {
        return R.layout.svga_bottom_navigation_item;
    }

    @Override
    public void initItemView(@NotNull View view) {
        super.initItemView(view);
        labelView = view.findViewById(R.id.bottomLabel);
        iconView = view.findViewById(R.id.svgaImage);
        unreadTextView = view.findViewById(R.id.tv_unread_count);
        labelView.getPaint().setFakeBoldText(true);
    }

    @Override
    public void onSelect() {
        labelView.getPaint().setFakeBoldText(true);
        isSelected = true;
        labelView.setText(itemInfo.isShowReplace ? itemInfo.replaceTitle : getActiveTitle());
        iconView.setSelected(true);
        labelView.setTextColor(itemInfo.activeColor);

        //优先显示皮肤
        if (itemInfo.iconSvgaFile != null && itemInfo.iconSvgaFile.exists()) {
            showSvgaLocalFile(itemInfo.iconSvgaFile, itemInfo.svagReplaceFile);
        } else if (!TextUtils.isEmpty(itemInfo.iconSvgaAssets)) {
            showSvgaAssetsFile(itemInfo.iconSvgaAssets, itemInfo.svgaReplaceAssets);
        }
    }

    @Override
    public void unSelect() {
        labelView.getPaint().setFakeBoldText(true);
        isSelected = false;

        labelView.setText(itemInfo.isShowReplace && itemInfo.hasSecondStatus ? itemInfo.replaceTitle : itemInfo.title);
        labelView.setTextColor(itemInfo.inActiveColor);
        iconView.setSelected(false);

        //如果有第二状态，需要传入replace svga资源
        if (itemInfo.hasSecondStatus) {
            if (!TextUtils.isEmpty(itemInfo.iconSvgaAssets)) {
                showSvgaAssetsFile(itemInfo.iconSvgaAssets, itemInfo.svgaReplaceAssets);
            } else {
                showSvgaLocalFile(itemInfo.iconSvgaFile, itemInfo.svagReplaceFile);
            }
        } else {
            if (itemInfo.iconSvgaFile != null && itemInfo.iconSvgaFile.exists()) {
                showSvgaLocalFile(itemInfo.iconSvgaFile);
            } else if (!TextUtils.isEmpty(itemInfo.iconSvgaAssets)) {
                showSvgaAssetsFile(itemInfo.iconSvgaAssets);
            }
        }
    }

    @Override
    public void setUnreadCount(int unReadCount) {
        unreadTextView.updateUnreadCount(unReadCount);
    }

    private String getActiveTitle() {
        return TextUtils.isEmpty(itemInfo.activeTitle) ? itemInfo.title : itemInfo.activeTitle;
    }

    @Override
    public void bindItemView() {
        if (isSelected) {
            labelView.setTextColor(itemInfo.activeColor);
            labelView.setText(itemInfo.isShowReplace ? itemInfo.replaceTitle : getActiveTitle());
        } else {
            labelView.setTextColor(itemInfo.inActiveColor);
            labelView.setText(itemInfo.isShowReplace && itemInfo.hasSecondStatus ? itemInfo.replaceTitle : itemInfo.title);
        }

        iconView.setSelected(isSelected);
        if (itemInfo.iconSvgaFile != null) {
            if (!isSelected && itemInfo.hasSecondStatus) {
                showSvgaLocalFile(itemInfo.iconSvgaFile, itemInfo.svagReplaceFile);
            } else if (isSelected && (itemInfo.hasTransformStatus || itemInfo.hasSecondStatus)) {
                showSvgaLocalFile(itemInfo.iconSvgaFile, itemInfo.svagReplaceFile);
            } else {
                showSvgaLocalFile(itemInfo.iconSvgaFile);
            }
        } else if (!TextUtils.isEmpty(itemInfo.iconSvgaAssets)) {
            if (!isSelected && itemInfo.hasSecondStatus) {
                showSvgaAssetsFile(itemInfo.iconSvgaAssets, itemInfo.svgaReplaceAssets);
            } else if (isSelected && (itemInfo.hasTransformStatus || itemInfo.hasSecondStatus)) {
                showSvgaAssetsFile(itemInfo.iconSvgaAssets, itemInfo.svgaReplaceAssets);
            } else {
                showSvgaAssetsFile(itemInfo.iconSvgaAssets);
            }
        } else {
            if (itemInfo.icon != null) {
                StateListDrawable states = new StateListDrawable();
                states.addState(new int[]{android.R.attr.state_selected},
                        itemInfo.isShowReplace && mCompactReplaceIcon != null ? mCompactReplaceIcon : mCompactIcon);
                states.addState(new int[]{-android.R.attr.state_selected},
                        itemInfo.icon);
                states.addState(new int[]{},
                        itemInfo.icon);
                iconView.setImageDrawable(states);
            } else if (mCompactIcon != null) {
                DrawableCompat.setTintList(mCompactIcon, new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_selected}, //1
                                new int[]{-android.R.attr.state_selected}, //2
                                new int[]{}
                        },
                        new int[]{
                                itemInfo.activeColor, //1
                                itemInfo.inActiveColor, //2
                                itemInfo.inActiveColor //3
                        }
                ));
                iconView.setImageDrawable(mCompactIcon);
            }
        }
        holderListener.onItemBindView(getItemView(), iconView, labelView);
    }

    protected void showSvgaAssetsFile(String svgaFile) {
        if (iconView == null || svgaParser == null) {
            return;
        }
        if (mCompactIcon == null) {
            svgaParser.decodeFromAssets(svgaFile, new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                    //有皮肤直接显示皮肤
                    if (mCompactIcon != null && itemInfo.iconSvgaFile != null) {
                        return;
                    }
                    mCompactIcon = new SVGADrawable(svgaVideoEntity);
                    playSvg(mCompactIcon);
                }

                @Override
                public void onError() {

                }
            }, null);
        } else {
            playSvg(mCompactIcon);
        }
    }

    protected void showSvgaLocalFile(File file) {
        if (iconView == null || svgaParser == null) {
            return;
        }
        if (mCompactIcon == null) {
            parseSvga(file, new SVGAParser.ParseCompletion() {
                @Override
                public void onError() {
                    // Logger.e(TAG, "showSvgaLocalFile decode failed");
                }

                @Override
                public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                    // Logger.i(TAG, "showSvgaLocalFile decode " + itemInfo.title);
                    mCompactIcon = new SVGADrawable(svgaVideoEntity);
                    ((SVGADrawable) mCompactIcon).setCurrentFrame$com_opensource_svgaplayer(1);
                    playSvg(mCompactIcon);
                }
            });
        } else {
            if (mCompactIcon instanceof SVGADrawable) {
                ((SVGADrawable) mCompactIcon).setCurrentFrame$com_opensource_svgaplayer(1);
            }
            playSvg(mCompactIcon);
        }
    }

    protected void showSvgaAssetsFile(String svgaFile, String replaceSvgaFile) {
        if (iconView == null || svgaParser == null) {
            return;
        }
        if (itemInfo.isShowReplace && !TextUtils.isEmpty(replaceSvgaFile)) {
            decodeAndPlaySVG(replaceSvgaFile);
        } else {
            showSvgaAssetsFile(svgaFile);
        }
    }

    private void decodeAndPlaySVG(String replaceSvgaFile) {
        if (mCompactReplaceIcon == null) {
            svgaParser.decodeFromAssets(replaceSvgaFile, new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NonNull SVGAVideoEntity svgaVideoEntity) {
                    if (mCompactReplaceIcon != null && itemInfo.svagReplaceFile != null) {
                        return;
                    }
                    mCompactReplaceIcon = new SVGADrawable(svgaVideoEntity);
                    iconView.setImageDrawable(mCompactReplaceIcon);
                    playSvg(mCompactReplaceIcon);
                }

                @Override
                public void onError() {

                }
            }, null);
        } else {
            iconView.setImageDrawable(mCompactReplaceIcon);
            playSvg(mCompactReplaceIcon);
        }
    }

    protected void showSvgaLocalFile(File file, File replaceFile) {
        if (itemInfo.isShowReplace && replaceFile != null) {
            if (itemInfo.hasSecondStatus) {
                if (mCompactReplaceIcon == null) {
                    parseSvga(replaceFile, new SVGAParser.ParseCompletion() {
                        @Override
                        public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                            mCompactReplaceIcon = new SVGADrawable(svgaVideoEntity);
                            playSvg(mCompactReplaceIcon);
                        }

                        @Override
                        public void onError() {
                            // Logger.d("Skin", "hasSecondStatus showSvgaLocalFile decode failed");
                        }
                    });
                } else {
                    playSvg(mCompactReplaceIcon);
                }
            } else {
                if (mCompactReplaceIcon == null) {
                    parseSvga(replaceFile, new SVGAParser.ParseCompletion() {
                        @Override
                        public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                            mCompactReplaceIcon = new SVGADrawable(svgaVideoEntity);
                            iconView.setImageDrawable(mCompactReplaceIcon);
                            iconView.startAnimation();
                        }

                        @Override
                        public void onError() {
                            // Logger.d("Skin", "showSvgaLocalFile decode failed");
                        }
                    });
                } else {
                    iconView.setImageDrawable(mCompactReplaceIcon);
                    iconView.startAnimation();
                }
            }
        } else {
            showSvgaLocalFile(file);
        }
    }

    private void playSvg(Drawable drawable) {
        iconView.setImageDrawable(drawable);

        if (iconView.isSelected()) {
            //这里andplay不为true，是因为总共只有2帧，如果改为true 会闪动
            if (isShowAnimation()) {
                iconView.stepToFrame(1, true);
            } else {
                iconView.stepToFrame(getEndFrame(drawable), false);
                setShowAnimation(true);
            }


        } else {
            iconView.stepToFrame(0, false);
        }
    }

    private int getEndFrame(Drawable drawable) {
        if (drawable instanceof SVGADrawable) {
            return Math.max(((SVGADrawable) drawable).getVideoItem().getFrames() - 1, 0);
        }
        return 0;
    }

    protected void parseSvga(File file, SVGAParser.ParseCompletion parseCompletion) {
        if (svgaParser == null || file == null || !file.exists()) {
            return;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (Exception e) {
            //Logger.e("showSvgaLocalFile", "showSvgaLocalFile file:$file  is fail");
        }
        if (fileInputStream == null) {
            return;
        }

        svgaParser.decodeFromInputStream(fileInputStream, buildCacheKey(file.getAbsolutePath()), parseCompletion, true, null, null);
    }

    /**
     * 由于SVGA没有把方法暴露出来，所以直接复制里面的实现
     */
    private String buildCacheKey(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(path.getBytes(StandardCharsets.UTF_8));
            byte[] digest = messageDigest.digest();

            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
        } catch (Exception e) {
            sb.append(path);
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public void reset() {
        super.reset();
        // Logger.i(TAG, "reset " + itemInfo.title + ", " + position);
        mCompactIcon = null;
        mCompactReplaceIcon = null;
    }

    interface OnSvgaBottomNavigationHolderBindListener {
        void onItemBindView(View itemView, SVGAImageView iconView, TextView labelView);
    }
}
