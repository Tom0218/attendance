package com.example.attendance.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.attendance.constants.RtnCode;
import com.example.attendance.entity.Departments;
import com.example.attendance.repository.DepartmentsDao;
import com.example.attendance.service.ifs.DepartmentsService;
import com.example.attendance.vo.DepartmentsCreateReq;
import com.example.attendance.vo.DepartmentsCreateRes;

@Service
public class DepartmentsServiceImpl implements DepartmentsService {

	@Autowired
	private DepartmentsDao dao;

	@Override
	public DepartmentsCreateRes create(DepartmentsCreateReq req) {
		if (CollectionUtils.isEmpty(req.getDepList())) {
			return new DepartmentsCreateRes(RtnCode.PARAM_ERROR);
		}
		List<String> idList = new ArrayList<>();
		for (Departments item : req.getDepList()) {
			if (!StringUtils.hasText(item.getId()) || !StringUtils.hasText(item.getName())) {
				return new DepartmentsCreateRes(RtnCode.PARAM_ERROR);
			}
			idList.add(item.getId());
		}
		if (dao.existsByIdIn(idList)) {
			return new DepartmentsCreateRes(RtnCode.ID_HAS_EXISTED);
		}
		dao.saveAll(req.getDepList());
		return new DepartmentsCreateRes(RtnCode.SUCCESSFUL);

	}

}
