package com.ylfcf.ppp.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.entity.ProjectInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * ��Ŀ����
 * @author Administrator
 *
 */
public class JsonParseProjectDetails {
	private BaseInfo baseInfo;
	private ProjectInfo projectInfo;
	
	public ProjectInfo getProjectInfo() {
		return projectInfo;
	}
	
	/**
	 * ����msg�ֶ�
	 * @param result
	 * @throws Exception
	 */
	public void parseMsg(String result) throws Exception {
		JSONObject object = null;
		object = new JSONObject(result);
		if(object != null){
			projectInfo = (ProjectInfo) MainJson.fromJson(ProjectInfo.class, object);
		}
	}
	
	/**
	 * ��ʼ����...
	 * @param result
	 * @throws Exception
	 */
	public void parseMain(String result) throws Exception{
		JSONObject object = null;
		try {
			object = new JSONObject(result);
		} catch (Exception e) {
		}
		
		if(object != null) {
			baseInfo = (BaseInfo)MainJson.fromJson(BaseInfo.class, object);
			int resultCode = SettingsManager.getResultCode(baseInfo);
			if(resultCode == 0){
				parseMsg(baseInfo.getMsg());
			}
		}
	}
	
	/**
	 * �������ýӿ�
	 * @param result
	 * @return
	 * @throws JSONException
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static ProjectInfo parseData(String result) throws Exception {
		JsonParseProjectDetails jsonParse = new JsonParseProjectDetails();
		jsonParse.parseMain(result);
		return jsonParse.getProjectInfo();
	}

}