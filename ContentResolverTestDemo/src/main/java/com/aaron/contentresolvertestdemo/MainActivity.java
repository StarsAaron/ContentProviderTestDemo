package com.aaron.contentresolvertestdemo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Contentprovider 测试样例，配套ContentResolverTestDemo 使用
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "ContentResolver---";
    private Button btnQuery, btnDelete, btnInsert, btnUpdate, btnThirdPart, btnUseFirstURI, btnUseSecondURI;
    private TextView mtv,tv_msg;
    public static final String AUTHORITY = "com.aaron.contentprovidertestdemo.PeopleContentProvider";
    public static final Uri CONTENT_URI_FIRST = Uri.parse("content://" + AUTHORITY + "/first");
    public static final Uri CONTENT_URI_SECOND = Uri.parse("content://" + AUTHORITY + "/second");
    public static Uri mCurrentURI = CONTENT_URI_FIRST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDelete = (Button) findViewById(R.id.delete);
        btnInsert = (Button) findViewById(R.id.insert);
        btnQuery = (Button) findViewById(R.id.query);
        btnUpdate = (Button) findViewById(R.id.update);
        btnThirdPart = (Button) findViewById(R.id.thirdPart);
        btnUseFirstURI = (Button) findViewById(R.id.first_uri);
        btnUseSecondURI = (Button) findViewById(R.id.second_uri);
        mtv = (TextView) findViewById(R.id.tv);
        mtv.setText("当前URI:" + mCurrentURI.toString());
        tv_msg = (TextView) findViewById(R.id.tv_msg);

        btnDelete.setOnClickListener(this);
        btnInsert.setOnClickListener(this);
        btnQuery.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnThirdPart.setOnClickListener(this);
        btnUseFirstURI.setOnClickListener(this);
        btnUseSecondURI.setOnClickListener(this);
    }

    /**
     * 使用Uri隐式调用Activity
     */
    private void thirdPart() {
        Intent intent = new Intent();
        intent.setAction("harvic.test.qijian");
        intent.setData(mCurrentURI);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 插入数据
     */
    private void insert() {
        ContentValues values = new ContentValues();
        values.put("name", "hello");
        values.put("detail", "my name is harvic");
        Uri uri = this.getContentResolver().insert(mCurrentURI, values);
        Log.e(TAG, uri.toString());
        tv_msg.setText("insert返回Uri："+uri.toString());
    }

    /**
     * 查找数据
     */
    private void query() {
        Cursor cursor = this.getContentResolver().query(mCurrentURI, null, null, null, null);
        Log.e(TAG, "count=" + cursor.getCount());
        cursor.moveToFirst();
        tv_msg.setText("query数据：\n");
        while (!cursor.isAfterLast()) {
            String table = cursor.getString(cursor.getColumnIndex("table_name"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String detail = cursor.getString(cursor.getColumnIndex("detail"));
            Log.e(TAG, "table_name:" + table);
            Log.e(TAG, "name: " + name);
            Log.e(TAG, "detail: " + detail);
            cursor.moveToNext();
            tv_msg.setText(tv_msg.getText()+"\ntable_name:"+table+"\nname: "+name+"\ndetail: "+detail+"\n");
        }
        cursor.close();
    }

    /**
     * 更新数据
     */
    private void update() {
        ContentValues values = new ContentValues();
        values.put("detail", "my name is harvic !!");
        int count = this.getContentResolver().update(mCurrentURI, values, "_id = 1", null);
        Log.e(TAG, "count=" + count);
        tv_msg.setText("update count="+count);
        query();
    }

    /**
     * 删除数据
     */
    private void delete() {
        int count = this.getContentResolver().delete(mCurrentURI, "_id = 1", null);
        Log.e(TAG, "count=" + count);
        tv_msg.setText("delete count="+count);
        query();
    }

    /**
     * 切换Uri
     * @param uri
     */
    private void switchURI(Uri uri) {
        mCurrentURI = uri;
        mtv.setText("当前URI:" + mCurrentURI.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_uri: {
                switchURI(CONTENT_URI_FIRST);
            }
            break;
            case R.id.second_uri: {
                switchURI(CONTENT_URI_SECOND);
            }
            break;
            case R.id.delete: {
                delete();
            }
            break;
            case R.id.insert: {
                insert();
            }
            break;
            case R.id.query: {
                query();
            }
            break;
            case R.id.update: {
                update();
            }
            break;
            case R.id.thirdPart: {
                thirdPart();
            }
            break;
            default:
                break;
        }

    }
}
