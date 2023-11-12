package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.pojo.Activity;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ActivityService {
    Object saveCreateActivity(Activity activity);
    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);
    int queryCountOfActivityByCondition(Map<String,Object> map);
    int deleteActivityByIds(String[] ids);
    Activity queryActivityById(String id);
    Object updateActivityById(Activity activity);
    HSSFWorkbook createActivityByExcel() throws IOException;
    Object saveCreateActivityByList(MultipartFile myFile, String absolutePath, HttpSession session);
    Activity queryDetailActivityById(String activityId);
    void queryActivityForDetailByClueId(String clueId, HttpServletRequest request);
    Object queryActivityForDetailByNameClueId(Map<String,Object> map);
    List<Activity> queryActivityForDetailByIds(String[] ids);
    Object queryActivityByActivityNameClueId(Map<String,Object> map);
}
