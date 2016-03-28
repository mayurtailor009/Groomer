package com.groomer.recyclerviewitemclick;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
    GestureDetector gestureDetector;
    MyOnClickListener myOnClickListener;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView,
                                 MyOnClickListener onClickListener) {
        this.myOnClickListener = onClickListener;
        gestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
                        if (view != null && myOnClickListener != null) {
                            myOnClickListener.onItemClick(view,
                                    recyclerView.getChildPosition(view));
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        gestureDetector.onTouchEvent(e);
        return false;

    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
