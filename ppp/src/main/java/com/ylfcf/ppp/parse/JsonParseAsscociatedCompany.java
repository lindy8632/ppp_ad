package com.ylfcf.ppp.parse;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ylfcf.ppp.entity.AssociatedCompanyInfo;
import com.ylfcf.ppp.entity.AssociatedCompanyParentInfo;
import com.ylfcf.ppp.entity.BaseInfo;
import com.ylfcf.ppp.util.MainJson;
import com.ylfcf.ppp.util.SettingsManager;

/**
 * 关联公司
 * @author Administrator
 *
 */
public class JsonParseAsscociatedCompany {
	private BaseInfo baseInfo;
	private AssociatedCompanyParentInfo associatedCompanyParentInfo;
	private AssociatedCompanyInfo loanInfo;//借款方
	private AssociatedCompanyInfo recommendInfo;//推荐方
	private AssociatedCompanyInfo guaranteeInfo;//担保方
	
	public BaseInfo getBaseInfo() {
		return baseInfo;
	}
	
	private List<String> parseString(String result) throws Exception{
		List<String> list = new ArrayList<String>();
		String str = null;
		JSONArray jsonArray = new JSONArray(result);
		int size = jsonArray.length();
		for(int i=0;i<size;i++){
			str =  jsonArray.getString(i);
			list.add(str);
		}
		return list;
	}
	
	/**
	 * 关联公司
	 * @param result
	 * @throws Exception
	 */
	private void parseGuaranteeInfo(String result) throws Exception{
		JSONObject object = null;
		object = new JSONObject(result);
		if(object != null){
			guaranteeInfo = (AssociatedCompanyInfo) MainJson.fromJson(AssociatedCompanyInfo.class, object);
			List<String> listPicsMark = parseString(guaranteeInfo.getMarkPics());
			List<String> listPicsNomark = parseString(guaranteeInfo.getNoMarkPics());
			List<String> listPicsName = parseString(guaranteeInfo.getMaterials_names());
			guaranteeInfo.setMarkPicsList(listPicsMark);
			guaranteeInfo.setNomarkPicsList(listPicsNomark);
			guaranteeInfo.setMaterialsNamesList(listPicsName);
			associatedCompanyParentInfo.setGuaranteeInfo(guaranteeInfo);
		}
	}
	
	private void parseRecommendInfo(String result) throws Exception{
		JSONObject object = null;
		object = new JSONObject(result);
		if(object != null){
			recommendInfo = (AssociatedCompanyInfo) MainJson.fromJson(AssociatedCompanyInfo.class, object);
			List<String> listPicsMark = parseString(recommendInfo.getMarkPics());
			List<String> listPicsNomark = parseString(recommendInfo.getNoMarkPics());
			List<String> listPicsName = parseString(recommendInfo.getMaterials_names());
			recommendInfo.setMarkPicsList(listPicsMark);
			recommendInfo.setNomarkPicsList(listPicsNomark);
			recommendInfo.setMaterialsNamesList(listPicsName);
			associatedCompanyParentInfo.setRecommendInfo(recommendInfo);
		}
	}
	
	private void parseLoanInfo(String result) throws Exception{
		JSONObject object = null;
		object = new JSONObject(result);
		if(object != null){
			loanInfo = (AssociatedCompanyInfo) MainJson.fromJson(AssociatedCompanyInfo.class, object);
			List<String> listPicsMark = parseString(loanInfo.getMarkPics());
			List<String> listPicsNomark = parseString(loanInfo.getNoMarkPics());
			List<String> listPicsName = parseString(loanInfo.getMaterials_names());
			loanInfo.setMarkPicsList(listPicsMark);
			loanInfo.setNomarkPicsList(listPicsNomark);
			loanInfo.setMaterialsNamesList(listPicsName);
			associatedCompanyParentInfo.setLoanInfo(loanInfo);
		}
	}
	
	/**
	 * 解析msg字段
	 * @param result
	 * @throws Exception
	 */
	public void parseMsg(String result) throws Exception {
		JSONObject object = null;
		object = new JSONObject(result);
		if(object != null){
			associatedCompanyParentInfo = (AssociatedCompanyParentInfo) MainJson.fromJson(AssociatedCompanyParentInfo.class, object);
			parseLoanInfo(associatedCompanyParentInfo.getLoan());
			parseRecommendInfo(associatedCompanyParentInfo.getRecommend());
			parseGuaranteeInfo(associatedCompanyParentInfo.getGuarantee());
			baseInfo.setAssociatedCompanyParentInfo(associatedCompanyParentInfo);
		}
	}
	
	/**
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
	 * 解析调用接口
	 * @param result
	 * @return
	 * @throws JSONException
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static BaseInfo parseData(String result) throws Exception {
		JsonParseAsscociatedCompany jsonParse = new JsonParseAsscociatedCompany();
		jsonParse.parseMain(result);
		return jsonParse.getBaseInfo();
	}
}
