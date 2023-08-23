package com.school.iqdigit.Model;

import java.util.List;

public class GroupsResponse{
	private List<StudgroupsItem> studgroups;
	private boolean error;

	public List<StudgroupsItem> getStudgroups(){
		return studgroups;
	}

	public boolean isError(){
		return error;
	}
}