package com.zzt.zt_textviewaction.selectV1;

/**
 * @author: zeting
 * @date: 2022/7/28
 */
public class SelectionInfo {
    /** 开始的左边的游标 */
    public Cursor startCursor = new Cursor();
    /** 结束的右边的游标 */
    public Cursor endCursor = new Cursor();
    /** 选择的内容 */
    public CharSequence content = "";

    public void clear() {
        startCursor.clear();
        endCursor.clear();
        content = "";
    }

    @Override
    public SelectionInfo clone() {
        final SelectionInfo newSelectionInfo = new SelectionInfo();

        newSelectionInfo.startCursor = startCursor.clone();
        newSelectionInfo.endCursor = endCursor.clone();
        newSelectionInfo.content = content;

        return null;
    }

}
