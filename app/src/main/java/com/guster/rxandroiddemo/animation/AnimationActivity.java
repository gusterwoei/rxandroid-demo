package com.guster.rxandroiddemo.animation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.guster.rxandroiddemo.BaseActivity;
import com.guster.rxandroiddemo.R;
import com.guster.rxandroiddemo.Util;

import butterknife.BindView;

public class AnimationActivity extends BaseActivity {

    @BindView(R.id.lyt_root)
    protected View lytRoot;

    @BindView(R.id.img_logo)
    protected View imglogo;

    @BindView(R.id.txt_text1)
    protected View txtText1;

    @BindView(R.id.txt_text2)
    protected View txtText2;

    @BindView(R.id.txt_text3)
    protected View txtText3;

    @BindView(R.id.lyt_footer_content)
    protected View lytFooterContent;

    @BindView(R.id.txt_apply_text)
    protected View txtApplyText;

    @BindView(R.id.btn_apply)
    protected View btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_animation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_reset:
                Util.logd("resetting");
                initViewPositions();
                break;
            case R.id.action_start:
                Util.logd("start animation");
                doAnimation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        initViewPositions();
    }

    private void initViewPositions() {
        lytRoot.setAlpha(0);
        lytRoot.setTranslationY(300f);

        imglogo.setAlpha(0);
        imglogo.setScaleX(1.5f);
        imglogo.setScaleY(1.5f);

        txtText1.setTranslationX(-800f);
        txtText2.setTranslationX(-800f);
        txtText3.setTranslationX(-800f);

        lytFooterContent.setAlpha(0);
        lytFooterContent.setTranslationY(300f);

        txtApplyText.setAlpha(0);

        btnApply.setAlpha(0);
    }

    private void doAnimation() {
        final int duration = 700;
        ObjectAnimator translateLytRoot = ObjectAnimator.ofFloat(lytRoot, "y", 0f).setDuration(duration);
        ObjectAnimator fadeInLytRoot = ObjectAnimator.ofFloat(lytRoot, "alpha", 1).setDuration(duration);

        ObjectAnimator fadeInImgLogo = ObjectAnimator.ofFloat(imglogo, "alpha", 1).setDuration(duration);
        ObjectAnimator scaleXImgLogo = ObjectAnimator.ofFloat(imglogo, "scaleX", 1).setDuration(duration);
        ObjectAnimator scaleYImgLogo = ObjectAnimator.ofFloat(imglogo, "scaleY", 1).setDuration(duration);

        ObjectAnimator translateTxtText1 = ObjectAnimator.ofFloat(txtText1, "x", 0).setDuration(duration);
        ObjectAnimator translateTxtText2 = ObjectAnimator.ofFloat(txtText2, "x", 0).setDuration(duration);
        ObjectAnimator translateTxtText3 = ObjectAnimator.ofFloat(txtText3, "x", 0).setDuration(duration);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translateLytRoot).with(fadeInLytRoot);
        animatorSet.play(fadeInImgLogo).with(scaleXImgLogo).with(scaleYImgLogo).after(200);
        //animatorSet.playSequentially(translateTxtText1, translateTxtText2, translateTxtText3);
        animatorSet.play(translateTxtText1).before(translateTxtText2).after(300);
        animatorSet.play(translateTxtText2).before(translateTxtText3).after(500);
        animatorSet.start();
    }
}
