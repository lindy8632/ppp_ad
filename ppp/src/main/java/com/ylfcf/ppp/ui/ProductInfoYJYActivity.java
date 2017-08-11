package com.ylfcf.ppp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ylfcf.ppp.R;
import com.ylfcf.ppp.async.AsyncAsscociatedCompany;
import com.ylfcf.ppp.entity.AssociatedCompanyInfo;
import com.ylfcf.ppp.entity.AssociatedCompanyParentInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.BorrowType;
import com.ylfcf.ppp.entity.ProductInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter;
import com.ylfcf.ppp.util.Constants;
import com.ylfcf.ppp.util.RequestApis;
import com.ylfcf.ppp.util.SettingsManager;
import com.ylfcf.ppp.util.UMengStatistics;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 元聚盈项目介绍
 * Created by Administrator on 2017/7/26.
 */

public class ProductInfoYJYActivity extends BaseActivity implements
        View.OnClickListener {
    private static final String className = "ProductInfoYJYActivity";
    private static final int REFRESH_VIEW = 5700;

    private static final int REQUEST_ASSC_WHAT = 7321;
    private static final int REQUEST_ASSC_SUCCESS = 7322;
    private static final int REQUEST_ASSC_NODATA = 7323;

    private LinearLayout topLeftBtn;
    private TextView topTitleTV;

    //项目结构更改后的布局
    private TextView xmjsTV;//项目介绍
    private TextView jkfTV,jkfjsTV,//借款方
            tjfTV,tjfjsTV,//推荐方
            dbfTV,dbfjsTV;//担保方
    private LinearLayout tjfIntroLayout,tjfLayout;

    private View tjfLine1,tjfLine2;
    private TextView vipPromptText;//vip产品备注

    private Button investBtn;
    private ProjectInfo projectInfo;
    private ProductInfo productInfo;
    private AlertDialog.Builder builder = null; // 先得到构造器

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_ASSC_WHAT:
                    requestAssociatedCompany(projectInfo.getLoan_id(), projectInfo.getRecommend_id(), projectInfo.getGuarantee_id());
                    break;
                case REQUEST_ASSC_SUCCESS:
                    AssociatedCompanyParentInfo parentInfo = (AssociatedCompanyParentInfo) msg.obj;
                    initXMYSBData(parentInfo);
                    break;
                case REQUEST_ASSC_NODATA:

                    break;
                case REFRESH_VIEW:
                    Bundle bundle = (Bundle) msg.obj;
                    refreshView(bundle);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.borrowinfo_yjy_activity);
        builder = new AlertDialog.Builder(ProductInfoYJYActivity.this,
                R.style.Dialog_Transparent); // 先得到构造器
        Bundle bundle = getIntent().getBundleExtra("BUNDLE");
        if(bundle != null){
            productInfo = (ProductInfo) bundle.getSerializable("PRODUCT_INFO");
            projectInfo = (ProjectInfo) bundle.getSerializable("PROJECT_INFO");
        }
        findViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UMengStatistics.statisticsOnPageStart(className);//友盟统计页面跳转
        UMengStatistics.statisticsResume(this);//友盟统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        UMengStatistics.statisticsOnPageEnd(className);//友盟统计页面跳转
        UMengStatistics.statisticsPause(this);//友盟统计时长
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void findViews() {
        topLeftBtn = (LinearLayout) findViewById(R.id.common_topbar_left_layout);
        topLeftBtn.setOnClickListener(this);
        topTitleTV = (TextView) findViewById(R.id.common_page_title);
        topTitleTV.setText("项目介绍");

        xmjsTV = (TextView) findViewById(R.id.borrowinfo_yjy_activity_xmjs);
        jkfTV = (TextView) findViewById(R.id.zxd_xmysb_layout_jkf_text);
        jkfjsTV = (TextView) findViewById(R.id.zxd_xmysb_layout_jkfjs_text);
        tjfTV = (TextView) findViewById(R.id.zxd_xmysb_layout_tjf_text);
        tjfjsTV = (TextView) findViewById(R.id.zxd_xmysb_layout_tjfjs_text);
        dbfTV = (TextView) findViewById(R.id.zxd_xmysb_layout_dbf_text);
        dbfjsTV = (TextView) findViewById(R.id.zxd_xmysb_layout_dbfjs_text);
        vipPromptText = (TextView)findViewById(R.id.borrowinfo_yjy_activity_vipprompt);

        tjfIntroLayout = (LinearLayout) findViewById(R.id.zxd_xmysb_layout_tjf_intro_layout);
        tjfLayout = (LinearLayout) findViewById(R.id.zxd_xmysb_layout_tjf_layout);
        tjfLine1 = findViewById(R.id.zxd_xmysb_tjf_line1);
        tjfLine2 = findViewById(R.id.zxd_xmysb_tjf_line2);
        investBtn = (Button) findViewById(R.id.borrowinfo_yjy_activity_bidBtn);
        investBtn.setOnClickListener(this);
        if("2".equals(projectInfo.getType())){
            //这一类型的资产包不会显示推荐方
            tjfLayout.setVisibility(View.GONE);
            tjfIntroLayout.setVisibility(View.GONE);
            tjfLine1.setVisibility(View.GONE);
            tjfLine2.setVisibility(View.GONE);
        }else{
            tjfLayout.setVisibility(View.VISIBLE);
            tjfIntroLayout.setVisibility(View.VISIBLE);
            tjfLine1.setVisibility(View.VISIBLE);
            tjfLine2.setVisibility(View.VISIBLE);
        }
        if(productInfo != null && BorrowType.VIP.equals(productInfo.getBorrow_type())){
            vipPromptText.setVisibility(View.VISIBLE);
            vipPromptText.setText("*提示：\n1. 本产品不参与平台其他优惠活动。\n2.元立方金服拥有本产品的最终解释权。");
            if("30".equals(projectInfo.getId())){//新的资产包不再显示推荐方
                tjfLayout.setVisibility(View.GONE);
                tjfIntroLayout.setVisibility(View.GONE);
                tjfLine1.setVisibility(View.GONE);
                tjfLine2.setVisibility(View.GONE);
            }else{
                tjfLayout.setVisibility(View.VISIBLE);
                tjfIntroLayout.setVisibility(View.VISIBLE);
                tjfLine1.setVisibility(View.VISIBLE);
                tjfLine2.setVisibility(View.VISIBLE);
            }
        }else{
            vipPromptText.setVisibility(View.VISIBLE);
            vipPromptText.setText("*提示：\n* 元立方金服拥有本产品的最终解释权。");
        }
        if(productInfo != null){
            if("未满标".equals(productInfo.getMoney_status())){
                if(SettingsManager.checkActiveStatusBySysTime(productInfo.getAdd_time(),SettingsManager.yyyJIAXIStartTime,
                        SettingsManager.yyyJIAXIEndTime) == 0 && "元年鑫".equals(productInfo.getBorrow_type())&& Constants.UserType.USER_COMPANY.
                        equals(SettingsManager.getUserType(ProductInfoYJYActivity.this))){
                    investBtn.setEnabled(false);
                }else{
                    investBtn.setEnabled(true);
                }
                investBtn.setText("立即投资");
            }else{
                investBtn.setEnabled(false);
                investBtn.setText("投资已结束");
            }
        }
        handler.sendEmptyMessage(REQUEST_ASSC_WHAT);
        initData();
    }

    private void initData(){
        if (projectInfo != null) {
            new ImageLoadThread(projectInfo.getSummary()).start();
        }
    }

    /**
     * 刷新页面
     * @param bundle
     */
    private void refreshView(Bundle bundle){
        if(bundle == null)
            return;
        CharSequence htmlText = bundle.getCharSequence("HTML_TEXT");
        xmjsTV.setText(htmlText);
    }

    class ImageLoadThread extends Thread {
        private String htmlText1 = "";
        public ImageLoadThread(String htmlText){
            this.htmlText1 = htmlText;
        }

        @Override
        public void run() {
            /**
             * 要实现图片的显示需要使用Html.fromHtml的一个重构方法：public static Spanned fromHtml
             * (String source, Html.ImageGetterimageGetter, Html.TagHandler
             * tagHandler)其中Html.ImageGetter是一个接口，我们要实现此接口，在它的getDrawable
             * (String source)方法中返回图片的Drawable对象才可以。
             */
            Html.ImageGetter imageGetter = new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                    // TODO Auto-generated method stub
                    URL url;
                    Drawable drawable = null;
                    try {
                        url = new URL(source);
                        int[] screen = SettingsManager.getScreenDispaly(ProductInfoYJYActivity.this);
                        drawable = Drawable.createFromStream(url.openStream(),null);
                        if(drawable != null){
                            int imageIntrinsicWidth = drawable.getIntrinsicWidth();
                            float imageIntrinsicHeight = (float)drawable.getIntrinsicHeight();
                            int curImageHeight = (int) (screen[0]*(imageIntrinsicHeight/imageIntrinsicWidth));
                            drawable.setBounds(0, 0, screen[0],curImageHeight);//四个参数含义为左上角、右下角坐标确定的一个矩形，图片就在这个矩形范围内画出来
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return drawable;
                }
            };
            CharSequence htmlText = Html.fromHtml(htmlText1, imageGetter, null);
            Message msg = handler.obtainMessage(REFRESH_VIEW);
            Bundle bundle = new Bundle();
            bundle.putCharSequence("HTML_TEXT", htmlText);
            msg.obj = bundle;
            handler.sendMessage(msg);

        }
    }

    /**
     * 初始化项目要素表
     */
    private void initXMYSBData(AssociatedCompanyParentInfo parentInfo){
        AssociatedCompanyInfo loanInfo = parentInfo.getLoanInfo();
        AssociatedCompanyInfo recommendInfo = parentInfo.getRecommendInfo();
        AssociatedCompanyInfo guaranteeInfo = parentInfo.getGuaranteeInfo();

        jkfTV.setText(Html.fromHtml(loanInfo.getCompany_name()));
        jkfjsTV.setText(Html.fromHtml(loanInfo.getIntroduce().trim()));
        tjfTV.setText(Html.fromHtml(recommendInfo.getCompany_name().trim()));
        tjfjsTV.setText(Html.fromHtml(recommendInfo.getIntroduce().trim()));
        dbfTV.setText(Html.fromHtml(guaranteeInfo.getCompany_name().trim()));
        dbfjsTV.setText(Html.fromHtml(guaranteeInfo.getIntroduce().trim()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_topbar_left_layout:
                finish();
                break;
            case R.id.borrowinfo_yjy_activity_bidBtn:
                // 从SettingsManager中读取密码，如果为空意味着没有登录。
                boolean isLogin = !SettingsManager.getLoginPassword(ProductInfoYJYActivity.this).isEmpty()
                        && !SettingsManager.getUser(ProductInfoYJYActivity.this).isEmpty();
                // isLogin = true;// 测试
                Intent intent = new Intent();
                // 1、检测是否已经登录
                if (isLogin) {
                    //判断是否实名绑卡
                    checkIsVerify();
                } else {
                    // 未登录，跳转到登录页面
                    intent.setClass(ProductInfoYJYActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 显示弹出框
     * @param type
     * @param msg
     */
    private void showMsgDialog(Context context, final String type, String msg){
        View contentView = LayoutInflater.from(context)
                .inflate(R.layout.borrow_details_msg_dialog, null);
        final Button sureBtn = (Button) contentView
                .findViewById(R.id.borrow_details_msg_dialog_surebtn);
        final TextView msgTV = (TextView) contentView
                .findViewById(R.id.borrow_details_msg_dialog_msg);
        final ImageView delBtn = (ImageView) contentView
                .findViewById(R.id.borrow_details_msg_dialog_delete);
        sureBtn.setVisibility(View.VISIBLE);
        msgTV.setText(msg);
        builder.setView(contentView);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if("实名认证".equals(type)){
                    intent.setClass(ProductInfoYJYActivity.this,UserVerifyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "元聚盈");
                    bundle.putSerializable("PRODUCT_INFO", productInfo);
                    intent.putExtra("bundle", bundle);
                    startActivity(intent);
                    investBtn.setEnabled(true);
                }
                dialog.dismiss();
            }
        });
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 参数都设置完成了，创建并显示出来
        dialog.show();
        // 设置dialog的宽度
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = display.getWidth() * 6 / 7;
        lp.height = display.getHeight()/3;
        dialog.getWindow().setAttributes(lp);
    }

    /**
     * 验证用户是否已经认证
     */
    private void checkIsVerify(){
        investBtn.setEnabled(false);
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        RequestApis.requestIsVerify(ProductInfoYJYActivity.this, SettingsManager.getUserId(getApplicationContext()), new Inter.OnIsVerifyListener() {
            @Override
            public void isVerify(boolean flag, Object object) {
                if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                    mLoadingDialog.dismiss();
                }
                Intent intent = new Intent();
                if(flag){
                    //用户已经实名，在这个页面只判断是否实名即可。不判断有没有绑卡
                    intent.setClass(ProductInfoYJYActivity.this, BidYJYActivity.class);
                    intent.putExtra("PRODUCT_INFO", productInfo);
                    investBtn.setEnabled(true);
                    startActivity(intent);
                    finish();
                }else{
                    //用户没有实名
                    showMsgDialog(ProductInfoYJYActivity.this, "实名认证", "请先实名认证！");
                }
            }
            @Override
            public void isSetWithdrawPwd(boolean flag, Object object) {
            }
        });
    }

    /**
     * 关联公司
     * @param loanId
     * @param recommendId
     * @param guaranteeId
     */
    private void requestAssociatedCompany(String loanId,String recommendId,String guaranteeId){
        if(mLoadingDialog != null){
            mLoadingDialog.show();
        }
        AsyncAsscociatedCompany task = new AsyncAsscociatedCompany(ProductInfoYJYActivity.this, loanId, recommendId, guaranteeId,
                new Inter.OnCommonInter(){
                    @Override
                    public void back(BaseInfo baseInfo) {
                        if(mLoadingDialog != null && mLoadingDialog.isShowing()){
                            mLoadingDialog.dismiss();
                        }
                        if(baseInfo != null){
                            int resultCode = SettingsManager.getResultCode(baseInfo);
                            if(resultCode == 0){
                                AssociatedCompanyParentInfo info = baseInfo.getAssociatedCompanyParentInfo();
                                Message msg = handler.obtainMessage(REQUEST_ASSC_SUCCESS);
                                msg.obj = info;
                                handler.sendMessage(msg);
                            }else{
                                Message msg = handler.obtainMessage(REQUEST_ASSC_NODATA);
                                msg.obj = baseInfo;
                                handler.sendMessage(msg);
                            }
                        }
                    }
                });
        task.executeAsyncTask(SettingsManager.FULL_TASK_EXECUTOR);
    }
}
