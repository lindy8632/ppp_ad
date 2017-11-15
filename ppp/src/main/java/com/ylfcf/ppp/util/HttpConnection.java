package com.ylfcf.ppp.util;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * http请求
 * @author Administrator
 *
 */
public class HttpConnection {
	private static final int REQUEST_TIME_OUT = 20 * 1000;
	private static final int READ_TIME_OUT = 20 * 1000;

	public static String getConnection(String paramURL) throws Exception {

		URL localURL = null;
		HttpURLConnection urlConnection = null;
		BufferedReader reader = null;

		try {
			localURL = new URL(paramURL);

			if (localURL != null) {
				urlConnection = (HttpURLConnection) localURL.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setConnectTimeout(REQUEST_TIME_OUT);
				urlConnection.setReadTimeout(READ_TIME_OUT);
				// urlConnection.setDoInput(true);
				// urlConnection.setDoOutput(true);
				// urlConnection.setUseCaches(false);

				StringBuffer sb = new StringBuffer();
				if (null != urlConnection && urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
					reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
					String lines;
					while ((lines = reader.readLine()) != null) {
						sb.append(lines);
					}
					return sb.toString();
				}
			}
		} catch (ConnectTimeoutException e) {
			throw new Exception(e);
		} catch (MalformedURLException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			localURL = null;
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new Exception(e);
				}
			}

			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return null;
	}

	/**
	 * Post请求
	 * 
	 * @param param
	 *            请求参数(key1=value1&key2=value2)
	 * @return
	 * @throws Exception
	 *             异常
	 */
	public static String postConnection(String url, String param) throws Exception {
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		HttpURLConnection httpURLConnection = null;
		StringBuffer responseResult = new StringBuffer();
		param = encryptPrams(param);
//		YLFLogger.d("接口：url-------"+url+"\n参数---------"+param);
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			httpURLConnection = (HttpURLConnection) realUrl.openConnection();
			httpURLConnection.setConnectTimeout(REQUEST_TIME_OUT);
			httpURLConnection.setReadTimeout(READ_TIME_OUT);
			// 设置通用的请求属性
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			// 发送请求参数
			printWriter.write(param);
			// flush输出流的缓冲
			printWriter.flush();
			// 根据ResponseCode判断连接是否成功
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode == httpURLConnection.HTTP_OK) {
				// 定义BufferedReader输入流来读取URL的ResponseData
				bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					responseResult.append(line);
				}
//				YLFLogger.d("结果："+responseResult.toString());
				return responseResult.toString();
			}
			return null;
		} catch (ConnectTimeoutException e) {
			throw new Exception(e);
		} catch (MalformedURLException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpURLConnection.disconnect();
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 将参数进行加密处理
	 * @param params
	 * @return
	 */
	private static String encryptPrams(String params) throws Exception{
		if(params == null || (params.contains("version_code") & params.contains("os_type"))){
			return params;
		}
		StringBuffer sb = new StringBuffer();
		String[] parmsStrArr1 = params.split("&");
		for (int i=0;i<parmsStrArr1.length;i++) {
			String[] keyvaluesArr = parmsStrArr1[i].split("=");
			if(keyvaluesArr[0].startsWith("_") && keyvaluesArr[0].endsWith("_") && !"_URL_".equals(keyvaluesArr[0])){
				sb.append(keyvaluesArr[0]).append("=").append(keyvaluesArr[1]);
			}else{
				sb.append(keyvaluesArr[0]).append("=").append(URLEncoder.encode(SimpleCrypto.encryptAES(keyvaluesArr[1],"akD#K2$k=s2kh?DL"),"utf-8")).append("&");
			}
		}
		sb.append("_FROM_=").append(URLEncoder.encode(SimpleCrypto.encryptAES("android","akD#K2$k=s2kh?DL"),"utf-8"));
		return sb.toString();
	}
}
