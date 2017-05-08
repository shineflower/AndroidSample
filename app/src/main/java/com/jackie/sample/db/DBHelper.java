/*
 * @Title DBHelper.java
 * @Copyright Copyright 2010-2015 Yann Software Co,.Ltd All Rights Reserved.
 * @Description��
 * @author Yann
 * @date 2015-8-8 ����10:48:31
 * @version 1.0
 */
package com.jackie.sample.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jackie on 2016/4/6.
 * 数据库
 */
public class DBHelper extends SQLiteOpenHelper {
	private static final String DB_NAME = "download.db";
	private static final int VERSION = 1;
	private static final String SQL_CREATE = "create table thread_info(_id integer primary key autoincrement," +
			"id integer, url text, start integer, end integer, progress integer)";
	private static final String SQL_DROP = "drop table if exists thread_info";

	private static DBHelper mDBHelper = null;

	private DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	public static DBHelper getInstance(Context context) {
		if (mDBHelper == null) {
			synchronized (DBHelper.class) {
				if (mDBHelper == null) {
					mDBHelper = new DBHelper(context);
				}
			}
		}

		return mDBHelper;
	}
	
	/**
	 * @see SQLiteOpenHelper#onCreate(SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE);
	}

	/**
	 * @see SQLiteOpenHelper#onUpgrade(SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DROP);
		db.execSQL(SQL_CREATE);
	}
}
