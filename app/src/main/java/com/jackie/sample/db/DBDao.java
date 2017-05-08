package com.jackie.sample.db;

import com.jackie.sample.bean.ThreadInfo;

import java.util.List;

/**
 * Created by Jackie on 2016/4/6.
 * 数据库访问接口
 */
public interface DBDao {
	/** 
	 * 插入线程信息
	 */ 
	public void insert(ThreadInfo threadInfo);

	/** 
	 * 删除线程信息
	 */ 
	public void delete(String url);

	/** 
	 * 更新线程下载进度
	 */ 
	public void update(int id, String url, int progress);

	/** 
	 * 查询文件的线程信息
	 */ 
	public List<ThreadInfo> getThreads(String url);

	/** 
	 * 线程信息是否存在
	 */
	public boolean isExist(int id, String url);
}
