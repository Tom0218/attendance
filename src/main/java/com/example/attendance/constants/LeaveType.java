package com.example.attendance.constants;

public enum LeaveType {

	OFFICIAL_LEAVE("Official_leave"), // ����
	PERSONAL_LEAVE("Personl_leave"), //�ư�
	SICK_LEAVE("Sick_leave"), //�f��
	FUNERAL_LEAVE("Funeral leave"),//�ల
	ANNUAL_LEAVE("Annual leave"),// �~��
	MATERNITY_LEAVE("Maternity leave"),//����
	MENSTRUATION_LEAVE("Menstruation leave"),//�Ͳz��
	PRE_MATERNITY_LEAVE("Pre-MaternityLeave")//������
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
