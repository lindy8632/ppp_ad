package com.ylfcf.ppp.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.ylfcf.ppp.util.YLFLogger;

import android.content.Context;
import android.text.TextUtils;

/**
 * 本地文件存储的工具类
 * @author Mr.liu
 *
 */
public class FileUtil {
	/** SD卡本地文件目录 */
	public static final String YLFCF_DIR = "ylfcf";
	public static final String YLFCF_BACKGROUND_DIR = YLFCF_DIR + File.separator + "Background";

	/** 内部文件目录 */
	public static final String YLFCF_INTERNAL_FILE_DIR = "YLFCFFile";
	public static final String YLFCF_INTERNAL_CACHE_DIR = "YLFCFCache";
	
	
	/**缓存的文件名 */
	public static final String YLFCF_BANNER_CACHE = "ylfcf_banner_cache";//首页banner
	public static final String YLFCF_YXB_PRODUCT_CACHE = "yxb_product_cache";//首页元信宝产品
	public static final String YLFCF_YXB_PRODUCTLOG_CACHE = "yxb_productlog_cache";//首页元信宝每日统计
	public static final String YLFCF_SRZX_TOTAL_CACHE = "srzx_total_cache";//私人尊享所有产品的列表
	public static final String YLFCF_VIP_TOTAL_CACHE = "vip_total_cache";//vip所有产品的列表
	public static final String YLFCF_ZXD_TOTAL_CACHE = "zxd_total_cache";//政信贷所有的列表
	public static final String YLFCF_ZXD_SUYING_CACHE = "zxd_suying_cache";//政信贷速盈列表
	public static final String YLFCF_ZXD_BAOYING_CACHE = "zxd_baoying_cache";//政信贷保盈列表
	public static final String YLFCF_ZXD_WENYING_CACHE = "zxd_wenying_cache";//政信贷稳赢列表
	public static final String YLFCF_YJH_CACHE = "yjh_cache";//元计划列表

	/**
	 * 获取文件目录，首先检查SD卡是否存在，存在则获取应用默认的文件目录，或者获取内部存储自定义目录
	 * 
	 * @param context
	 * @return eg:/storage/emulated/0/Android/data/项目包名/files
	 */
	public static String getFilePath(Context context) {
		String path = "";
		if (SDCardUtil.checkSDCard()) {
			File file = context.getExternalFilesDir(null);
//			if(!file.exists()) {
//				file.mkdirs();
//			}
			// File file = Environment.getExternalStorageDirectory();
			// 如果外部文件目录获取不到，则使用内部存储控件
			if (null != file) {
				if(!file.exists()) {
					file.mkdirs();
				}
				path = file.getAbsolutePath();// + File.separator + KANKETV_DIR;
			} else {
				path = context.getDir(YLFCF_INTERNAL_FILE_DIR, Context.MODE_PRIVATE).getAbsolutePath();
			}
		} else {
			path = context.getDir(YLFCF_INTERNAL_FILE_DIR, Context.MODE_PRIVATE).getAbsolutePath();
		}
		return path;
	}

	/**
	 * 获取文件目录，首先检查SD卡是否存在，存在则获取应用默认的缓存目录，或者获取内部存储自定义目录
	 * 
	 * @param context
	 * @return eg:/storage/emulated/0/Android/data/项目包名/caches
	 */
	public static String getCachePath(Context context) {
		String path = "";
		if (SDCardUtil.checkSDCard()) {
			File file = context.getExternalCacheDir();
			// File file = Environment.getExternalStorageDirectory();
			if (null != file) {// 如果外部文件目录获取不到，则使用内部存储控件
				path = file.getAbsolutePath();// + File.separator
												// +KANKETV_CACHE_DIR;
			} else {
				path = context.getDir(YLFCF_INTERNAL_CACHE_DIR, Context.MODE_PRIVATE).getAbsolutePath();
			}
		} else {
			path = context.getDir(YLFCF_INTERNAL_CACHE_DIR, Context.MODE_PRIVATE).getAbsolutePath();
		}
		return path;
	}

	/**
	 * 保存数据,保存位置为当前应用私有的存储空间,文件目录：data/data/***包名/files/
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            内容
	 */
	public static void save(Context context, String filename, String content) throws IOException {
		FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
		fos.write(content.getBytes());
		fos.close();
		YLFLogger.d("saveFile " + filename);
	}

	/**
	 * 保存数据,保存位置为当前应用私有的存储空间,文件目录：data/data/***包名/files/
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            内容
	 */
	public static void saveObject(Context context, String filename, Object ob) throws IOException {
		YLFLogger.d("saveFile " + filename);
		ObjectOutputStream oos = null;
		if (context == null) {
			new NullPointerException("Context is not null.");
		}
		try {
			oos = new ObjectOutputStream(context.openFileOutput(filename, Context.MODE_PRIVATE));
			oos.writeObject(ob);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (oos != null) {
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 保存二进制数据,保存位置为当前应用私有的存储空间,文件目录：data/data/***包名/files/
	 * 
	 * @param filename
	 *            文件名称
	 * @param content
	 *            内容
	 */
	public static void save(Context context, String filename, InputStream content) throws IOException {
		FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
		BufferedOutputStream buf = new BufferedOutputStream(fos);

		byte[] buffer = new byte[1024];
		int length;
		try {
			while ((length = content.read(buffer)) != -1) {
				buf.write(buffer, 0, length);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		buf.flush();
		buf.close();
		fos.close();
		YLFLogger.d("saveFile " + filename);
	}

	/**
	 * 根据文件名，获取此文件内容的字节数组,文件目录：data/data/***包名/files/
	 * 
	 * @param filename
	 *            有效的文件名
	 * @return 文件内容的字节数组
	 * @throws Exception
	 */
	public static InputStream readStram(Context context, String filename) {
		InputStream is = null;
		FileInputStream fis = null;
		if (context == null) {
			new NullPointerException("Context is not null.");
		}
		if (TextUtils.isEmpty(filename)) {
			new NullPointerException("FileName is not null.");
		}
		try {
			fis = context.openFileInput(filename);
			is = getInputStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return is;
	}

	/**
	 * 根据文件名，获取此文件内容的字节数组,文件目录：data/data/***包名/files/
	 * 
	 * @param filename
	 *            有效的文件名
	 * @return 文件内容的字节数组
	 * @throws Exception
	 */
	public static byte[] readByte(Context context, String filename) {
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		if (context == null) {
			new NullPointerException("Context is not null.");
		}
		if (TextUtils.isEmpty(filename)) {
			new NullPointerException("FileName is not null.");
		}
		try {
			fis = context.openFileInput(filename);
			bos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) != -1) {
				bos.write(buffer, 0, length);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bos.toByteArray();
	}

	/**
	 * 根据文件名，获取对象内容,文件目录：data/data/***包名/files/
	 * 
	 * @param filename
	 *            有效的文件名
	 * @return 文件内容的字节数组
	 * @throws Exception
	 */
	public static Object readObject(Context context, String filename) {
		YLFLogger.d("saveFile " + filename);
		ObjectInputStream ois = null;
		if (context == null) {
			new NullPointerException("Context is not null.");
		}
		try {
			ois = new ObjectInputStream(context.openFileInput(filename));
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * FileInputStram to inputStram
	 * 
	 * @param fileInput
	 * @return
	 */
	public static InputStream getInputStream(FileInputStream fileInput) {
		ByteArrayOutputStream baos = null;
		byte[] buffer = new byte[1024 * 4];
		int n = -1;
		InputStream inputStream = null;
		try {
			baos = new ByteArrayOutputStream();
			while ((n = fileInput.read(buffer)) != -1) {
				baos.write(buffer, 0, n);
			}
			byte[] byteArray = baos.toByteArray();
			inputStream = new ByteArrayInputStream(byteArray);
			return inputStream;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 文件是否存在
	 * 
	 * @param path
	 *            文件目录
	 * @return
	 */
	public static boolean isFileExists(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		return new File(path).exists();
	}
}
