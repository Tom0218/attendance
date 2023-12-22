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
	
	//根據外部傳進來的 title 取得對應的權限(permission)!!
	//可以確認 titl e參數是否定義在 JobPosition中!!
	//因為無法new
	//static可直接使用
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
