package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.pojo.ClueRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<ClueRemark> queryActivityRemarkForDetailByActivityId(String activityId);
    Object saveCreatedActivityRemark(ClueRemark remark);
    Object deleteActivityRemarkById(String id);
    Object saveEditActivityRemark(ClueRemark clueRemark);
}
