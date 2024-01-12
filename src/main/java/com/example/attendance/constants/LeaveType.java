package com.example.attendance.constants;

public enum LeaveType {

	OFFICIAL("Official"), // ����
	PERSONAL("Personal"), // �ư�
	SICK("Sick"), // �f��
	FUNERAL("Funeral"), // �ల
	ANNUAL("Annual"), // �S��
	MATERNITY("Maternity"), // ����
	MENSTRUATION("Menstruation"), // �Ͳz��
	PRE_MATERNITY("Pre_Maternity"),// ������
	MARITAl("Marital")// ������
	;

	private String type;

	private LeaveType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	// �ھڥ~���� type �ѼƭȨ��o�������v��(permission)
	// �i�H�T�{ type �ѼƭȬO�_�O�w�q�b�� JobPosition ��
	// �[ static �~��I�s
	public static String parser(String type) {
		for (LeaveType item : LeaveType.values()) {
			if (type.equalsIgnoreCase(item.getType())) {
				return item.getType();
			}
		}
		return null;
	}

	//�ݭn�ҩ���󪺰��O
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
