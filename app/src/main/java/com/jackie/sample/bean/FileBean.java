package com.jackie.sample.bean;

import com.jackie.sample.annotation.TreeNodeId;
import com.jackie.sample.annotation.TreeNodeName;
import com.jackie.sample.annotation.TreeNodePid;

/**
 * Created by Jackie on 2016/1/12.
 * 文件实体类
 */
public class FileBean {
    @TreeNodeId(type = String.class)
    private int id;
    /**
     * 指向父节点的标记
     */
    @TreeNodePid
    private int pid;
    @TreeNodeName
    private String name;
    private String description;

    public FileBean(int id, int pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
