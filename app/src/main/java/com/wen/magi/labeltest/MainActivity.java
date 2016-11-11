package com.wen.magi.labeltest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wen.magi.labeltest.widgets.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static int WIDTH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logScreenWidth();
        initFlowLayout();
    }

    private void initFlowLayout() {
        TagFlowLayout flowLayout = new TagFlowLayout(this);
        ((LinearLayout) findViewById(R.id.activity_main)).addView(flowLayout);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.topic_index_related_topic_left_margin);
        flowLayout.setLayoutParams(params);
        initLabelTitleView(flowLayout);
    }

    private List<TextView> labelViews = new ArrayList<>();
    private List<Integer> widths = new ArrayList<>();

    private void initLabelTitleView(final TagFlowLayout flowLayout) {
        List<String> text = getTextList();
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.topic_index_related_topic_left_margin);
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.topic_index_related_topic_left_margin);

        for (int i = 0; i < text.size(); i++) {

            TextView v = (TextView) View.inflate(this, R.layout.widget_label, null);
            v.setText(text.get(i));
            v.setLayoutParams(params);

            labelViews.add(v);
        }

        for (TextView labelView : labelViews) {
            flowLayout.addView(labelView);
        }
        flowLayout.requestLayout();
    }



    private List<String> getTextList() {
        List<String> labels = new ArrayList<>();
        labels.add("对齐索引页的部分功能");
        labels.add("Android开发");
        labels.add("详细的");
        labels.add("开发实践");
        labels.add("刺入对手心脏让对方遵守制约");
        labels.add("第二篇");
        labels.add("全面的介绍一下 Toolbar 的使用");
        labels.add("过年前发了一篇介绍");
        labels.add("Translucent System Bar 的最佳实践");
        labels.add("很多开发者");
        labels.add("关注和反馈说说说");
        labels.add("追踪 测谎 平时是酷拉皮卡用的最多的链");
        labels.add("全面的介绍一下 Toolbar");
        labels.add("过年前发了一篇介绍");
        labels.add("Translucent System");
        labels.add("很多开发者");
        labels.add("关注和反馈说");
        labels.add("今天开始");
        labels.add("用笔");
        labels.add("写");
        labels.add("写");
        labels.add("Toolbar 是在 Android 5.0 开始推出的一个 Material Design 风格的导航控件");
        labels.add("对齐索引页的部分功能开始推出的一个");
        labels.add("瞬间治愈 但治愈时防御力锐减");
        labels.add("酷拉皮卡");
        labels.add("漫画全职猎人主角");
        labels.add("富坚");
        labels.add("第二篇");
        labels.add("灭族凶手幻影旅团");
        labels.add("奇犽、雷欧力");
        labels.add("小杰");
        labels.add("感受锁链");
        labels.add("黑帮诺斯拉家族");
        labels.add("打倒成员窝金");
        labels.add("秘密");
        labels.add("古代文字");
        labels.add("中指的束缚之链：困缚——把敌人锁住");
        labels.add("很多开发者");
        labels.add("关注和反馈说");
        labels.add("窟卢塔族");
        labels.add("蜘蛛");
        labels.add("治愈之链");
        labels.add("追魂之链");
        labels.add("Toolbar 是在 Android 5.0 开始推出的一个 Material Design 风格的导航控件");
        return labels;
    }
    /**
     * 输出屏幕宽度
     */
    private void logScreenWidth(){
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            WIDTH = display.getWidth();
        } else {
            Point size = new Point();
            display.getSize(size);
            WIDTH = size.x;
        }


    }
}
