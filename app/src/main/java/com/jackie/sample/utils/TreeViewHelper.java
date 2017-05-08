package com.jackie.sample.utils;

import com.jackie.sample.R;
import com.jackie.sample.bean.Node;
import com.jackie.sample.annotation.TreeNodeId;
import com.jackie.sample.annotation.TreeNodeName;
import com.jackie.sample.annotation.TreeNodePid;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jackie on 2016/1/12.
 * 将系统中的数据(普通的实体类)转化成树形结构中的Node(节点)
 * 通过反射加注解获取任意实体对象中的id pid和name属性
 */
public class TreeViewHelper {

    public static <T> List<Node> convertDatas2Nodes(List<T> datas) throws IllegalAccessException {
        List<Node> nodes = new ArrayList<>();
        int id = -1;
        int pid = -1;
        String name = null;

        for (T t : datas) {
            Class clazz = t.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(TreeNodeId.class) != null) {
                    //根据不同类型的id用注解进行不同的类型转化
//                    Class type = field.getAnnotation(TreeNodeId.class).type();
//                    if (type == String.class) {
//                        id = (String) field.get(t);
//                    } else if (type = Integer.class) {
//                        id = field.getInt(t);
//                    }
                    field.setAccessible(true);
                    id = field.getInt(t);
                }

                if (field.getAnnotation(TreeNodePid.class) != null) {
                    field.setAccessible(true);
                    pid = field.getInt(t);
                }

                if (field.getAnnotation(TreeNodeName.class) != null) {
                    field.setAccessible(true);
                    name = (String) field.get(t);
                }
            }

            Node node = new Node(id, pid, name);
            nodes.add(node);
        }

        //设置Node间的节点关系
        for (int i = 0; i < nodes.size(); i++) {
            Node m = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node n = nodes.get(j);
                if (m.getPid() == n.getId()) {
                    n.getChildren().add(m);
                    m.setParent(n);
                } else if (m.getId() == n.getPid()) {
                    m.getChildren().add(n);
                    n.setParent(m);
                }
            }
        }

        //设置图标
        for (Node node : nodes) {
            setNodeIcon(node);
        }

        return nodes;
    }

    /**
     * @param datas
     * @param defaultExpandLevel  默认展开多少层
     * @param <T>
     * @return
     * @throws IllegalAccessException
     */
    public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) throws IllegalAccessException {
        List<Node> nodes = convertDatas2Nodes(datas);
        List<Node> rootNodes = getRootNodes(nodes);

        List<Node> result = new ArrayList<>();
        for (Node node : rootNodes) {
            addNode(result, node, defaultExpandLevel, 1);
        }

        return result;
    }

    /**
     * 把一个节点的所有孩子节点按顺序放入result中
     * @param result
     * @param node
     * @param defaultExpandLevel
     * @param currentLevel
     */
    private static void addNode(List<Node> result, Node node, int defaultExpandLevel, int currentLevel) {
        result.add(node);

        if (defaultExpandLevel >= currentLevel) {
            node.setExpand(true);
        }

        if (node.isLeaf()) {
            return;
        }

        for (Node childNode : node.getChildren()) {
            addNode(result, childNode, defaultExpandLevel, currentLevel + 1);
        }
    }

    /**
     * 过滤出可见的节点
     * @param nodes
     * @return
     */
    public static List<Node> filterVisibleNodes(List<Node> nodes) {
        List<Node> result = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot() || node.isParentExpand()) {
                setNodeIcon(node);
                result.add(node);
            }
        }

        return result;
    }


    /**
     * 过滤出所有的根节点
     * @param nodes
     * @return
     */
    private static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> rootNodes = new ArrayList<>();
        for (Node node : nodes) {
            if (node.isRoot()) {
                rootNodes.add(node);
            }
        }

        return rootNodes;
    }

    private static void setNodeIcon(Node node) {
        if (node.getChildren().size() > 0 && node.isExpand()) {
            node.setIcon(R.drawable.tree_expand);
        } else if (node.getChildren().size() > 0 && !node.isExpand()) {
            node.setIcon(R.drawable.tree_collapse);
        } else {
            node.setIcon(-1);
        }
    }
}
