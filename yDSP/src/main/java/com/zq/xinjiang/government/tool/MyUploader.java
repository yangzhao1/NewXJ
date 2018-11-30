package com.zq.xinjiang.government.tool;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/26.
 */

public class MyUploader {

    /**
     *
     * 同步上传多个文件
     * 基于标准的http实现，需要在非UI线程中调用，以免阻塞UI。
     *
     */
        private static final String TAG = "MyUploader";

        // ////////////////////同步上传多个文件/////////

        /**
         * 同步上传File
         *
         * @param Url
         * @param  : 全路径，ex. /sdcard/f/yh.jpg
         * @param      : file name, ex. yh.jpg
         * @return 服务器的响应结果(字符串形式)
         */
        public String MyUploadMultiFileSync(String Url,
                                            List<Map<String,String>> fileList, Map<String, String> params) {
            String reulstCode = "";
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "--------boundary";

            try {
                URL url = new URL(Url);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                // 允许Input、Output，不使用Cache
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                // 设置传送的method=POST
                con.setRequestMethod("POST");
                // setRequestProperty
                con.setRequestProperty("Connection", "Keep-Alive");
                con.setRequestProperty("Charset", "UTF-8");
                // con.setRequestProperty("Content-Type",
                // "application/x-www-form-urlencoded");
                con.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);

                StringBuffer s = new StringBuffer();
                // 设置DataOutputStream
                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                if (fileList.size()!=0){
                    for (int i = 0; i < fileList.size(); i++) {

                        String filePath = (String) fileList.get(i).get("phonePath");
                        String id = (String) fileList.get(i).get("sourceid");

                        int endFileIndex = filePath.lastIndexOf("/");
                        String fileName = filePath.substring(endFileIndex+1);
                        fileName = id + "_" + fileName;
                        Log.i(TAG, "filename= " + fileName);
                        // set 头部
                        StringBuilder sb = new StringBuilder();

                        sb.append(twoHyphens);
                        sb.append(boundary);
                        sb.append(end);
                        sb.append("Content-Disposition: form-data; ");
                        sb.append("name=" + "\"" + "loadFile" + "\"");
                        sb.append(";filename=");
                        sb.append("\"" + fileName + "\"");
                        sb.append(end);

                        sb.append("Content-Type: ");
                        sb.append("application/octet-stream");

                        sb.append(end);
                        sb.append(end);

                        // 1. write sb
                        dos.writeBytes(sb.toString());

                        // 取得文件的FileInputStream
                        FileInputStream fis = new FileInputStream(filePath);
                        // 设置每次写入1024bytes
                        int bufferSize = 1024;
                        byte[] buffer = new byte[bufferSize];

                        int length = -1;
                        // 从文件读取数据至缓冲区
                        while ((length = fis.read(buffer)) != -1) {
                            dos.write(buffer, 0, length);
                        }
                        dos.writeBytes(end);
                        fis.close();

                        dos.writeBytes(end);
                        dos.writeBytes(end);

                        //dos.writeBytes(end);
                        //dos.flush();
                        // close streams
                        //fis.close();
                    }
                }
                // set 尾部
                StringBuilder sb2 = new StringBuilder();

                if (params != null && !params.isEmpty()) {
                    for (String key : params.keySet()) {
                        String value = params.get(key);
                        sb2.append(twoHyphens);
                        sb2.append(boundary);
                        sb2.append(end);
                        sb2.append("Content-Disposition: form-data; ");
                        sb2.append("name=" + "\"");
                        sb2.append(key + "\"");
                        sb2.append(end);
                        sb2.append(end);
                        sb2.append(value);
                        sb2.append(end);
                    }
                }
                sb2.append(twoHyphens + boundary + end);
                dos.writeBytes(sb2.toString());
                dos.flush();
                Log.i(TAG, "sb2:" + sb2.toString());

                // 取得Response内容
                InputStream is = con.getInputStream();
                int ch;
                StringBuffer b = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    b.append((char) ch);
                }
                reulstCode = b.toString().trim();
                // 关闭
                dos.close();
            } catch (IOException e) {
                Log.i(TAG, "IOException: " + e);
                e.printStackTrace();
            }

            return reulstCode;
        }
    }
