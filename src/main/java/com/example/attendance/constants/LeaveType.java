package com.example.attendance.constants;

public enum LeaveType {

	OFFICIAL("Official"), // そ安
	PERSONAL("Personal"), // ㄆ安
	SICK("Sick"), // 痜安
	FUNERAL("Funeral"), // 赤安
	ANNUAL("Annual"), // 疭ヰ
	MATERNITY("Maternity"), // 玻安
	MENSTRUATION("Menstruation"), // ネ瞶安
	PRE_MATERNITY("Pre_Maternity"),// 抄玻安
	MARITAl("Marital")// 抄玻安
	;

	private String type;

	private LeaveType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	// 沮场 type 把计眔癸莱舦(permission)
	// 絋粄 type 把计琌琌﹚竡 JobPosition い
	//  static ㊣
	public static String parser(String type) {
		for (LeaveType item : LeaveType.values()) {
			if (type.equalsIgnoreCase(item.getType())) {
				return item.getType();
			}
		}
		return null;
	}

	//惠璶靡ゅン安
	public static boolean needCertification(String type) {
		if (type.equalsIgnoreCase(LeaveType.OFFICIAL.getType())
				|| type.equalsIgnoreCase(LeaveType.SICK.getType())
				|| type.equalsIgnoreCase(LeaveType.MENSTRUATION.getType())
				|| type.equalsIgnoreCase(LeaveType.PRE_MATERNITY.getType())) {
			return true;
		}
		return false;
	}
}
