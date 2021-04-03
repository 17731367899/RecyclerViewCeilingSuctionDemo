package com.chenjimou.recyclerviewceilingsuctiondemo;

import android.content.Context;

public class DisplayUtils {
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale * 0.5f);
    }
    public static int sp2px(Context context, float spValue)
    {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5f);
    }
}
