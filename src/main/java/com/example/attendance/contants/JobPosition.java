package com.example.attendance.contants;

public enum JobPosition {
	
	ADMIN(99,"Admin"),//
	SUPERVISOR(21,"Supervisor"),//
	DIRECTOR(20,"Derictor"),//
	SENIOR(2,"Senior"),//
	GENERAL(1,"General");
	
	
	private int permission;
	
	private String title;

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
	
	//�ھڥ~���Ƕi�Ӫ� title ���o�������v��(permission)!!
	//�i�H�T�{ titl e�ѼƬO�_�w�q�b JobPosition��!!
	//�]���L�knew
	//static�i�����ϥ�
	public static int parser(String title) {
		for(JobPosition item : JobPosition.values()) {
			if(title.equalsIgnoreCase(item.getTitle())) {
				return item.getPermission();
			}
		}
		return 0;
	}
	
	private static int reviewPermission = 20;
	
	public static boolean hasReviewPermission(String title) {
		int callerPermission = parser(title);
		return callerPermission >= reviewPermission ? true : false;
	}
	
	

}
