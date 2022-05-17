package com.school.iqdigit.Model;

public class StudgroupsItem{
	private String group_name;
	private int id;

	public String getGroupName(){
		return group_name;
	}

	public int getId(){
		return id;
	}

	@Override
	public String toString() {
		return group_name;
	}
}
