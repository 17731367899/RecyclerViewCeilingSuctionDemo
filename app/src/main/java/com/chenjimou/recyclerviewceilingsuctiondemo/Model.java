package com.chenjimou.recyclerviewceilingsuctiondemo;

public class Model {
    private String name;
    private String GroupName;
    public Model(String name, String groupName) {
        this.name = name;
        GroupName = groupName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGroupName() {
        return GroupName;
    }
    public void setGroupName(String groupName) {
        GroupName = groupName;
    }
}
