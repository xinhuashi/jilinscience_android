package cn.palmap.jilinscience.view;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import cn.palmap.jilinscience.R;
import cn.palmap.jilinscience.base.BaseActivity;
import cn.palmap.jilinscience.base.FragmentTabController;
import cn.palmap.jilinscience.di.component.DaggerViewComponent;
import cn.palmap.jilinscience.di.module.MainModule;
import cn.palmap.jilinscience.utils.ActivityUtils;
import cn.palmap.jilinscience.view.fragment.HomePageFragment;

import static cn.palmap.jilinscience.di.module.MainModule.mainFragmentTabController;

public class MainActivity extends BaseActivity implements FragmentTabController.onTabChangedListener {

    @Inject @Named(mainFragmentTabController) FragmentTabController fragmentTabController;

    @BindView(R.id.layoutTabHome) ViewGroup layoutTabHome;
    @BindView(R.id.layoutTabMine) ViewGroup layoutTabMine;
    @BindView(R.id.layoutTab) ViewGroup layoutTab;

    public static final int CODE_LOGIN = 1000;

    @OnClick({
            R.id.layoutTabHome,
            R.id.layoutTabMine
    })
    public void onTabClick(View v){
        switch (v.getId()) {
            case R.id.layoutTabHome:
                fragmentTabController.changeTab(0);
                break;
            case R.id.layoutTabMine:
                fragmentTabController.changeTab(1);
                break;
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterOnCreate() {
        ActivityUtils.noTitle(this);
        ActivityUtils.noBackground(this);
    }

    @Override
    protected void inject() {
        DaggerViewComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .mainModule(new MainModule(this, R.id.fragmentContainer))
                .build().inject(this);
    }

    @Override
    protected void onInjected() {
        fragmentTabController.initTab();
        fragmentTabController.setOnTabChangedListener(this);
        layoutTabHome.setSelected(true);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)
                && 0 == fragmentTabController.getCurrentTabPosition()
                && ((HomePageFragment) fragmentTabController.getCurrentFragment()).canGoBack()) {
            ((HomePageFragment) fragmentTabController.getCurrentFragment()).goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTabChanged(int oldPosition, int newPosition) {
        layoutTabHome.setSelected(newPosition == 0);
        layoutTabMine.setSelected(newPosition == 1);
    }

    public void hideTab(){
        layoutTab.setVisibility(View.GONE);
    }

    public void showTab(){
        layoutTab.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_LOGIN) {
            fragmentTabController.getCurrentFragment().onActivityResult(requestCode, resultCode, data);
        }
    }
}