package com.ylfcf.ppp.ui;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.db.DBGesturePwdManager;
import com.ylfcf.ppp.entity.GesturePwdEntity;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.Util;
import com.ylfcf.ppp.widget.GestureContentView;
import com.ylfcf.ppp.widget.GestureDrawline.GestureCallBack;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 手势绘制/校验界面
 *
 */
public class GestureVerifyActivity extends Activity implements android.view.View.OnClickListener{
	/** 手机号码*/
	public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
	/** 意图 */
	public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
	private ImageView mImgUserLogo;
	private TextView mTextPhoneNumber;
	private TextView mTextTip;
	private FrameLayout mGestureContainer;
	private GestureContentView mGestureContentView;
	private TextView mTextForget;
	private String mParamPhoneNumber;
	private long mExitTime = 0;
	private int mParamIntentCode;
	private int startCount = 5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesture_verify_activity);
		ObtainExtraData();
		setUpViews();
		setUpListeners();
	}
	
	private void ObtainExtraData() {
		mParamPhoneNumber = getIntent().getStringExtra(PARAM_PHONE_NUMBER);
		mParamIntentCode = getIntent().getIntExtra(PARAM_INTENT_CODE, 0);
	}
	
	private void setUpViews() {
		mImgUserLogo = (ImageView) findViewById(R.id.user_logo);
		mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
		mTextTip = (TextView) findViewById(R.id.text_tip);
		mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
		mTextForget = (TextView) findViewById(R.id.text_forget_gesture);
		mTextPhoneNumber.setText(Util.hidPhoneNum(SettingsManager.getUser(getApplicationContext())));
		
		// 初始化一个显示各个点的viewGroup
		String userId = SettingsManager.getUserId(getApplicationContext());
		GesturePwdEntity entity = DBGesturePwdManager.getInstance(getApplicationContext()).getGesturePwdEntity(userId);
		String gesturePwd = null;
		if(entity != null)
		gesturePwd = entity.getPwd();
		mGestureContentView = new GestureContentView(this, true, gesturePwd,
				new GestureCallBack() {
					@Override
					public void onGestureCodeInput(String inputCode) {
					}

					@Override
					public void checkedSuccess() {
						mGestureContentView.clearDrawlineState(0L);
						GestureVerifyActivity.this.finish();
						Intent intent = new Intent(GestureVerifyActivity.this,MainFragmentActivity.class);
						startActivity(intent);
					}

					@Override
					public void checkedFail() {
						--startCount;
						mGestureContentView.clearDrawlineState(300L);
						mTextTip.setVisibility(View.VISIBLE);
						mTextTip.setText("密码错误，还可以输入"+startCount+"次");
						// 左右移动动画
						Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this, R.anim.shake);
						mTextTip.startAnimation(shakeAnimation);
						if(startCount == 0){
							showDialog("手势密码失效，需重新登录。");
						}
					}
				});
		// 设置手势解锁显示到哪个布局里面
		mGestureContentView.setParentView(mGestureContainer);
	}
	
	private void setUpListeners() {
		mTextForget.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_forget_gesture:
			showDialog("忘记手势密码，需重新登录。");
			break;
		default:
			break;
		}
	}
	
	private void showDialog(String content){
		AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器  
        builder.setTitle("提示"); //设置标题  
        builder.setMessage(content); //设置内容  
        builder.setCancelable(false);
//        builder.setIcon(R.drawable.ic_launcher);//设置图标，图片id即可  
        builder.setPositiveButton("重新登录", new DialogInterface.OnClickListener() { //设置确定按钮  
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss(); //关闭dialog  
                GesturePwdEntity entity = new GesturePwdEntity();
        		String userId = SettingsManager.getUserId(getApplicationContext());
        		String phone = SettingsManager.getUser(getApplicationContext());
        		entity.setUserId(userId);
        		entity.setPhone(phone);
        		entity.setStatus("0");
        		entity.setPwd("");
                SettingsManager.setUser(GestureVerifyActivity.this,"");
        		SettingsManager.setLoginPassword(GestureVerifyActivity.this,"",true);
        		SettingsManager.setUserId(GestureVerifyActivity.this,"");
        		SettingsManager.setUserName(GestureVerifyActivity.this,"");
        		DBGesturePwdManager.getInstance(getApplicationContext()).updateGestureEntity(entity);
                Intent intent = new Intent(GestureVerifyActivity.this,LoginActivity.class);
                intent.putExtra("FLAG", "from_gesture_verify_activity");
                startActivity(intent);
                finish();
            }  
        });  
  
        //参数都设置完成了，创建并显示出来  
        builder.create().show();  
	}
}
