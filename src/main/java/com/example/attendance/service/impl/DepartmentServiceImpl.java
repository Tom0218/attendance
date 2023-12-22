package com.example.attendance.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.attendance.contants.RtnCode;
import com.example.attendance.entity.Department;
import com.example.attendance.repository.DepartmentDao;
import com.example.attendance.service.ifs.DepartmentService;
import com.example.attendance.vo.DepartmentCreateReq;
import com.example.attendance.vo.BasicRes;

@Service
public class DepartmentServiceImpl implements DepartmentService{
	
	@Autowired 
	private DepartmentDao dao;

	@Override
	public BasicRes create(DepartmentCreateReq req) {
		//CollectionUtils.isEmpty檢查list是否為空
		if(CollectionUtils.isEmpty(req.getDepList())) {
			return new BasicRes(RtnCode.PARAM_ERROR);
		}
		List<String> idList = new ArrayList<>();
		for(Department item : req.getDepList()){
			//格式檢查id、name
			if(!StringUtils.hasText(item.getId()) || !StringUtils.hasText(item.getName())) {
				return new BasicRes(RtnCode.PARAM_ERROR);
			}
			idList.add(item.getId());
		}
		if(dao.existsByIdIn(idList)) {
			return new BasicRes(RtnCode.ID_HAS_EXISTED);
		}
		dao.saveAll(req.getDepList());
		return new BasicRes(RtnCode.SUCCESSFUL);
	}


}
