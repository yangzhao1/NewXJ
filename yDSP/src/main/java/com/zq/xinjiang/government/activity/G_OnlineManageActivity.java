package com.zq.xinjiang.government.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zq.xinjiang.BaseActivity;
import com.zq.xinjiang.R;
import com.zq.xinjiang.approval.homeactivity.MainActivity;
import com.zq.xinjiang.approval.utils.MSimpleHttpUtil;
import com.zq.xinjiang.government.adapter.OnlineManageAdapter;
import com.zq.xinjiang.government.entity.ItemDetails;
import com.zq.xinjiang.government.tool.Allports;
import com.zq.xinjiang.government.tool.DownloadFile;
import com.zq.xinjiang.government.tool.FileUploaders;
import com.zq.xinjiang.government.tool.MyLinearLayoutManager;
import com.zq.xinjiang.government.tool.MyUploader;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Administrator on 2017/9/14.
 * 在线办理
 */
@RuntimePermissions
public class G_OnlineManageActivity extends BaseActivity {
    private TextView titleText;
    private TextView back;
    private RecyclerView recyclerView;
    private List<ItemDetails> list = new ArrayList<>();
    private String title = "在线办理";
    private FinalHttp finalHttp ;
    private ItemDetails itemDetails = new ItemDetails();
    private TextView itemname,orgname;
    private List<Map<String,String>> mapList = new ArrayList<Map<String, String>>();;
    private OnlineManageAdapter adapter;
    private TextView submit,idnumber,realname;
    private EditText phone,address;
    private String realname_s,idnumber_s,phone_s,address_s,itemid,iteminstanceid;
    private LinearLayout lin;
    private JSONArray array = null;
    private int fireNumber = 0;//已经选择了的文件数目
    //代表的是第几个文件
    private int one = 1;//选择文件后赋值0.防止重复选择
    private int two = 1;//选择文件后赋值0.防止重复选择
    private int three = 1;//选择文件后赋值0.防止重复选择
    private int four = 1;//选择文件后赋值0.防止重复选择
    private int five = 1;//选择文件后赋值0.防止重复选择
    private int six = 1;//选择文件后赋值0.防止重复选择
    private int seven = 1;//选择文件后赋值0.防止重复选择
    private int eight = 1;//选择文件后赋值0.防止重复选择
    private int nine = 1;//选择文件后赋值0.防止重复选择
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_onlinemanage_main);
        setStatusColor();

        init();
    }

    private void init(){
        finalHttp = new FinalHttp();

        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText(title);
        back = (TextView) findViewById(R.id.back);
        itemname = (TextView) findViewById(R.id.itemname);
        orgname = (TextView) findViewById(R.id.orgname);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        submit = (TextView) findViewById(R.id.submit);
        realname = (TextView) findViewById(R.id.realName);
        realname.setText(usernames);
        idnumber = (TextView) findViewById(R.id.idnumber);
        idnumber.setText(preferences.getString("identitycode","身份号码"));
        phone = (EditText) findViewById(R.id.phone);
        String phone_pre = preferences.getString("linkmobile","电话号码");
        String address_pre = preferences.getString("address","联系地址");
        phone.setText(phone_pre);
        phone.setSelection(phone_pre.length());

        address = (EditText) findViewById(R.id.address);
        address.setText(address_pre);
        address.setSelection(address_pre.length());

        lin = (LinearLayout) findViewById(R.id.lin);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (judge()){
                    uploadThreadTest();
                }
//                setSubmit();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        itemid = getIntent().getStringExtra("id");
        getUrlData(itemid);
    }

    //判断数据是否为空
    private boolean judge(){
        realname_s = realname.getText().toString();
        idnumber_s = idnumber.getText().toString();
        phone_s = phone.getText().toString();
        address_s = phone.getText().toString();

        if (!realname_s.equals("")&&!idnumber_s.equals("")&&!phone_s.equals("")&&!address_s.equals("")){

        }else {
            initToast("请填写完整数据！");
            return false;
        }

        if (isMobileNO(phone_s)){

        }else {
            initToast("电话号码格式不正确");
            return false;
        }

        Log.e("","已经选择了的文件个数：：" + fireNumber +"接口解析出来的文件个数：："+array.length()+"");

        if (array!=null&&array.length()>0){
            if (fireNumber>=array.length()){
                return true;
            }else {
                initToast("请上传需要资料");
                return false;
            }
        }else {
            return true;
        }
    }

    private void getUrlData(String id){

        String url = Allports.getItemsDetailsUrl("api","getitemdetail",id);

        Log.i("事项详情接口：",url);
        if (MSimpleHttpUtil.isCheckNet(this)) {
            dialog = showLoadingPop();
            finalHttp.get(url, new AjaxCallBack<String>() {

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    dialog.dismiss();
                    printError(errorNo);
                }

                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    dialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(t);
                        int errno = jsonObject.getInt("errno");
                        if (errno == 0) {
                            JSONObject ob = jsonObject.getJSONObject("item");
                            itemDetails.setItemcode(ob.get("itemcode").toString());
                            itemDetails.setItemproperty(ob.get("itemproperty").toString());
                            itemDetails.setItemname(ob.getString("itemname"));
                            itemDetails.setWindowid(ob.get("windowid").toString());
                            itemDetails.setOrgname(ob.get("orgname").toString());
                            itemDetails.setItemtype(ob.get("itemtype").toString());
                            itemDetails.setLimittime(ob.get("limittime").toString());
                            itemDetails.setLimitlegaltime(ob.get("limitlegaltime").toString());
                            itemDetails.setOrgphone(ob.get("phone").toString());
                            itemDetails.setRequirements(ob.get("requirements").toString());
                            itemDetails.setApplypursuant(ob.get("applypursuant").toString());
                            itemDetails.setChargepursuant(ob.get("chargequantity").toString());

                            array = ob.getJSONArray("docs"); //表格下载
                            HashMap map ;
                            for (int i = 0;i<array.length();i++){
                                map = new HashMap();
                                JSONObject o = (JSONObject) array.get(i);
                                String filename = o.getString("name");//文件名称
                                String path = o.getString("directory");//文件路径  sourceid
                                String Canonlinedownload = o.getString("canonlinedownload");
                                String Canonlineupload = o.getString("canonlineupload");
                                String id = o.getString("id");

                                map.put("filename",filename);
                                map.put("path",path);
                                map.put("sourceid",id);
                                map.put("phonePath","");

                                mapList.add(map);
                            }

                            itemDetails.setDocs(mapList);

                            list.add(itemDetails);

                            setData();
                        } else {
                            String errors = jsonObject.getJSONArray("errors").getString(0);
                            initToast(errors);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            initToast("请检查网络是否连接！");
        }
    }

    //这个方法没有用
    File sdDir;
    private void setSubmit(){
        sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if(sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        final HashMap<String,String> map = new HashMap<String,String>();

        final List<Map<String,Object>> listmap = new ArrayList<>();

        //文件地址转换文件对象
        File file;
        Map map1;

        for (int i = 0;i<mapList.size();i++){
            String id = mapList.get(i).get("sourceid");
            String phonePath = mapList.get(i).get("phonePath");
            if (phonePath.equals("")){
//                return;
            }
            file = new File(phonePath);
            map1 = new HashMap();
            map1.put("id",id);
            map1.put("file",file);
            listmap.add(map1);
        }

        Log.i("文件list：",listmap.size()+"");

        map.put("mod","op");
        map.put("act","applyofsubmititem");
        map.put("id","00101");
        map.put("iteminstanceid","0");
        map.put("itemdefid",itemid);
        map.put("username",realname_s);
        map.put("identitycode",idnumber_s);
        map.put("linkmobile",phone_s);
        map.put("address",address_s);
        //mod=op&act=applyofsubmititem&id=00101&iteminstanceid=0&itemdefid=2567&username=杨钊&identitycode=11111111111&linkmobile=13222222&address=1111111111
        new Thread(){
            @Override
            public void run() {
                FileUploaders.upload("http://192.168.1.103/heyang/webservices/Json.aspx?", listmap, map, new FileUploaders.FileUploadListener() {
                    @Override
                    public void onProgress(long pro, double precent) {
                        Log.i("cky", precent+"");
                    }

                    @Override
                    public void onFinish(int code, String res, Map<String, List<String>> headers) {
                        Log.i("ckysssss", "成功");
                    }
                });
            }
        }.start();
    }

    Dialog dialog;
    //多文件上传
    public void uploadThreadTest() {
        dialog = showLoadingPop();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    upload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    private void upload() {
//        String url = "http://192.168.1.103/heyang/webservices/Json.aspx?";
        String url = Allports.getPorts;
        List<String> fileList = getCacheFiles();
        final HashMap<String,String> map = new HashMap<String,String>();

        map.put("mod","op");
        map.put("act","applyofsubmititem");
        map.put("id",loginid);
        map.put("iteminstanceid","0");
        map.put("itemdefid",itemid);
        map.put("username",realname_s);
        map.put("identitycode",idnumber_s);
        map.put("linkmobile",phone_s);
        map.put("address",address_s);

//        if (mapList == null) {
//            myHandler.sendEmptyMessage(-1);
//        }else {

        Log.e("在线办理数据：",url+"   "+map.toString());
            MyUploader myUpload = new MyUploader();
            //同步请求，直接返回结果，根据结果来判断是否成功。
            String reulstCode = myUpload.MyUploadMultiFileSync(url, mapList, map);
            Log.i("", "upload reulstCode: " + reulstCode);
            myHandler.sendEmptyMessage(0);
//        }
    }

    private List<String> getCacheFiles() {
        List<String> fileList = new ArrayList<String>();
        File catchPath = this.getCacheDir();

        if (catchPath!=null && catchPath.isDirectory()) {

            File[] files = catchPath.listFiles();
            if (files == null || files.length<1) {
                return null;
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() && files[i].getAbsolutePath().endsWith(".jpg")) {
                    fileList.add(files[i].getAbsolutePath());
                }
            }
            return fileList;
        }
        return null;
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.i("","handleMessage msg===" + msg);
            dialog.dismiss();
            if (msg.what == -1) {
                showResultPop("办理失败",false,lin);
            }else {
                showResultPop("办理成功",true,lin);
            }

        }

    };


    private void setData(){
        MyLinearLayoutManager layoutManager = new MyLinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OnlineManageAdapter(this,mapList);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setSelectFile(new OnlineManageAdapter.SelectFile() {
            @Override
            public void setSelectFile(int pos) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                MainActivityPermissionsDispatcher.showCameraWithPermissionCheck(this);
                startActivityForResult(intent,pos);
            }
        });
        itemname.setText(itemDetails.getItemname());
        orgname.setText(itemDetails.getOrgname());
    }

    String path = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File file = null;
        if (resultCode == this.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                path = uri.getPath();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                getPath(this, uri);
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
            }

            initToast(path);
        }
        Map map;

        if (!TextUtils.isEmpty(path)){
            switch (requestCode){
                case 0:
                    fireNumber= fireNumber+one;
                    one=0;
                    map = (HashMap) mapList.get(0);
                    map.put("phonePath",path);

                    adapter.notifyDataSetChanged();
                    Log.e("","选择文件后返回时：：："+fireNumber+"  " + path);
                    break;
                case 1:
                    map = (HashMap) mapList.get(1);
                    map.put("phonePath",path);

                    adapter.notifyDataSetChanged();
                    fireNumber= fireNumber+two;
                    two=0;
                    break;
                case 2:
                    map = (HashMap) mapList.get(2);
                    map.put("phonePath",path);

                    adapter.notifyDataSetChanged();
                    fireNumber= fireNumber+three;
                    three=0;
                    break;
                case 3:
                    map = (HashMap) mapList.get(3);
                    map.put("phonePath",path);

                    adapter.notifyDataSetChanged();
                    fireNumber= fireNumber+four;
                    four=0;
                    break;
                case 4:
                    map = (HashMap) mapList.get(4);
                    map.put("phonePath",path);

                    adapter.notifyDataSetChanged();
                    fireNumber= fireNumber+five;
                    five=0;
                    break;
                case 5:
                    map = (HashMap) mapList.get(5);
                    map.put("phonePath",path);

                    adapter.notifyDataSetChanged();
                    fireNumber= fireNumber+six;
                    six=0;
                    break;
                case 6:
                    map = (HashMap) mapList.get(6);
                    map.put("phonePath",path);

                    adapter.notifyDataSetChanged();
                    fireNumber= fireNumber+seven;
                    seven=0;
                    break;
                case 7:
                    map = (HashMap) mapList.get(7);
                    map.put("phonePath",path);

                    adapter.notifyDataSetChanged();
                    fireNumber= fireNumber+eight;
                    eight=0;
                    break;
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(null!=cursor&&cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     */
    @SuppressLint("NewApi")
    public void getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    path = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                G_OnlineManageActivityPermissionsDispatcher.getDataColumnWithCheck(this,getApplicationContext(),contentUri,null,null);
//                getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                G_OnlineManageActivityPermissionsDispatcher.getDataColumnWithCheck(this,getApplicationContext(),contentUri,selection,selectionArgs);

//                getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            G_OnlineManageActivityPermissionsDispatcher.getDataColumnWithCheck(this,getApplicationContext(),uri,null,null);

//            getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private String TAG = "";

//    @SuppressLint("NewApi")
    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                            null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                path = cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }


//---------------------
//    作者：LakeSideHu
//    来源：CSDN
//    原文：https://blog.csdn.net/likesidehu/article/details/52668879
//    版权声明：本文为博主原创文章，转载请附上博文链接！


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        G_OnlineManageActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

}
