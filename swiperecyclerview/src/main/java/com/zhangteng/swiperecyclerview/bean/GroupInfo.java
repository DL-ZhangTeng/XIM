package com.zhangteng.swiperecyclerview.bean;

/**
 * Created by swing on 2018/4/12.
 */
public class GroupInfo {
    private int groupNum;
    private String title;
    private int position;
    private int total;

    public GroupInfo() {
    }

    public GroupInfo(int groupNum, String title, int position, int total) {
        this.groupNum = groupNum;
        this.title = title;
        this.position = position;
        this.total = total;
    }

    public boolean isFirst() {
        return position == 0;
    }

    public boolean isLast() {
        return position == total - 1;
    }

    public int getGroupNum() {
        return groupNum;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
