package com.zq.xinjiang.approval.db;

import java.util.ArrayList;

import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.entity.BmsfEntity;
import com.zq.xinjiang.approval.pulltorefresh.DbsxInfo;
import com.zq.xinjiang.approval.pulltorefresh.ZnxxInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class DBWrapper {
	/**
	 *  数据库执行语句
	 *  杨钊 2016-6-24
	 */
	private static DBWrapper sInstance;
	DBHelper dbHelper;

	SQLiteDatabase mDb;

	public DBWrapper(Context context) {
		// TODO Auto-generated constructor stub
		DBHelper dbHelper = new DBHelper(context);
		dbHelper = DBHelper.getInstance(context);
		mDb = dbHelper.getWritableDatabase();
	}

	/**
	 * 获得DBWrapper类对象
	 */

	public static DBWrapper getInstance(Context context) {
		if (sInstance == null) {
			synchronized (DBWrapper.class) {
				if (sInstance == null) {
					sInstance = new DBWrapper(context);
				}
			}
		}
		return sInstance;
	}

	/**
	 * 删除表
	 * @param table
	 */

	public void deleteTable(String table){
		mDb.delete(table, null, null);
		Log.i("DBWrapper", "删除了     " + table + "    表");
	}

	public void deleteTableBySpid(String table,String spid) {
		mDb.delete(table, "spid=?", new String[]{spid});
		Log.i("DBWrapper", "删除  "+table+"表");
	}

	public void deleteTableBySpid(String table,String spid,String ip) {
		mDb.delete(table, "spid=? and ip=?", new String[]{spid,ip});
		Log.i("DBWrapper", "删除  "+table+"表");
	}

	public void deleteTableBySpid(String table,String spid,String ip,String id) {
		mDb.delete(table, "spid=? and ip=? and id=?", new String[]{spid,ip,id});
		Log.i("DBWrapper", "删除  "+table+"表");
	}

	/**
	 * 查询表
	 * @param table
	 * @param spid
	 * @return
	 */
	public Cursor selectTableBySpid(String table,String spid) {
		Cursor cursor = mDb.query(table, null, "spid=?", new String[]{spid}, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	public Cursor selectTableBySpid(String table,String spid,String ip) {
		Cursor cursor = mDb.query(table, null, "spid=? and ip=?", new String[]{spid,ip}, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	/**
	 * 部门收费
	 * create table bmsf (spid text,orgid text,orgname text,shoujian text,acceptnum text,yiban text,yishou text,charge text)
	 * @param table
	 * @param bmsfEntity
	 */
	public void insertBMSF(String table,BmsfEntity bmsfEntity) {
		ContentValues values = new ContentValues();
		values.put("spid", MainActivity.spid);
		values.put("ip",MainActivity.hostIp);//新加ip参数
		values.put("orgid", bmsfEntity.getOrgid());
		values.put("orgname", bmsfEntity.getOrgname());
		values.put("yishou", bmsfEntity.getYishou());
		values.put("charge", bmsfEntity.getCharge());
		long rowid = mDb.insert(table, null, values);
		Log.i("DBWrapper", "添加入数据库第几行          "+rowid);
	}

	/**
	 * 收费列表
	 * create table sflb (spid text,rowid text,id text,orgid text,sncode text,itemname text,username text,charge text)
	 * @param table
	 * @param dbsxInfo
	 */
	
	public void insertSFLB(String table,DbsxInfo dbsxInfo) {
		ContentValues values = new ContentValues();
		values.put("spid", MainActivity.spid);
		values.put("ip",MainActivity.hostIp);//新加ip参数
		values.put("id", dbsxInfo.getId());
		values.put("sncode", dbsxInfo.getSncode());
		values.put("itemname", dbsxInfo.getItemname());
		values.put("username", dbsxInfo.getUsername());
		values.put("charge", dbsxInfo.getCharge());
		long rowid = mDb.insert(table, null, values);
		Log.i("DBWrapper", "添加入数据库第几行          "+rowid);
	}
	/**
	 * 个人办件
	 * create table grbj (spid text,id text,sncode text,itemname text,username text,issupervised text,
	 * flowid text,"flowid text,stepid text,endtime text,statedesc text,charge text,iteminstanceid text)"
	 * @param table
	 * @param dbsxInfo
	 */
	public void insertGrbj(String table,DbsxInfo dbsxInfo) {
		ContentValues values = new ContentValues();
		values.put("spid", MainActivity.spid);
		values.put("ip",MainActivity.hostIp);//新加ip参数
		values.put("id", dbsxInfo.getId());
		values.put("sncode", dbsxInfo.getSncode());
		values.put("itemname", dbsxInfo.getItemname());
		values.put("username", dbsxInfo.getUsername());
		values.put("endtime", dbsxInfo.getEndtime());
		values.put("statedesc", dbsxInfo.getStatedesc());
		long rowid = mDb.insert(table, null, values);
		Log.i("DBWrapper", "添加入数据库第几行          "+rowid);
	}
	
	/**
	 * 待办事项--正常
	 * create table dbsx_zc (spid text,id text,sncode text,flowid text,stepid text,issupervised text,itemname text,username text
	 * @param table
	 * @param dbsxInfo
	 */
	public void insertDbsxzc(String table,DbsxInfo dbsxInfo) {
		ContentValues values = new ContentValues();
		values.put("spid", MainActivity.spid);
		values.put("ip",MainActivity.hostIp);//新加ip参数
		values.put("id", dbsxInfo.getId());
		values.put("sncode", dbsxInfo.getSncode());
		values.put("flowid", dbsxInfo.getFlowid());
		values.put("stepid", dbsxInfo.getStepid());
		values.put("issupervised", dbsxInfo.getIssupervised());
		values.put("itemname", dbsxInfo.getItemname());
		values.put("username", dbsxInfo.getUsername());
		long rowid = mDb.insert(table, null, values);
		Log.i("DBWrapper", "添加入数据库第几行          "+rowid);
	}
	
	/**
	 * 待办事项--过期
	 * create table dbsx_gq (spid text,id text,sncode text,flowid text,stepid text,issupervised text,itemname text,username text
	 * @param table
	 * @param dbsxInfo
	 */
	public void insertDbsxgq(String table,DbsxInfo dbsxInfo) {
		ContentValues values = new ContentValues();
		values.put("spid", MainActivity.spid);
		values.put("ip",MainActivity.hostIp);//新加ip参数
		values.put("id", dbsxInfo.getId());
		values.put("sncode", dbsxInfo.getSncode());
		values.put("flowid", dbsxInfo.getFlowid());
		values.put("stepid", dbsxInfo.getStepid());
		values.put("issupervised", dbsxInfo.getIssupervised());
		values.put("itemname", dbsxInfo.getItemname());
		values.put("username", dbsxInfo.getUsername());
		long rowid = mDb.insert(table, null, values);
		Log.i("DBWrapper", "添加入数据库第几行          "+rowid);
	}
	
	/**
	 * 待办事项--预警
	 * create table dbsx_yj (spid text,id text,sncode text,flowid text,stepid text,issupervised text,itemname text,username text
	 * @param table
	 * @param dbsxInfo
	 */
	public void insertDbsxyj(String table,DbsxInfo dbsxInfo) {
		ContentValues values = new ContentValues();
		values.put("spid", MainActivity.spid);
		values.put("ip",MainActivity.hostIp);//新加ip参数
		values.put("id", dbsxInfo.getId());
		values.put("sncode", dbsxInfo.getSncode());
		values.put("flowid", dbsxInfo.getFlowid());
		values.put("stepid", dbsxInfo.getStepid());
		values.put("issupervised", dbsxInfo.getIssupervised());
		values.put("itemname", dbsxInfo.getItemname());
		values.put("username", dbsxInfo.getUsername());
		long rowid = mDb.insert(table, null, values);
		Log.i("DBWrapper", "添加入数据库第几行          "+rowid);
	}
	
	/**
	 * 异常办件--预警
	 * create table ycbj_yj (spid text,id text,sncode text,flowid text,stepid text," +
	 *				"issupervised text,itemname text,username text)";
	 * @param table
	 * @param dbsxInfo
	 */
	public void insertYcbjyj(String table,DbsxInfo dbsxInfo) {
		
		ContentValues values = new ContentValues();
		values.put("spid", MainActivity.spid);
		values.put("ip",MainActivity.hostIp);//新加ip参数
		values.put("id", dbsxInfo.getId());
		values.put("sncode", dbsxInfo.getSncode());
		values.put("flowid", dbsxInfo.getFlowid());
		values.put("stepid", dbsxInfo.getStepid());
		values.put("issupervised", dbsxInfo.getIssupervised());
		values.put("itemname", dbsxInfo.getItemname());
		values.put("username", dbsxInfo.getUsername());
		long rowid = mDb.insert(table, null, values);
		Log.i("DBWrapper", "添加入数据库第几行          "+rowid);
	}
	
	/**
	 * 异常办件--过期
	 * create table ycbj_gq (spid text,id text,sncode text,flowid text,stepid text," +
	 *				"issupervised text,itemname text,username text)";
	 * @param table
	 * @param dbsxInfo
	 */
	public void insertYcbjgq(String table,DbsxInfo dbsxInfo) {
		
		ContentValues values = new ContentValues();
		values.put("spid", MainActivity.spid);
		values.put("ip",MainActivity.hostIp);//新加ip参数
		values.put("id", dbsxInfo.getId());
		values.put("sncode", dbsxInfo.getSncode());
		values.put("flowid", dbsxInfo.getFlowid());
		values.put("stepid", dbsxInfo.getStepid());
		values.put("issupervised", dbsxInfo.getIssupervised());
		values.put("itemname", dbsxInfo.getItemname());
		values.put("username", dbsxInfo.getUsername());
		long rowid = mDb.insert(table, null, values);
		Log.i("DBWrapper", "添加入数据库第几行          "+rowid);
	}
	
	/**
	 * 报延批示--已处理
	 * create table byps_ycl (spid text,id text,sncode text,itemname text,username text,statedesc text)
	 * @param table
	 * @param dbsxInfo
	 */
	public void insertBypsycl(String table,DbsxInfo dbsxInfo) {
		
		ContentValues values = new ContentValues();
		values.put("spid", MainActivity.spid);
		values.put("ip",MainActivity.hostIp);//新加ip参数
		values.put("id", dbsxInfo.getId());
		values.put("sncode", dbsxInfo.getSncode());
		values.put("itemname", dbsxInfo.getItemname());
		values.put("username", dbsxInfo.getUsername());
		values.put("statedesc", dbsxInfo.getStatedesc());
		long rowid = mDb.insert(table, null, values);
		Log.i("DBWrapper", "添加入数据库第几行          "+rowid);
	}
	
	/**
	 * 报延批示--未处理
	 * create table byps_dcl (spid text,id text,sncode text,itemname text,username text)
	 * @param table
	 * @param dbsxInfo
	 */
	public void insertBypsdcl(String table,DbsxInfo dbsxInfo) {
		
		ContentValues values = new ContentValues();
		values.put("spid", MainActivity.spid);
		values.put("ip",MainActivity.hostIp);//新加ip参数
		values.put("id", dbsxInfo.getId());
		values.put("sncode", dbsxInfo.getSncode());
		values.put("itemname", dbsxInfo.getItemname());
		values.put("username", dbsxInfo.getUsername());
		long rowid = mDb.insert(table, null, values);
		Log.i("DBWrapper", "添加入数据库第几行          "+rowid);
	}
	
	/**
	 * 部门办件
	 * create table bmbj (spid text,orgid text,tuiban text,zuofei text,shanchu text,zhuanbao text,bujiaobulai text," +
	 *				"zixun text,banjie text)
	 * @param table
	 * @param list
	 * list必须按照顺序插入数据
	 */
	public void insertBmbj(String table,ArrayList<String> list) {
		
		ContentValues values = new ContentValues();
		values.put("spid", MainActivity.spid);
		values.put("ip",MainActivity.hostIp);//新加ip参数
		values.put("tuiban", list.get(0));
		values.put("zuofei", list.get(1));
		values.put("zixun", list.get(2));
		values.put("bujiaobulai", list.get(3));
		values.put("shanchu", list.get(4));
		values.put("banjie", list.get(5));
		values.put("zhuanbao", list.get(6));
		long rowid = mDb.insert(table, null, values);
		Log.i("DBWrapper", "添加入数据库第几行          "+rowid);
	}
	
	/**
	 * 部门办件--退回办理/作废办结/咨询办结/补交不来/删除办结/正常办结/转报办结
	 * create table bmbj_thbl (spid text,sncode text,itemname text,username text,endtime text,statedesc text)
	 * @param table
	 * @param dbsxInfo
	 */
	public void insertBmbj(String table,DbsxInfo dbsxInfo) {
		ContentValues values = new ContentValues();
		values.put("spid", MainActivity.spid);
		values.put("ip",MainActivity.hostIp);//新加ip参数
		values.put("id",dbsxInfo.getId());
		values.put("sncode", dbsxInfo.getSncode());
		values.put("endtime", dbsxInfo.getEndtime());
		values.put("itemname", dbsxInfo.getItemname());
		values.put("username", dbsxInfo.getUsername());
		values.put("statedesc", dbsxInfo.getStatedesc());
		long rowid = mDb.insert(table, null, values);
		Log.i("DBWrapper", "添加入数据库第几行          "+rowid);
	}
	
	/**
	 * 站内消息/已收和已发
	 * create table znxx_ys (spid text,id text,title text,content text,sendtime text,sender_name text,isreceived text)
	 * @param table
	 * @param znxxinfo
	 */
	public void insertZnxx(String table,ZnxxInfo znxxinfo) {
		ContentValues values = new ContentValues();
		values.put("spid", MainActivity.spid);
		values.put("ip",MainActivity.hostIp);//新加ip参数
		values.put("id", znxxinfo.getId());
		values.put("title", znxxinfo.getTitle());
		values.put("content", znxxinfo.getContent());
		values.put("sendtime", znxxinfo.getSendtime());
		values.put("sender_name", znxxinfo.getSender_name());
		values.put("isreceived", znxxinfo.getIsreceived());
		long rowid = mDb.insert(table, null, values);
		Log.i("DBWrapper", "添加入数据库第几行          "+rowid);
	}
}
