package com.example.attendance.constants;

public enum JobPosition {

	ADMIN(99, "Admin"), //
	SUPERVISOR(21, "Supervisor"), //
	DIRECTOR(20, "Director"), //
	SENIOR(2, "Senior"), //
	GETNERAL(1, "Getneral"),//

	;

	private int permission;

	private String title;

	// �غc��k
	private JobPosition(int permission, String title) {
		this.permission = permission;
		this.title = title;
	}

	public int getPermission() {
		return permission;
	}

	public String getTitle() {
		return title;
	}

	// �ھڥ~���� title �ѼƭȨ��o�������v��(permission)
	// �i�H�T�{ title �ѼƭȬO�_�O�w�q�b�� JobPosition ��
	// �[ static �~��I�s
	public static int parser(String title) {
		for (JobPosition item : JobPosition.values()) {
			if (title.equalsIgnoreCase(item.getTitle())) {
				return item.getPermission();
			}
		}
		return 0;
	}

	public static int reviewPermission = 20;

	public static boolean hasGetStaffInfoPermission(String title) {
		int callerPermission = parser(title);
		return callerPermission >= reviewPermission ? true : false;
	}
}
