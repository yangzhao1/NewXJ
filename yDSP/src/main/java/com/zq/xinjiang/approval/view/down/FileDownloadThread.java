package com.zq.xinjiang.approval.view.down;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class FileDownloadThread extends Thread {
	private static final int BUFFER_SIZE=1024;
	private URL url;
	private File file;
	private int startPosition;
	private int endPosition;
	private int curPosition;
	//���ڱ�ʶ��ǰ�߳��Ƿ��������
	private boolean finished=false;
	private int downloadSize=0;
	public FileDownloadThread(URL url,File file,int startPosition,int endPosition){
		this.url=url;
		this.file=file;
		this.startPosition=startPosition;
		this.curPosition=startPosition;
		this.endPosition=endPosition;
	}
	@Override
	public void run() {
        BufferedInputStream bis = null;
        RandomAccessFile fos = null;                                               
        byte[] buf = new byte[BUFFER_SIZE];
        URLConnection con = null;
        try {
            con = url.openConnection();
            con.setAllowUserInteraction(true);
            //���õ�ǰ�߳����ص���㣬�յ�
            con.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);
            //ʹ��java�е�RandomAccessFile ���ļ���������д����
            fos = new RandomAccessFile(file, "rw");
            //���ÿ�ʼд�ļ���λ��
            fos.seek(startPosition);
            bis = new BufferedInputStream(con.getInputStream());  
            //��ʼѭ����������ʽ��д�ļ�
            while (curPosition < endPosition) {
                int len = bis.read(buf, 0, BUFFER_SIZE);                
                if (len == -1) {
                    break;
                }
                fos.write(buf, 0, len);
                curPosition = curPosition + len;
                if (curPosition > endPosition) {
                	downloadSize+=len - (curPosition - endPosition) + 1;
                } else {
                	downloadSize+=len;
                }
            }
            //���������Ϊtrue
            this.finished = true;
            bis.close();
            fos.close();
        } catch (IOException e) {
          Log.d(getName() +" Error:", e.getMessage());
        }
	}
 
	public boolean isFinished(){
		return finished;
	}
 
	public int getDownloadSize() {
		return downloadSize;
	}
}

