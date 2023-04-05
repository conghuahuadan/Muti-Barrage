package com.borient.tea.arragephotoview.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.borient.tea.arragephotoview.R;
import com.borient.tea.arragephotoview.data.BarrageData;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.orient.tea.barragephoto.adapter.AdapterListener;
import com.orient.tea.barragephoto.adapter.BarrageAdapter;
import com.orient.tea.barragephoto.ui.BarrageView;

@SuppressWarnings("ALL")
public class MutiBarrageActivity extends AppCompatActivity {

    private static final String TAG = MutiBarrageActivity.class.getSimpleName();

    private String text[] = {"666666666666666", "大盖伦无敌！", "这波操作不亏，50血极限反杀，我们还有机会！"
            , "雷瑟守备钻石守门员求带～", "反向操作！！！！！", "谢谢金克丝送的一发火箭"};

    private String name[] = {"逍遥子送了一架飞机", "盐城小王送了一辆UFO", "无敌的VN送了一辆宝马", "快乐的皮皮虾送了一发火箭"};

    private final int ICON_RESOURCES[] = {R.drawable.plane, R.drawable.ufo, R.drawable.car
            , R.drawable.rocket};

    private Button btnTest;
    private BarrageView barrageView;
    private FrameLayout barrageDetailContainer;
    private BarrageAdapter<BarrageData> mAdapter;
    private View barrageDetail;
    private Handler handler = new Handler();
    private BarrageDetailLayout detailLayout;

    public static void show(Context context) {
        Intent intent = new Intent(context, MutiBarrageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti_barrage);

        btnTest = findViewById(R.id.btn_test);
        barrageView = findViewById(R.id.barrage);
        detailLayout = findViewById(R.id.barrage_detail_layout);

        initBarrage();

        barrageDetail = findViewById(R.id.barrage_detail);

        barrageView.setCallback(new BarrageView.Callback() {
            @Override
            public void onDispatchTouchEvent(MotionEvent ev) {
                detailLayout.hideDetail();
            }
        });
    }

    public void onTestClick(View view) {
        if (barrageView != null) {
            barrageView.pause();
        }
    }

    public void onPauseClick(View view) {
        if (barrageView != null) {
            barrageView.pause();
        }
    }

    public void onResumeClick(View view) {
        if (barrageView != null) {
            barrageView.resume();
        }
    }

    private void initBarrage() {
        BarrageView.Options options = new BarrageView.Options()
                .setGravity(BarrageView.GRAVITY_TOP)                // 设置弹幕的位置
                .setInterval(70)                                     // 设置弹幕的发送间隔
                .setModel(BarrageView.MODEL_COLLISION_DETECTION)     // 设置弹幕生成模式
                .setClick(true);                                     // 设置弹幕是否可以点击
        barrageView.setOptions(options);
        // 设置适配器 第一个参数是点击事件的监听器
        barrageView.setAdapter(mAdapter = new BarrageAdapter<BarrageData>(null, this) {
            @Override
            public BarrageViewHolder<BarrageData> onCreateViewHolder(View root, int type) {
                switch (type) {
                    case R.layout.barrage_item_text:
                    case R.layout.barrage_item_text_vip:
                        return new TextViewHolder(root);
                    default:
                        return new BigHolder(root);
                }

            }

            @Override
            public int getItemLayout(BarrageData barrageData) {
                switch (barrageData.getType()) {
                    case 0:
                        return R.layout.barrage_item_text;
                    case 1:
                        return R.layout.barrage_item_text_vip;
                    default:
                        return R.layout.barrage_item_big;
                }
            }
        });
        // 设置监听器
        mAdapter.setAdapterListener(new AdapterListener<BarrageData>() {
            @Override
            public void onItemClick(final BarrageAdapter.BarrageViewHolder<BarrageData> holder, BarrageData item) {
                Toast.makeText(MutiBarrageActivity.this, item.getContent() + "点击了一次", Toast.LENGTH_SHORT).show();
                View itemView = holder.getItemView();
                detailLayout.showDetail(itemView);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        initData();
    }

    private void initData() {
        int strLength = text.length;
        for (int i = 0; i < 50; i++) {
            if (i == 0 || i % 9 != 0)
                if (i % 4 != 0) {
                    mAdapter.add(new BarrageData(text[i % 6], 0, i));
                } else {
                    mAdapter.add(new BarrageData(text[i % 6], 1, i));
                }
            else
                mAdapter.add(new BarrageData(name[i % name.length], 2, i));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        barrageView.destroy();
    }

    class TextViewHolder extends BarrageAdapter.BarrageViewHolder<BarrageData> {

        private TextView mContent;

        public TextViewHolder(View itemView) {
            super(itemView);

            mContent = itemView.findViewById(R.id.content);
        }

        @Override
        protected void onBind(BarrageData data) {
            mContent.setText(data.getContent());
        }
    }

    class BigHolder extends BarrageAdapter.BarrageViewHolder<BarrageData> {

        private ImageView mHeadView;
        private TextView mContent;

        public BigHolder(View itemView) {
            super(itemView);

            mHeadView = itemView.findViewById(R.id.image);
            mContent = itemView.findViewById(R.id.content);
        }

        @Override
        protected void onBind(BarrageData data) {
            mHeadView.setImageResource(ICON_RESOURCES[data.getPos() % ICON_RESOURCES.length]);
            mContent.setText(data.getContent());
        }
    }
}
