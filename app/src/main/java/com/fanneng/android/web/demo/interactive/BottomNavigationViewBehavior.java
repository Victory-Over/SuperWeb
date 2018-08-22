package com.fanneng.android.web.demo.interactive;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * 与toolbar联动隐藏底部菜单
 */
public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<View> {
    public BottomNavigationViewBehavior() {
    }

    public BottomNavigationViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        ((CoordinatorLayout.LayoutParams) child.getLayoutParams()).topMargin = parent
                .getMeasuredHeight() - child.getMeasuredHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //得到依赖View的滑动距离
        int top = ((AppBarLayout.Behavior) ((CoordinatorLayout.LayoutParams) dependency
                .getLayoutParams()).getBehavior()).getTopAndBottomOffset();
        //因为BottomNavigation的滑动与ToolBar是反向的，所以取负值
        ViewCompat.setTranslationY(child, -(top * child.getMeasuredHeight() / dependency
                .getMeasuredHeight()));
        return false;
    }
}
