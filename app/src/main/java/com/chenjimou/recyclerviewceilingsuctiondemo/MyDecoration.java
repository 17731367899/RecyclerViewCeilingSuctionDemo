package com.chenjimou.recyclerviewceilingsuctiondemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyDecoration extends RecyclerView.ItemDecoration {

    // 每组标题的高度（即每个item的高度）
    private final int groupHeadHeight;
    // 标题背景的画笔
    private final Paint backgroundPaint;
    // 标题文字的画笔
    private final Paint textPaint;
    private final Rect textRect;

    private static final String TAG = "MyDecoration";

    public MyDecoration(Context context) {
        groupHeadHeight = DisplayUtils.dp2px(context, 110);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.BLACK);

        textPaint = new Paint();
        textPaint.setTextSize(50);
        textPaint.setColor(Color.WHITE);

        textRect = new Rect();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        // 判断adapter的类型
        if (parent.getAdapter() instanceof MyAdapter){
            MyAdapter adapter = (MyAdapter) parent.getAdapter();
            // 获取当前item相对于整个RecyclerView的布局位置
            int position = parent.getChildLayoutPosition(view);
            // 判断当前item是否是该组的第一个item
            if (adapter.isGroupFirstItem(position)) {
                // 如果是该组的第一个item，预留更大的地方
                outRect.set(0, groupHeadHeight, 0, 0);
            } else {
                // 1像素分割线
                outRect.set(0, 1, 0, 0);
            }
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent,
                       @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        // 判断adapter的类型
        if (parent.getAdapter() instanceof MyAdapter) {
            MyAdapter adapter = (MyAdapter) parent.getAdapter();
            // 当前屏幕内的item个数
            int count = parent.getChildCount();
            // RecyclerView左padding的像素值（即item最左边的X坐标值）
            int left = parent.getPaddingLeft();
            // item最右边的X坐标值 = 整个RecyclerView的宽度 - RecyclerView右padding的像素值
            int right = parent.getWidth() - parent.getPaddingRight();
            for (int i = 0; i < count; i++) {
                View view = parent.getChildAt(i);
                // 获取当前item相对于整个RecyclerView的布局位置
                int position = parent.getChildLayoutPosition(view);
                // 标题最上边的Y坐标 = 当前item的最上边到RecyclerView顶部的距离 - 每组标题的高度
                int top = view.getTop() - groupHeadHeight;
                // 标题最下边的Y坐标 = 当前item的最上边到RecyclerView顶部的距离
                int bottom = view.getTop();
                // 判断标题的最上边到RecyclerView的顶部是否还有距离（防止onDraw在padding中进行绘制）
                boolean isHaveDistance = top - parent.getPaddingTop() >= 0;
                if (isHaveDistance){
                    // 判断当前item是否是该组的第一个item
                    if (adapter.isGroupFirstItem(position)) {
                        // 绘制背景
                        canvas.drawRect(left, top, right, bottom, backgroundPaint);
                        // 获取组名
                        String groupName = adapter.getGroupName(position);
                        // 绘制文字
                        textPaint.getTextBounds(groupName, 0, groupName.length(), textRect);
                        canvas.drawText(groupName, left + 20,
                                bottom - groupHeadHeight / 2 - (textPaint.descent()/2 + textPaint.ascent()/2), textPaint);
                    } else {
                        // 绘制分隔线
                        canvas.drawRect(left, bottom - 1, right, bottom, backgroundPaint);
                    }
                }
            }
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent,
                           @NonNull RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        if (parent.getAdapter() instanceof MyAdapter) {
            MyAdapter adapter = (MyAdapter) parent.getAdapter();
            // 获取可见区域内第一个item相对于整个RecyclerView的适配器位置
            int position = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
            // 获取可见区域内第一个item的View
            View firstVisibleItemView = parent.findViewHolderForAdapterPosition(position).itemView;
            // RecyclerView左padding的像素值（即item最左边的X坐标值）
            int left = parent.getPaddingLeft();
            // item最右边的X坐标值 = 整个RecyclerView的宽度 - RecyclerView右padding的像素值
            int right = parent.getWidth() - parent.getPaddingRight();
            // 标题最上边的Y坐标（即RecyclerView上padding的像素值，实现标题在滑动时的吸顶）
            int top = parent.getPaddingTop();
            // 判断下一个item是否是另一组的第一个item
            // （当可见区域内第一个item的下一个item是另一组的第一个item时，即下一组的标题与上一组的标题相遇时，
            // 继续滑动，下一组的标题将把上一组的标题推上去）
            if (adapter.isGroupFirstItem(position + 1)) {
                // 标题最下边的Y坐标 = top + 可见区域内第一个item的最底边到RecyclerView顶部的距离
                // （当向上滑动时，可见区域内第一个item的最底边到RecyclerView顶部的距离，随滑动而减小）
                int bottom = top + firstVisibleItemView.getBottom() - parent.getPaddingTop();
                // 绘制背景
                canvas.drawRect(left, top, right, bottom, backgroundPaint);
                // 获取组名
                String groupName = adapter.getGroupName(position);
                // 绘制文字
                canvas.save();
                // 裁剪画布，防止文字在padding中进行绘制
                canvas.clipRect(left, top, right, bottom);
                textPaint.getTextBounds(groupName, 0, groupName.length(), textRect);
                canvas.drawText(groupName, left + 20,
                        bottom - groupHeadHeight / 2 - (textPaint.descent()/2 + textPaint.ascent()/2), textPaint);
                canvas.restore();
            } else {
                // 绘制背景
                canvas.drawRect(left, top, right, top + groupHeadHeight, backgroundPaint);
                // 获取组名
                String groupName = adapter.getGroupName(position);
                // 绘制文字
                textPaint.getTextBounds(groupName, 0, groupName.length(), textRect);
                canvas.drawText(groupName, left + 20,
                        top + groupHeadHeight / 2 - (textPaint.descent()/2 + textPaint.ascent()/2), textPaint);
            }
        }
    }
}
