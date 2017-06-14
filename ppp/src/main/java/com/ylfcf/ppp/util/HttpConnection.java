package com.ylfcf.ppp.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.conn.ConnectTimeoutException;

/**
 * http����
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
	 * Post����
	 * 
	 * @param url�����ַ
	 * @param param
	 *            �������(key1=value1&key2=value2)
	 * @return
	 * @throws Exception
	 *             �쳣
	 */
	public static String postConnection(String url, String param) throws Exception {
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;
		HttpURLConnection httpURLConnection = null;

		StringBuffer responseResult = new StringBuffer();

		try {
			URL realUrl = new URL(url);
			// �򿪺�URL֮�������
			httpURLConnection = (HttpURLConnection) realUrl.openConnection();
			httpURLConnection.setConnectTimeout(REQUEST_TIME_OUT);
			httpURLConnection.setReadTimeout(READ_TIME_OUT);
			// ����ͨ�õ���������
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));
			// ����POST�������������������
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// ��ȡURLConnection�����Ӧ�������
			printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			// �����������
			printWriter.write(param);
			// flush������Ļ���
			printWriter.flush();
			// ����ResponseCode�ж������Ƿ�ɹ�
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode == httpURLConnection.HTTP_OK) {
				// ����BufferedReader����������ȡURL��ResponseData
				bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					responseResult.append(line);
				}
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
}