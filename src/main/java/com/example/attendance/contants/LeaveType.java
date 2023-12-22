package com.example.attendance.contants;

public enum LeaveType {
	PERSONAL("Personal"),//
	SICK("Sick"),//
	OFFICIAL("Official"),//
	ANNUAL("Annual");
	
	private String type;
	
	private LeaveType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public static String parser( String type) {
		for(LeaveType item:LeaveType.values()) {
			if(type.equalsIgnoreCase(item.getType())) {
				return item.getType();
			}
		}
		return type;
	}
	
	public static boolean needCertification(String type) {
		if(type.equalsIgnoreCase(LeaveType.OFFICIAL.getType())||
				type.equalsIgnoreCase(LeaveType.SICK.getType())) {
			return true;
		}
		return false;
			
	};
	
	
	

}
