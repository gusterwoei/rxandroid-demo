package com.guster.rxandroiddemo.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.guster.rxandroiddemo.BaseActivity;
import com.guster.rxandroiddemo.R;
import com.guster.rxandroiddemo.Util;

import java.util.Random;

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

    @BindView(R.id.lyt_footer_layer1)
    protected View lytFooterLayer1;

    @BindView(R.id.lyt_footer_layer2)
    protected View lytFooterLayer2;

    @BindView(R.id.view_rain)
    protected View viewRain;

    @BindView(R.id.txt_apply_text)
    protected View txtApplyText;

    @BindView(R.id.btn_apply)
    protected View btnApply;

    private boolean isAnimating;

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
                initViewPositions();
                break;
            case R.id.action_start:
                if(isAnimating) {
                    Snackbar.make(lytRoot, "Scene is currently animating, please try again later.", Snackbar.LENGTH_LONG).show();
                    return true;
                }
                initViewPositions();
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

        txtText1.setTranslationX(-500f);
        txtText2.setTranslationX(-700f);
        txtText3.setTranslationX(-800f);

        lytFooterContent.setTranslationY(500f);
        lytFooterLayer1.setTranslationY(550f);
        lytFooterLayer2.setTranslationY(600f);

        txtApplyText.setAlpha(0);

        btnApply.setAlpha(0);

        viewRain.setTranslationX(0);
        viewRain.setTranslationY(-200f);

        viewRain.setRotation(viewRain.getRotation()-viewRain.getRotation());
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
        AnimatorSet textAnimSet = new AnimatorSet();
        textAnimSet.play(translateTxtText2).before(translateTxtText3);

        ObjectAnimator translateLytFooterContent = ObjectAnimator.ofFloat(lytFooterContent, "y", lytFooterContent.getY() - lytFooterContent.getTranslationY()).setDuration(1500);
        ObjectAnimator translateFooterLayer1 = ObjectAnimator.ofFloat(lytFooterLayer1, "y", lytFooterLayer1.getY() - lytFooterLayer1.getTranslationY()).setDuration(1500);
        ObjectAnimator translateFooterLayer2 = ObjectAnimator.ofFloat(lytFooterLayer2, "y", lytFooterLayer2.getY() - lytFooterLayer2.getTranslationY()).setDuration(1500);
        ObjectAnimator fadeInTxtApplyText = ObjectAnimator.ofFloat(txtApplyText, "alpha", 1).setDuration(duration);
        ObjectAnimator fadeInBtnApply = ObjectAnimator.ofFloat(btnApply, "alpha", 1).setDuration(duration);
        fadeInTxtApplyText.setStartDelay(1500);
        fadeInBtnApply.setStartDelay(2000);
        translateFooterLayer1.setStartDelay(500);
        translateFooterLayer2.setStartDelay(1000);

        // let the rain fall down on me
        float[] yvals = new float[15];
        float[] xvals = new float[15];
        float x = viewRain.getX();
        float y = viewRain.getY();
        Random rnd = new Random();
        for(int i=0; i<yvals.length; i++) {
            x = ((100 + rnd.nextInt(50)) * (rnd.nextBoolean() ? 1 : -1)) + x;
            y = (100 + rnd.nextInt(100)) + y;
            xvals[i] = x;
            yvals[i] = y;
        }
        ObjectAnimator rainFallX = ObjectAnimator.ofFloat(viewRain, "x", xvals).setDuration(10000);
        ObjectAnimator rainFallY = ObjectAnimator.ofFloat(viewRain, "y", yvals).setDuration(10000);
        ObjectAnimator rainRotate = ObjectAnimator.ofFloat(viewRain, "rotation", 360).setDuration(7000);
        //rainRotate.setRepeatCount(ObjectAnimator.INFINITE);
        //rainRotate.setRepeatMode(ObjectAnimator.RESTART);
        rainFallY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                Util.logd("start");
                isAnimating = true;
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                Util.logd("end");
                isAnimating = false;
            }
            @Override
            public void onAnimationCancel(Animator animator) {
                Util.logd("canceled");
            }
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translateLytRoot).with(fadeInLytRoot);
        animatorSet.play(fadeInImgLogo).with(scaleXImgLogo).with(scaleYImgLogo).after(500);
        animatorSet.play(translateTxtText1).before(textAnimSet).after(1000);
        animatorSet.play(translateLytFooterContent).with(translateFooterLayer1).with(translateFooterLayer2).with(fadeInTxtApplyText).with(fadeInBtnApply).after(2000);
        animatorSet.play(rainFallY).with(rainFallX).with(rainRotate).after(700);
        animatorSet.start();
    }
}
