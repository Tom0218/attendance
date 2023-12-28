package com.example.attendance.constants;

public enum LeaveType {

	PERSONAL("Personl"), //
	SICK("Sick"), //
	OFFICIAL("Official"), //
	ANNUAL("Annual"),//
	;

	private String type;

	private LeaveType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public static String parser(String type) {
		for (LeaveType item : LeaveType.values()) {
			if (type.equalsIgnoreCase(item.getType())) {
				return item.getType();
			}
		}
		return null;
	}

	public static boolean needCertification(String type) {
		if (type.equalsIgnoreCase(LeaveType.OFFICIAL.getType()) 
				|| type.equalsIgnoreCase(LeaveType.SICK.getType())) {
			return true;
		}
		return false;
	}
}
