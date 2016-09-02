package com.example.databasetest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class DatabaseProvider extends ContentProvider{

	public static final int BOOK_DIR=0;
	public static final int BOOK_ITEM=1;
	public static final int CATEGORY_DIR=2;
	public static final int CATEGORY_ITEM=3;
	public static final String AUTHORITY="com.example.databasetest.provider";
	private static UriMatcher uriMatcher;
	private MyDatabaseHelper dbHelper;
	static{
		uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "Book",BOOK_DIR);
		uriMatcher.addURI(AUTHORITY, "Book/#",BOOK_ITEM);
		uriMatcher.addURI(AUTHORITY, "Category2",CATEGORY_DIR);
		uriMatcher.addURI(AUTHORITY, "Category2/#",CATEGORY_ITEM);
	}
	@Override
	public boolean onCreate() {
		//当存在contentresolver要访问我们程序中的数据是，内容提供器才会被初始化
		dbHelper=new MyDatabaseHelper(getContext(),"BookStore.db",null,2);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// 查询数据
		SQLiteDatabase db=dbHelper.getReadableDatabase();
		Cursor cursor=null;//cursor方法返回cursor
		switch(uriMatcher.match(uri)){
		case BOOK_DIR:
			cursor=db.query("Book", projection, selection, selectionArgs, null,null,sortOrder);
			break;
		case BOOK_ITEM:
			//getPathSegments()是把权限之后的字符串(路径)以"/"为分隔符分成数组
			//例如 content://com.example.databasetest.provider/book/2 "book"和"2"组成一个数组
			String bookId=uri.getPathSegments().get(1);
			cursor=db.query("Book", projection, "id=?", new String[]{bookId}, null, null, sortOrder);
			break;
		case CATEGORY_DIR:
			cursor=db.query("Category2", projection, selection, selectionArgs, null,null,sortOrder);
			break;
		case CATEGORY_ITEM:
			String categoryId=uri.getPathSegments().get(1);
			cursor=db.query("Category2", projection, "id=?", new String[]{categoryId}, null, null, sortOrder);
			break;
		}
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO 自动生成的方法存根
		switch(uriMatcher.match(uri)){
		case BOOK_DIR:
			return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.book";
			
		case BOOK_ITEM:
			return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.book";
		case CATEGORY_DIR:
			return "vnd.android.cursor.dir/vnd.com.example.databasetest.provider.category2";
		case CATEGORY_ITEM:
			return "vnd.android.cursor.item/vnd.com.example.databasetest.provider.category2";
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// 添加数据
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		Uri uriReturn=null;//insert()方法返回插入条款的uri
		switch(uriMatcher.match(uri)){
		case BOOK_DIR:			
		case BOOK_ITEM:
			long newBookId=db.insert("Book", null, values);
			//得到uri字符串后再用parse函数解析成Uri对象
			uriReturn=Uri.parse("content://"+AUTHORITY+"/book/"+newBookId);
			break;
		case CATEGORY_DIR:			
		case CATEGORY_ITEM:
			long newCategoryId=db.insert("Category2", null, values);
			uriReturn=Uri.parse("content://"+AUTHORITY+"/category/"+newCategoryId);
			break;
		}
		Log.v("test", "uriReturn="+uriReturn);
		return uriReturn;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// 删除数据  delete()方法返回被删除的行数
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		int deletedRows=0;
		switch(uriMatcher.match(uri)){
		case BOOK_DIR:			
			deletedRows=db.delete("Book", selection, selectionArgs);
			break;
		case BOOK_ITEM:
			String bookId=uri.getPathSegments().get(1);
			deletedRows=db.delete("Book", "id=?", new String[]{bookId});
			break;
		case CATEGORY_DIR:		
			deletedRows=db.delete("Category2", selection, selectionArgs);
			break;
		case CATEGORY_ITEM:
			String categoryId=uri.getPathSegments().get(1);
			deletedRows=db.delete("Category2", "id=?", new String[]{categoryId});
			break;
		}
		return deletedRows;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// 更新数据
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		//update函数返回被更新的行数
		int updatedRows=0;
		switch(uriMatcher.match(uri)){
		case BOOK_DIR:			
			updatedRows=db.update("Book", values, selection, selectionArgs);
			break;
		case BOOK_ITEM:
			String bookId=uri.getPathSegments().get(1);
			updatedRows=db.update("Book", values, "id=?", new String[]{bookId});
			break;
		case CATEGORY_DIR:		
			updatedRows=db.update("Category2", values, selection, selectionArgs);
			break;
		case CATEGORY_ITEM:
			String categoryId=uri.getPathSegments().get(1);
			updatedRows=db.update("Category2", values, "id=?", new String[]{categoryId});
			break;
		}
		return updatedRows;
	}

}
