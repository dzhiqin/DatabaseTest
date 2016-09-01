package com.example.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper{
	//	建表语句 创建一个“Book”为名的表格
	public static final String CREATE_BOOK="create table Book("
			+ "id integer primary key autoincrement, "
			+ "author text, "
			+ "price real, "
			+ "pages integer, "
			+ "name text )";
	//创建一个“category2”为名的表格
	public static final String CREATE_CATEGORY="create table Category2("
			+ "id integer primary key autoincrement, "
			+ "category_name text, "
			+ "category_code integer )";
	//创建新表格语句
	public static final String CREATE_GONGYING="create table Gongying("
			+ "id integer primary key autoincrement, "
			+ "gongying_name text, "
			+ "gongying_code integer,"
			+ "gongying_price real  )";
	private Context mContext;
	public MyDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		mContext=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_BOOK);
		db.execSQL(CREATE_CATEGORY);
		//db.execSQL(CREATE_GONGYING);
		Toast.makeText(mContext, "Create succeed", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		switch(oldVersion){
		case 1:
			db.execSQL(CREATE_GONGYING);
		case 2:
			db.execSQL("alter table Book add column category_id");
			default:
				break;
		}
		
	}

}
