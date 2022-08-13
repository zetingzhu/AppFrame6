package com.zzt.zt_textviewaction.selectV1;

/**
 * @author: zeting
 * @date: 2022/7/28
 * 文本选中监听
 */
public interface SelectActionListener {
    /**
     * 选中
     *
     * @param content
     */
    void onTextSelected(CharSequence content);

    /**
     * 拷贝
     *
     * @param content
     */
    void onTextSelectedCopy(CharSequence content);

    /**
     * 回复
     *
     * @param content
     */
    void onTextSelectedReply(CharSequence content);

    /**
     * 下载
     *
     * @param content
     */
    void onTextSelectedDownload(CharSequence content);
}
