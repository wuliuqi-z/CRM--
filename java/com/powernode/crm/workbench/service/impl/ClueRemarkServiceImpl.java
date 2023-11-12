package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.workbench.mapper.ClueRemarkMapper;
import com.powernode.crm.workbench.pojo.ClueRemark;
import com.powernode.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Autowired
    ClueRemarkMapper clueRemarkMapper;
    @Override
    public void queryClueRemarkForDetailByClueId(String clueId, HttpServletRequest request) {
        List<ClueRemark> clueRemarks = clueRemarkMapper.selectClueRemarkForDetailByClueId(clueId);
        request.setAttribute("clueRemarks",clueRemarks);
    }
}
