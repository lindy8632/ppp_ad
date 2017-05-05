package com.ylfcf.ppp.async;

import android.content.Context;

import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.inter.Inter.OnProjectDetails;
import com.ylfcf.ppp.parse.JsonParseProjectDetails;
import com.ylfcf.ppp.util.BackType;
import com.ylfcf.ppp.util.HttpConnection;
import com.ylfcf.ppp.util.URLGenerator;
/**
 * 项目详情
 * @author Administrator
 */
public class AsyncProjectDetails extends AsyncTaskBase{
	private Context context;
	
	private String id;
	private OnProjectDetails onProjectDetails;
	
	private ProjectInfo projectInfo;
	
	public AsyncProjectDetails(Context context, String id,OnProjectDetails onProjectDetails) {
		this.context = context;
		this.id= id;
		this.onProjectDetails = onProjectDetails;
	}

	@Override
	protected String doInBackground(String... params) {
		String url[] = null;
		String result = null;
		try {
			url = URLGenerator.getInstance().getProjectDetailsById(id);
			if (result == null) {
				result = HttpConnection.postConnection(url[0], url[1]);
			}

			if (result == null) {
				result = BackType.FAILE;
			} else {
				projectInfo = JsonParseProjectDetails.parseData(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BackType.ERROR;
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (isCancelled()) {
			return;
		}
		if (BackType.ERROR.equals(result)) {
			// 访问错误
			onProjectDetails.back(null);
		} else if (BackType.FAILE.equals(result)) {
			// 获取失败
			onProjectDetails.back(null);
		} else {
			// 获取成功
			onProjectDetails.back(projectInfo);
		}
	}

}
