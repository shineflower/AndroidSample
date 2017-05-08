/*
 * @Title ThreadDAOImpl.java
 * @Copyright Copyright 2010-2015 Yann Software Co,.Ltd All Rights Reserved.
 * @Description：
 * @author Yann
 * @date 2015-8-8 上午11:00:38
 * @version 1.0
 */
package com.jackie.sample.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jackie.sample.bean.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据访问接口实现
 */
public class DBDaoImpl implements DBDao {
	private DBHelper mHelper = null;

	public DBDaoImpl(Context context) {
		mHelper = DBHelper.getInstance(context);
	}

	@Override
	public synchronized void insert(ThreadInfo threadInfo) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.execSQL("insert into thread_info(id,url,start,end,progress) values(?,?,?,?,?)",
				new Object[]{ threadInfo.getId(), threadInfo.getUrl(),
						threadInfo.getStart(), threadInfo.getEnd(), threadInfo.getProgress() });

		db.close();
	}

	@Override
	public synchronized void delete(String url) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.execSQL("delete from thread_info where url = ?",
				new Object[]{ url });

		db.close();
	}

	@Override
	public synchronized void update(int id, String url, int progress) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.execSQL("update thread_info set progress = ? where id = ? and url = ?",
				new Object[]{ progress, id, url });

		db.close();
	}

	@Override
	public List<ThreadInfo> getThreads(String url) {
		List<ThreadInfo> list = new ArrayList<>();

		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from thread_info where url = ?", new String[]{ url });
		while (cursor.moveToNext()) {
			ThreadInfo threadInfo = new ThreadInfo();
			threadInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
			threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
			threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
			threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
			threadInfo.setProgress(cursor.getInt(cursor.getColumnIndex("progress")));
			list.add(threadInfo);
		}

		cursor.close();
		db.close();

		return list;
	}

	@Override
	public boolean isExist(int id, String url) {
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from thread_info where id = ? and url = ?", new String[]{ id + "", url });
		boolean exist = cursor.moveToNext();
		cursor.close();
		db.close();

		return exist;
	}
}
