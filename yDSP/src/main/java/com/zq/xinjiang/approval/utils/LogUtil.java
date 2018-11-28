package com.zq.xinjiang.approval.utils;

import java.io.File;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;

/**
 * 在 -file:/storage/emulated/0/zqzc/log/log.txt输出日志
 * 
 * @author Administrator
 */
public class LogUtil {

	public static String exction;

	public LogUtil() {
		super();
	}

	/**
	 * 功能：记录日志<br>
	 * 
	 * @param string
	 *            保存日志数据
	 */
	public static void recordLog(String string) {
		try {
			File saveFilePath = new File(
					Environment.getExternalStorageDirectory(), "ydsp/log/");

			if (!saveFilePath.exists()) {
				System.out.println("-saveFilePath:" + saveFilePath);
				saveFilePath.mkdirs();
			}
			File file = new File(saveFilePath.getAbsolutePath(), "log.txt");
			if (!file.exists()) {
				System.out.println("-file:" + file);
				file.createNewFile();
			}
			// 只读方式("r"),读写方式("rw")
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			// seek搜寻方法，只适用于文件
			raf.seek(file.length());
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss:SS");
			String logDate = dateFormat.format(new Date());
			System.out.println("--输出时间：" + logDate + "--输出日志：" + string);
			raf.write(logDate.getBytes());
			raf.writeBytes("\t");
			raf.write(string.getBytes());
			raf.writeBytes("\r\n");
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
