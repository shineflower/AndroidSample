package com.jackie.sample.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2016/1/12.
 * 节点
 */
public class Node {
    private int id;
    /**
     * 根节点的pid = 0;
     * 没有指向任何其他的父节点
     */
    private int pid = 0;
    private String name;
    /**
     * 树的层级
     */
    private int level;
    private int icon;
    /**
     * 是否展开
     */
    private boolean isExpand;
    private Node parent;
    private List<Node> children = new ArrayList<>();

    public Node(int id, int pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public List<Node> getChildren() {
        return children;
    }

    public Node getParent() {
        return parent;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    /**
     * 得到当前节点的层级
     * @return
     */
    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    public int getPid() {
        return pid;
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

    public void setLevel(int level) {
        this.level = level;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;

        if (!isExpand) {
            for (Node node : children) {
                //将所有子节点的展开状态都设置成false
                node.setExpand(false);
            }
        }
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    /**
     * 是否是根节点
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 是否是叶子节点
     * @return
     */
    public boolean isLeaf() {
        return children.size() == 0;
    }

    /**
     * 判断当前父节点的状态(是否是展开)
     * @return
     */
    public boolean isParentExpand() {
        if (parent == null)
            return false;
        return parent.isExpand();
    }
}
