package com.zq.xinjiang.approval.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
	
	/**
	 * 数据库名字和版本
	 */
	public final static String DB_NAME = "ydsp.db";
	final static int DB_VERSION = 1; 	
	
	private static DBHelper mInstance;
	//部门收费 创建表
	private String bmsf_sql = "create table bmsf (spid text,ip text,orgid text,orgname text,shoujian text,acceptnum text,yiban text,yishou text,charge text)";
	//收费列表
	private String sflb_sql = "create table sflb (spid text,ip text,rowid text,id text,orgid text,sncode text,itemname text,username text,charge text)";
	//个人办件
	private String grbj_sql = "create table grbj (spid text,ip text,id text,sncode text,itemname text,username text,issupervised text,flowid text," +
								"stepid text,endtime text,statedesc text,charge text,iteminstanceid text)";
	//待办事项--正常
	private String dbsx_zc_sql = "create table dbsx_zc (spid text,ip text,id text,sncode text,flowid text,stepid text," +
					"issupervised text,itemname text,username text)";	
	//待办事项--过期
	private String dbsx_gq_sql = "create table dbsx_gq (spid text,ip text,id text,sncode text,flowid text,stepid text," +
					"issupervised text,itemname text,username text)";
	//待办事项--预警
	private String dbsx_yj_sql = "create table dbsx_yj (spid text,ip text,id text,sncode text,flowid text,stepid text," +
					"issupervised text,itemname text,username text)";
	//异常办件--过期
	private String ycbj_gq_sql = "create table ycbj_gq (spid text,ip text,id text,sncode text,flowid text,stepid text," +
					"issupervised text,itemname text,username text)";
	//异常办件--预警
	private String ycbj_yj_sql = "create table ycbj_yj (spid text,ip text,id text,sncode text,flowid text,stepid text," +
					"issupervised text,itemname text,username text)";
	//报延批示--已处理
	private String byps_ycl_sql = "create table byps_ycl (spid text,ip text,id text,sncode text,itemname text,username text,statedesc text)";
	//报延批示--未处理
	private String byps_dcl_sql = "create table byps_dcl (spid text,ip text,id text,sncode text,itemname text,username text)";
	//部门办件--件数
	private String bmbj_sql = "create table bmbj (spid text,ip text,orgid text,tuiban text,zuofei text,shanchu text,zhuanbao text,bujiaobulai text," +
					"zixun text,banjie text)";
	//部门办件--退回办理
	private String bmbj_thbl_sql = "create table bmbj_thbl (spid text,ip text,id text,sncode text,itemname text,username text,endtime text,statedesc text)";
	//部门办件--作废办结
	private String bmbj_zfbj_sql = "create table bmbj_zfbj (spid text,ip text,id text,sncode text,itemname text,username text,endtime text,statedesc text)";
	//部门办件--咨询办结
	private String bmbj_zxbj_sql = "create table bmbj_zxbj (spid text,ip text,id text,sncode text,itemname text,username text,endtime text,statedesc text)";
	//部门办件--补交不来
	private String bmbj_bjbl_sql = "create table bmbj_bjbl (spid text,ip text,id text,sncode text,itemname text,username text,endtime text,statedesc text)";
	//部门办件--删除办结
	private String bmbj_scbj_sql = "create table bmbj_scbj (spid text,ip text,id text,sncode text,itemname text,username text,endtime text,statedesc text)";
	//部门办件--正常办结
	private String bmbj_zcbj_sql = "create table bmbj_zcbj (spid text,ip text,id text,sncode text,itemname text,username text,endtime text,statedesc text)";
	//部门办件--转报办结
	private String bmbj_zbbj_sql = "create table bmbj_zbbj (spid text,ip text,id text,sncode text,itemname text,username text,endtime text,statedesc text)";
	//站内消息--已收消息
	private String znxx_ys_sql = "create table znxx_ys (spid text,ip text,id text,title text,content text,sendtime text,sender_name text,isreceived text)";
	//站内消息--已发消息
	private String znxx_yf_sql = "create table znxx_yf (spid text,ip text,id text,title text,content text,sendtime text,sender_name text,isreceived text)";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	public synchronized static DBHelper getInstance(Context context) {  
        if (mInstance == null) {  
            mInstance = new DBHelper(context);  
        }  
        return mInstance;  
    }; 

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Log.i("创建表", "部门收费");
		db.execSQL(bmsf_sql);
		Log.i("创建表", "收费列表");
		db.execSQL(sflb_sql);
		Log.i("创建表", "个人办件");
		db.execSQL(grbj_sql);
		Log.i("创建表", "待办事项--正常");
		db.execSQL(dbsx_zc_sql);
		Log.i("创建表", "待办事项--过期");
		db.execSQL(dbsx_gq_sql);
		Log.i("创建表", "待办事项--预警");
		db.execSQL(dbsx_yj_sql);
		Log.i("创建表", "异常办件--预警");
		db.execSQL(ycbj_yj_sql);
		Log.i("创建表", "异常办件--过期");
		db.execSQL(ycbj_gq_sql);
		Log.i("创建表", "报延批示--已处理");
		db.execSQL(byps_ycl_sql);
		Log.i("创建表", "报延批示--未处理");
		db.execSQL(byps_dcl_sql);
		Log.i("创建表", "部门办件");
		db.execSQL(bmbj_sql);
		Log.i("创建表", "部门办件--退回办理");
		db.execSQL(bmbj_thbl_sql);
		Log.i("创建表", "部门办件--作废办结");
		db.execSQL(bmbj_zfbj_sql);
		Log.i("创建表", "部门办件--咨询办结");
		db.execSQL(bmbj_zxbj_sql);
		Log.i("创建表", "部门办件--补交不来");
		db.execSQL(bmbj_bjbl_sql);
		Log.i("创建表", "部门办件--删除办结");
		db.execSQL(bmbj_scbj_sql);
		Log.i("创建表", "部门办件--正常办结");
		db.execSQL(bmbj_zcbj_sql);
		Log.i("创建表", "部门办件--转报办结");
		db.execSQL(bmbj_zbbj_sql);
		Log.i("创建表", "站内消息--已发");
		db.execSQL(znxx_yf_sql);
		Log.i("创建表", "站内消息--已收");
		db.execSQL(znxx_ys_sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		switch (oldVersion) {
        case 0:
            if (newVersion <= 1) {
                return;
            }

            db.beginTransaction();
            try {
//                upgradeDatabaseToVersion1(db);
                db.setTransactionSuccessful();
            } catch (Throwable ex) {
//                Log.e(TAG, ex.getMessage(), ex);
                break;
            } finally {
                db.endTransaction();
            }
     	   return;
	    }
	    onCreate(db);
	}
}
