package com.wen.magi.labeltest;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 11-01-2016
 */

public class ViewUtils {

    /**
     * Remove a view from viewgroup.
     *
     * @param o can be null
     */
    public static void removeFromSuperView(Object o) throws Exception {
        if (o == null)
            return;

        if (o instanceof View) {
            View view = (View) o;
            final ViewParent parent = view.getParent();
            if (parent == null)
                return;
            if (parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            } else
                throw new Exception("the parent of view is not a viewgroup");
        } else if (o instanceof Dialog) {
            Dialog dialog = (Dialog) o;
            dialog.hide();
        }
    }
}
