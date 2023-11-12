package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.commons.constant.LoginConstant;
import com.powernode.crm.commons.domain.ReturnObject;
import com.powernode.crm.commons.utils.DateUtils;
import com.powernode.crm.commons.utils.HSSFUtils;
import com.powernode.crm.commons.utils.UUIDUtils;
import com.powernode.crm.settings.pojo.User;
import com.powernode.crm.workbench.mapper.ActivityMapper;
import com.powernode.crm.workbench.pojo.Activity;
import com.powernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Override
    @Transactional
    public Object saveCreateActivity(Activity activity) {
        int count = activityMapper.insertActivity(activity);
        ReturnObject returnObject = new ReturnObject();
        if(count==1){
            returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
            return returnObject;
        }
        returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
        returnObject.setMessage("系统忙，请稍后重试.....");
        return returnObject;
    }

    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    @Transactional
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public int queryCountOfActivityByCondition(Map<String, Object> map) {
        return activityMapper.selectCountActivityByCondition(map);
    }

    @Override
    public Activity queryActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    @Transactional
    public Object updateActivityById(Activity activity) {
        ReturnObject returnObject=new ReturnObject();
        int count=0;
        try{
            count=activityMapper.updateActivityById(activity);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(count==1){
            returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请联系管理员");
        }

        return returnObject;
    }

    @Override

    public HSSFWorkbook createActivityByExcel() throws IOException {
        List<Activity> activities = activityMapper.selectAll();
//        创建excel文件，并且吧activities写到excel文件中
        HSSFWorkbook excel=new HSSFWorkbook();
//        首先在这个excel文件中创建页
        HSSFSheet sheet = excel.createSheet("市场活动列表");
//        在页里面创建行
        HSSFRow row = sheet.createRow(0);
//        在行里面创建列
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建时间");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("修改时间");
        cell = row.createCell(10);
        cell.setCellValue("修改者");
//        以上是表头
        int rowIndex=1;
        int columnIndex=0;
        if(activities!=null&&activities.size()>0){
            for(Activity activity:activities){
                row=sheet.createRow(rowIndex++);
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
//            根据excel对象生成excel文件
//                OutputStream os=null;
//                File file=new File("src/resources/excel");
//                String absolutePath1=file.getAbsolutePath();
//                String absolutePath="C:\\Users\\12801\\Desktop\\CRM\\crm-project\\crm\\src\\main\\resources";
//                os=new FileOutputStream(absolutePath+"/"+"activities.xls",false);
/*            excel.write(new FileOutputStream("src/resources/1.xls"));*/
        }

        return excel;
    }

    @Override
    @Transactional
    public Object saveCreateActivityByList(MultipartFile myFile, String absolutePath, HttpSession session){
        List<Activity> activityList=new ArrayList<>();
        User user = (User) session.getAttribute(LoginConstant.SESSION_USER);
        FileInputStream is =null;
        try {
//            File file=new File(absolutePath,myFile.getOriginalFilename());
//            myFile.transferTo(file);
//            is = new FileInputStream(absolutePath + myFile.getOriginalFilename());
            HSSFWorkbook sheets = new HSSFWorkbook(myFile.getInputStream());//连接myFile的流，将本就在内存里面的myFile放到输入流里面、、然后我们就不要再磁盘和内存之间创建通道了
            HSSFSheet sheet = sheets.getSheetAt(0);
            HSSFRow row=null;
            HSSFCell cell=null;

            String cellValue=null;
            for(int i=1;i<=sheet.getLastRowNum();i++){
                row=sheet.getRow(i);
                Activity activity=new Activity();
                activity.setOwner(user.getId());
                activity.setCreateBy(user.getId());
                String uuid = UUIDUtils.getUUID();
                activity.setId(uuid);
                activity.setCreateTime(DateUtils.DateToStr(new Date()));
                for(int j=0;j<row.getLastCellNum();j++){
                    cell= row.getCell(j);
                    cellValue= HSSFUtils.getCellValueForStr(cell);
                    if(j==0){
                        activity.setName(cellValue);
                    }else if(j==1){
                        activity.setStartDate(cellValue);
                    }else if(j==2){
                        activity.setEndDate(cellValue);
                    }else if(j==3){
                        activity.setCost(cellValue);
                    }else if(j==4){
                        activity.setDescription(cellValue);
                    }
                }
                activityList.add(activity);
            }
            for(Activity activity1:activityList){
                System.out.println(activity1.getName()+":"+activity1.getCreateTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        int count = activityMapper.insertActivityByList(activityList);
        ReturnObject returnObject=new ReturnObject();
        if(count<=0){
            returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试");
        }else{
            returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetDate(count);
        }
        return returnObject;

    }

    @Override
    public Activity queryDetailActivityById(String activityId) {
        return activityMapper.selectActivityDetailById(activityId);
    }

    @Override
    public void queryActivityForDetailByClueId(String clueId,HttpServletRequest request) {
        List<Activity> activities = activityMapper.selectActivityForDetailByClueId(clueId);
        request.setAttribute("activities",activities);
    }

    @Override
    public Object queryActivityForDetailByNameClueId(Map<String, Object> map) {
        List<Activity> activities = activityMapper.selectActivityForDetailByNameClueId(map);
        ReturnObject returnObject=new ReturnObject();
        returnObject.setRetDate(activities);
        return returnObject;
    }

    @Override
    public List<Activity> queryActivityForDetailByIds(String[] ids) {
        return activityMapper.selectActivityForDetailByIds(ids);
    }

    @Override
    public Object queryActivityByActivityNameClueId(Map<String, Object> map) {
        List<Activity> activities = activityMapper.selectActivityByActivityNameClueId(map);
        ReturnObject returnObject=new ReturnObject();
        returnObject.setRetDate(activities);
        return returnObject;
    }
}
