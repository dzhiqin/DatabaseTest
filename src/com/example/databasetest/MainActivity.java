package com.example.databasetest;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	private Button btnCreate;
	private Button btnInsert;
	private Button btnUpdate;
	private Button btnDelete;
	private Button btnQuery;
	private Button btnReplace;
	private MyDatabaseHelper dbHelper;
	private SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dbHelper=new MyDatabaseHelper(this,"BookStore.db",null,2);
		
		btnCreate=(Button)findViewById(R.id.btn_create);
		btnInsert=(Button)findViewById(R.id.btn_insert);
		btnUpdate=(Button)findViewById(R.id.btn_update);
		btnDelete=(Button)findViewById(R.id.btn_delete);
		btnQuery=(Button)findViewById(R.id.btn_query);
		btnReplace=(Button)findViewById(R.id.btn_replace);
		
		btnCreate.setOnClickListener(this);
		btnInsert.setOnClickListener(this);
		btnUpdate.setOnClickListener(this);
		btnDelete.setOnClickListener(this);
		btnQuery.setOnClickListener(this);
		btnReplace.setOnClickListener(this);
		
		db=dbHelper.getWritableDatabase();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) {
		
		
		ContentValues values=new ContentValues();
		switch(arg0.getId()){
		case R.id.btn_create:
			dbHelper.getWritableDatabase();			
			break;
		case R.id.btn_insert:
		//	SQLiteDatabase db=dbHelper.getWritableDatabase();
			//ContentValues values=new ContentValues();
			//组装第一条数据
			values.put("name", "The Da Vinci Code");
			values.put("author", "Dan Brown");
			values.put("pages", 454);
			values.put("price", 16.9);
			db.insert("Book", null,values);
			values.clear();
			//组装第二条数据
			values.put("name", "The Lost Symble");
			values.put("author", "Dan Brown");
			values.put("pages", 404);
			values.put("price", 26.9);
			db.insert("Book", null,values);
			values.clear();
			break;
		case R.id.btn_update:
			values.put("price", 12);
			db.update("Book", values, "name=?and author=?and pages=?", new String[]{"The Da Vinci Code","Dan Brown","454"});
			values.clear();
			break;
		case R.id.btn_delete:
			db.delete("Book", "price>?", new String[]{"20"});
			break;
		case R.id.btn_query:
			Cursor cursor=db.query("Book", null, null, null, null, null, null);
			while(cursor.moveToNext()){
				String name=cursor.getString(cursor.getColumnIndex("name"));
				String author=cursor.getString(cursor.getColumnIndex("author"));
				int pages=cursor.getInt(cursor.getColumnIndex("pages"));
				double price=cursor.getDouble(cursor.getColumnIndex("price"));
				Log.v("test", "name="+name);
				Log.v("test", "author="+author);
				Log.v("test", "pages="+pages);
				Log.v("test", "price="+price);
			}
			cursor.close();
			break;
		case R.id.btn_replace:
			db.beginTransaction();//开启事物
			try{
				db.delete("Book", null, null);
				if(true){
					//这里手动抛出了一个异常，事物失败
					//throw new NullPointerException();
				}
				values.put("name","Game of thrones");
				values.put("author", "George Martin");
				values.put("pages", 690);
				values.put("price",30.51);
				db.insert("Book", null, values);
				values.clear();
				db.setTransactionSuccessful();//事物执行成功
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				db.endTransaction();//结束事物
			}
			break;
		}
		
	}
}
