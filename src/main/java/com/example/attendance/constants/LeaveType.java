package com.example.attendance.constants;

public enum LeaveType {

	OFFICIAL_LEAVE("Official_leave"), // そ安
	PERSONAL_LEAVE("Personl_leave"), //ㄆ安
	SICK_LEAVE("Sick_leave"), //痜安
	FUNERAL_LEAVE("Funeral leave"),//赤安
	ANNUAL_LEAVE("Annual leave"),// 安
	MATERNITY_LEAVE("Maternity leave"),//玻安
	MENSTRUATION_LEAVE("Menstruation leave"),//ネ瞶安
	PRE_MATERNITY_LEAVE("Pre-MaternityLeave")//抄玻安
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
		if (type.equalsIgnoreCase(LeaveType.OFFICIAL_LEAVE.getType()) 
				|| type.equalsIgnoreCase(LeaveType.SICK_LEAVE.getType())) {
			return true;
		}
		return false;
	}
}
