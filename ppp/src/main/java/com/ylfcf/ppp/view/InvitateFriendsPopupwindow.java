package com.ylfcf.ppp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.PaintDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;
import com.ylfcf.ppp.R;
import com.ylfcf.ppp.entity.ShareInfo;
import com.ylfcf.ppp.util.YLFLogger;

import java.util.Map;

/**
 * 邀请好友
 * 
 * @author jianbing
 * 
 */
public class InvitateFriendsPopupwindow extends PopupWindow implements
		OnClickListener {
	private ImageView wechatImage, wechatCircleImage, sinaImage, qqImage;
	private Button cancelBtn;
	private Activity context;
	private String url;
	private WindowManager.LayoutParams lp = null;
	private UMImage image ;//分享出去的图片
	private String title;//分享出去的标题
	private String text;//分享出去的内容
	private String fromWhere;
	private Bitmap mBitmap;

	public InvitateFriendsPopupwindow(Context context, View convertView,
			int width, int height) {
		super(convertView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		this.context = (Activity) context;
		findViews(convertView);
	}

	private void findViews(View popView) {
		lp = context.getWindow().getAttributes();
		lp.alpha = 0.4f;
		context.getWindow().setAttributes(lp);

		wechatImage = (ImageView) popView
				.findViewById(R.id.invivate_friends_pop_wechat_btn);
		wechatImage.setOnClickListener(this);
		wechatCircleImage = (ImageView) popView
				.findViewById(R.id.invivate_friends_pop_wechatcircle_btn);
		wechatCircleImage.setOnClickListener(this);
		sinaImage = (ImageView) popView
				.findViewById(R.id.invivate_friends_pop_sina_btn);
		sinaImage.setOnClickListener(this);
		qqImage = (ImageView) popView
				.findViewById(R.id.invivate_friends_pop_qq_btn);
		qqImage.setOnClickListener(this);
		cancelBtn = (Button) popView
				.findViewById(R.id.invitate_friends_popupwindow_cancel_btn);
		cancelBtn.setOnClickListener(this);
	}

	public void show(View parentView, String url, String fromWhere, ShareInfo mShareInfo, Bitmap mBitmap) {
		this.url = url;
		this.fromWhere = fromWhere;
		this.mBitmap = mBitmap;
		this.setBackgroundDrawable(new PaintDrawable(R.color.transparent)); // 使得返回键有效
		this.setAnimationStyle(R.style.bidPopwindowStyle);
		this.setOutsideTouchable(true);
		this.setFocusable(true);
		this.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		if("邀请有奖".equals(fromWhere)){
			image = new UMImage(context, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.share_logo));
			title = "元立方金服";
			text = "全国首家PPP模式互联网金融平台";
		}else if("新春福利2017".equals(fromWhere)){
			image = new UMImage(context, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.xcfl_share_logo));
			title = "小元喊你来领压岁钱";
			text = "元月来元立方领压岁钱啦，千万现金注册即领！";
		}else if("开门红乐享返现".equals(fromWhere)){
			image = new UMImage(context, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.lxfx_share_logo));
			title = "开年红，乐享返现！更有加息券等你来领！";
			text = "一唱雄鸡天下白，元立方金服与您一起喜迎新春，特推出投资返现活动，另有无门槛加息券等您来领！";
		}else if("三月份签到活动".equals(fromWhere)){
			image = new UMImage(context, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.sign_share_logo));
			title = "天天签到，天天领取加息券！";
			text = "3月1日起，每日签到小元都会送您一张加息券，为您理财添彩头。";
		}else if("会员福利二期".equals(fromWhere)){
			image = new UMImage(context, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.hyfl2_share_logo));
			title = "会员福利，免费礼品天天领!";
			text = "3月15日起，每天登录元立方金服，20种礼品任您选，只求您登录不要您出钱。";
		}else if("四月份推广活动".equals(fromWhere)){
			image = new UMImage(context, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.yqhy_share_logo));
			title = "邀请好友理财，奖励高达1.3%";
			text = "邀好友来元立方理财，可获好友年化投资额高达1.3%的奖励！";
		}else if("每周一抢现金".equals(fromWhere)){
			image = new UMImage(context, BitmapFactory.decodeResource(
					context.getResources(), R.drawable.qxj5_share_logo));
			title = "每周一，抢现金，人人有份！";
			text = "关注进入微信群，每周发红包，大家疯狂开抢！平台周一更有超大现金红包等你领取！";
		}else{
			title = mShareInfo.getTitle();
			text = mShareInfo.getContent();
			if(mBitmap == null){
				image = new UMImage(context, BitmapFactory.decodeResource(
						context.getResources(), R.drawable.share_logo_default));
			}else{
				image = new UMImage(context, mBitmap);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.invitate_friends_popupwindow_cancel_btn:
			this.dismiss();
			break;
		case R.id.invivate_friends_pop_wechat_btn:
			shareWechat(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.invivate_friends_pop_wechatcircle_btn:
			shareWechat(SHARE_MEDIA.WEIXIN_CIRCLE);
			break;
		case R.id.invivate_friends_pop_sina_btn:
			shareSina(SHARE_MEDIA.SINA);
			break;
		case R.id.invivate_friends_pop_qq_btn:
			shareQQ(SHARE_MEDIA.QQ);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
		lp.alpha = 1.0f;
		context.getWindow().setAttributes(lp);
	}

	/**
	 * 微信分享
	 * @param plateName
	 */
	private void shareWechat(SHARE_MEDIA plateName) {
		ShareAction shareAction = new ShareAction(context);
		shareAction.withTitle(title).withText(text)
				.withTargetUrl(url).withMedia(image);
		shareAction.setPlatform(plateName).setCallback(umShareListener).share();
	}

	/**
	 * QQ分享
	 * @param plateName
	 */
	private void shareQQ(SHARE_MEDIA plateName) {
		ShareAction shareAction = new ShareAction(context);
		shareAction.withText(text)
				 .withMedia(image)
				.withTitle(title).withTargetUrl(url);
		shareAction.setPlatform(plateName).setCallback(umShareListener).share();
	}

	/**
	 * 新浪微博分享
	 * @param plateName
	 */
	private void shareSina(SHARE_MEDIA plateName) {
		ShareAction shareAction = new ShareAction(context);
		shareAction.withText(text);
		shareAction.withTitle(title);
		shareAction.withTargetUrl(url);
		shareAction.withMedia(image);
		shareAction.setPlatform(plateName).setCallback(umShareListener).share();
	}

	// 分享回调
	private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(context, " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, " 分享成功啦", Toast.LENGTH_SHORT).show();
            }
            YLFLogger.d("中秋大转盘", "分享成功。");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context, " 分享失败啦", Toast.LENGTH_SHORT).show();
            YLFLogger.d("中秋大转盘", "分享失败。");
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context, " 分享取消了", Toast.LENGTH_SHORT).show();
            YLFLogger.d("中秋大转盘", "分享取消。");
        }
    };

	// 授权回调
	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action,
				Map<String, String> data) {
			Toast.makeText(context, "Authorize succeed", Toast.LENGTH_SHORT)
					.show();
			shareWechat(platform);
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			Toast.makeText(context, "Authorize fail", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			Toast.makeText(context, "Authorize cancel", Toast.LENGTH_SHORT)
					.show();
		}
	};
	
}
