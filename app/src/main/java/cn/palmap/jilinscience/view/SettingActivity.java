package cn.palmap.jilinscience.view;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;

import cn.palmap.jilinscience.App;
import cn.palmap.jilinscience.R;
import cn.palmap.jilinscience.model.User;
import cn.palmap.jilinscience.utils.DeviceUtils;

/**
 * Created by stone on 2017/5/16.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mNotification;
    private TextView mResetPassword;
    private TextView mExitApplication;
    private PopupWindow pop;
    private LinearLayout ll_popup;
    private LinearLayout ll_exit_app;
    private User mUser;
    private Intent mExitIntent;
    private ImageView mImageBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        App.getInstance().addActivity(this);
        mUser = App.getInstance().getUser();
        initView();
        initEvent();
    }

    private void initEvent() {
        mNotification.setOnClickListener(this);
        mExitApplication.setOnClickListener(this);
        mResetPassword.setOnClickListener(this);
        mImageBack.setOnClickListener(this);
    }

    private void initView() {
        mNotification = (TextView) findViewById(R.id.tv_setting_notification);
        mResetPassword = (TextView) findViewById(R.id.tv_setting_resetpsw);
        mExitApplication = (TextView) findViewById(R.id.tv_setting_exitapp);
        mImageBack = (ImageView) findViewById(R.id.imageBack);
        if (mUser == null) {
            mResetPassword.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_setting_exitapp:
                showExitWindow();
                ll_popup.startAnimation(AnimationUtils.loadAnimation(
                        SettingActivity.this, R.anim.activity_translate_in));
                pop.showAtLocation(v, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.tv_setting_notification:
                startActivity(new Intent(SettingActivity.this,NotificationActivity.class));
                break;
            case R.id.tv_setting_resetpsw:
                startActivity(new Intent(SettingActivity.this,ResetPswActivity.class));
                break;
            case R.id.imageBack:
                finish();
                break;
        }
    }

    private void deletePersistFile() {
        File file = new File(SettingActivity.this.getExternalCacheDir().getPath()+"/user.txt");
        if (file != null) {
            file.delete();
        }
    }

    public void showExitWindow() {
        pop = new PopupWindow(SettingActivity.this);
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
                null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        ll_exit_app = (LinearLayout) view.findViewById(R.id.ll_exit_app);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
        TextView bt1 = (TextView) view.findViewById(R.id.item_popupwindows_camera);
        bt1.setText("退出账号");
        TextView bt2 = (TextView) view.findViewById(R.id.item_popupwindows_Photo);
        bt2.setText("关闭程序");
        TextView bt3 = (TextView) view.findViewById(R.id.item_popupwindows_cancel);
        if (mUser == null) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll_exit_app.getLayoutParams();
            layoutParams.height = DeviceUtils.dip2px(this,44);
            ll_exit_app.setLayoutParams(layoutParams);
            bt1.setVisibility(View.GONE);
        }
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deletePersistFile();
                finish();
                App.getInstance().setUser(null);
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                App.getInstance().onTerminate();
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
    }
}
