package com.example.attendance.contants;

public enum ReviewType {
	PASS("Pass"),//
	REVIEWING("Reviewing"),//
	REJECT("Reject"),//
	REVOKE("Revoke");
	
	private String type;
	
	private ReviewType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	public static String parser( String type) {
		for(ReviewType item:ReviewType.values()) {
			if(type.equalsIgnoreCase(item.getType())) {
				return item.getType();
			}
		}
		return type;
	}
	
	

}
