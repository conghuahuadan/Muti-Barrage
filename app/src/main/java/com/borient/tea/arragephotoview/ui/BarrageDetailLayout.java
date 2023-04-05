package com.borient.tea.arragephotoview.ui;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.borient.tea.arragephotoview.R;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;

public class BarrageDetailLayout extends FrameLayout {

    private Handler handler = new Handler();
    private View barrageDetail;
    private AnimOutTask animOutTask;

    public BarrageDetailLayout(@NonNull Context context) {
        this(context, null);
    }

    public BarrageDetailLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarrageDetailLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_barrage_detail, this, false);
        addView(view);

        barrageDetail = findViewById(R.id.barrage_detail);
    }

    private void animInBarrage(final View itemView) {
        int offsetY = (barrageDetail.getHeight() - itemView.getHeight()) / 2;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) barrageDetail.getLayoutParams();
        params.topMargin = itemView.getTop() - offsetY;
        barrageDetail.requestLayout();
        int itemViewMiddle = (itemView.getLeft() + itemView.getRight()) / 2;
        int parentMiddle = (getLeft() + getRight()) / 2;
        barrageDetail.setTranslationX(itemViewMiddle - parentMiddle);
        itemView.setVisibility(View.VISIBLE);
        ViewAnimator.animate(itemView)
                .alpha(1, 0)
                .scale(1, 0)
                .duration(200)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        itemView.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
        barrageDetail.setVisibility(View.VISIBLE);
        ViewAnimator.animate(barrageDetail)
                .alpha(0, 1)
                .scale(0, 1)
                .translationX(barrageDetail.getTranslationX(), 0)
                .duration(450)
                .start();
    }

    private void animOutBarrage(final View itemView) {
        int itemViewMiddle = (itemView.getLeft() + itemView.getRight()) / 2;
        int parentMiddle = (getLeft() + getRight()) / 2;
        barrageDetail.setVisibility(View.VISIBLE);
        ViewAnimator.animate(barrageDetail)
                .alpha(1, 0)
                .scale(1, 0)
                .translationX(0, itemViewMiddle - parentMiddle)
                .duration(450)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        itemView.setVisibility(View.VISIBLE);
                        ViewAnimator.animate(itemView)
                                .alpha(0, 1)
                                .scale(0, 1)
                                .duration(200)
                                .start();
                    }
                })
                .start();
        animOutTask = null;
    }

    private class AnimOutTask implements Runnable {

        View itemView;

        public AnimOutTask(View itemView) {
            this.itemView = itemView;
        }

        @Override
        public void run() {
            animOutBarrage(itemView);
        }

        public void cancel() {
            ViewAnimator.animate(itemView)
                    .alpha(0, 1)
                    .scale(0, 1)
                    .duration(200)
                    .start();
            itemView.setVisibility(View.VISIBLE);
            ViewAnimator.animate(barrageDetail)
                    .alpha(1, 0)
                    .scale(1, 0)
                    .duration(450)
                    .start();
        }
    }

    public void showDetail(View itemView) {
        if (barrageDetail != null) {
            animInBarrage(itemView);
            if (animOutTask != null) {
                handler.removeCallbacks(animOutTask);
                animOutTask.cancel();
            }
            animOutTask = new AnimOutTask(itemView);
            handler.removeCallbacks(animOutTask);
            handler.postDelayed(animOutTask, 5 * 1000);
        }
    }

    public void hideDetail() {
        if (animOutTask != null) {
            handler.removeCallbacks(animOutTask);
            animOutTask.cancel();
            animOutTask = null;
        }
    }
}
