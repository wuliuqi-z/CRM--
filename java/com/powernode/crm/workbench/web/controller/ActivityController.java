package com.powernode.crm.workbench.web.controller;

import com.powernode.crm.commons.constant.LoginConstant;
import com.powernode.crm.commons.domain.ReturnObject;
import com.powernode.crm.commons.utils.DateUtils;
import com.powernode.crm.commons.utils.UUIDUtils;
import com.powernode.crm.settings.pojo.User;
import com.powernode.crm.settings.service.UserService;
import com.powernode.crm.workbench.pojo.Activity;
import com.powernode.crm.workbench.pojo.ClueRemark;
import com.powernode.crm.workbench.service.ActivityRemarkService;
import com.powernode.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private ActivityRemarkService activityRemarkService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        List<User> users = userService.queryAllUsers();
        request.setAttribute("users",users);
        return "/workbench/activity/index";
    }
    @ResponseBody
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    public Object saveCreateActivity(Activity activity, HttpSession session){
        User user=(User)session.getAttribute(LoginConstant.SESSION_USER);
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.DateToStr(new Date()));
        activity.setCreateBy(user.getId());//名字有可能重名，所以用的是id
        Object object = activityService.saveCreateActivity(activity);
        return object;

    }
    @ResponseBody
    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,String endDate,Integer pageNo,Integer pageSize){
        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        int count = activityService.queryCountOfActivityByCondition(map);
        List<Activity> activities = activityService.queryActivityByConditionForPage(map);
        Map<String,Object> res=new HashMap<>();
        res.put("totalRow",count);
        res.put("activities",activities);
        return res;
    }
    @ResponseBody
    @RequestMapping("/workbench/activity/deleteActivityIds.do")
    public Object deleteActivityIds(String[] id){
//        id.replaceAll("id=","");
//        String[] ids=id.split("&");
//        System.out.println(ids);
        int count=0;
        try{
             count= activityService.deleteActivityByIds(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        ReturnObject object=new ReturnObject();
        if(count>0){
            object.setCode(LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
        }else{
            object.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
            object.setMessage("系统忙，请稍后重试");
        }
        return object;
    }
    @ResponseBody
    @RequestMapping("/workbench/activity/queryActivityById.do")
    public Object queryActivityById(String id){
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }
    @ResponseBody
    @RequestMapping("/workbench/activity/updateActivityById.do")
    public Object updateActivityById(HttpSession session,String owner,String name,String startTime,String endTime,String cost,String describe,String id){
        String editTime = DateUtils.DateToStr(new Date());
        User user = (User)session.getAttribute(LoginConstant.SESSION_USER);
        String editorId = user.getId();
        Activity activity=new Activity();
        activity.setCost(cost);
        activity.setDescription(describe);
        activity.setEditBy(editorId);
        activity.setName(name);
        activity.setOwner(owner);
        activity.setEditTime(editTime);
        activity.setEndDate(endTime);
        activity.setStartDate(startTime);
        activity.setId(id);
        Object object = activityService.updateActivityById(activity);
        return object;
    }
    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws IOException {
//      设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");//应用程序返回的二进制文件
//        获取输入流
        OutputStream out = response.getOutputStream();//tomcat开的让他自己关
        //        浏览器接收到响应信息之后，默认情况下，直接在现实窗口中打开响应信息，即使打不开也会调用应用程序打开，实在打不开，才会激活文件下载窗口，让你去下载
//        我们可以设置响应头信息，使浏览器接收到响应信息之后直接激活文件下载窗口，即使能打开也不打开
        response.addHeader("Content-Disposition","attachment;filename=myStudentList.xls");//告诉浏览器以attachment的形式打开响应信息，也就是以附件的形式打开,文件的名字叫myStudentList.xls
//        PrintWriter out = outputStream;//字符流，只能写以字符为单位的普通文本文件，不能写以二进制为单位的
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("student.xls");
        byte[] buff=new byte[256];//缓冲区
        int len=0;
        while((len=is.read(buff))!=-1){//一个字节一个字节读)
               out.write(buff,0,len);
        }
        is.close();
        out.flush();
    }
    @RequestMapping("/workbench/activity/exportAllActivitiesByExcel.do")
    public void exportAllActivitiesByExcel(HttpServletResponse response,HttpServletRequest request) throws IOException {
        HSSFWorkbook excel = activityService.createActivityByExcel();
        response.setContentType("application/octet-stream;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename=myActivities.xls");
//        InputStream is = this.getClass().getClassLoader().getResourceAsStream("activities.xls");
//        byte[] buff=new byte[256];
//        int len=-1;
//        while((len=is.read(buff))!=-1){
//            out.write(buff,0,len);
//        }
//        is.close();
//        out.flush();

        excel.write(out);
        excel.close();
        out.flush();
    }
    @ResponseBody
    @RequestMapping("/workbench/activity/fileUpLoad.do")
    public Object fileUpLoad(HttpServletRequest request,String userName,MultipartFile myFile) throws IOException {//会自动把请求体里的文件封装到这个参数里面，springMVC提供的，调用了springmvc内部一个类，但是那个类一开始没有放到springmvc的容器里，所以你必须创建那个对象，将其纳入springmvc的容器中
        System.out.println("userName:"+userName);
//        把文件在服务器指定的目录中生成同样的文件
        String realPath=request.getServletContext().getRealPath("/");//C:\Users\12801\Desktop\CRM\crm-project\crm\target\crm\你不加斜杠就是C:\Users\12801\Desktop\CRM\crm-project\crm\target\crm
        System.out.println(realPath);
        File file = new File(realPath,myFile.getOriginalFilename());//文件不存在可以给你自动创建
        myFile.transferTo(file);//转换到哪个文件里面
        ReturnObject returnObject=new ReturnObject();
        returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
        returnObject.setMessage("上传成功");
        return returnObject;
    }
    @ResponseBody
    @RequestMapping("/workbench/activity/importActivity.do")
    public Object importActivity(MultipartFile activityFile,HttpServletRequest request,HttpSession session){
        String realPath = request.getServletContext().getRealPath("/");
        Object object = activityService.saveCreateActivityByList(activityFile, realPath,session);
        return object;
    }
    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String activityId,HttpServletRequest request){
        Activity activity = activityService.queryDetailActivityById(activityId);
        List<ClueRemark> clueRemarks = activityRemarkService.queryActivityRemarkForDetailByActivityId(activityId);
        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",clueRemarks);
        return "workbench/activity/detail";
    }
    @ResponseBody
    @RequestMapping("/workbench/activity/saveCreatedActivityComment.do")
    public Object saveCreatedActivityComment(ClueRemark remark, HttpSession session){
        User user=(User)session.getAttribute(LoginConstant.SESSION_USER);
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DateUtils.DateToStr(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(LoginConstant.REMARK_EDIT_FLAG_NO_EDIT);
        Object object = activityRemarkService.saveCreatedActivityRemark(remark);
        return object;
    }
    @ResponseBody
    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    public Object deleteActivityRemarkById(String id){
        Object object = activityRemarkService.deleteActivityRemarkById(id);
        return object;
    }
    @ResponseBody
    @RequestMapping("/workbench/activity/saveEditActivityRemark.do")
    public Object saveEditActivityRemark(ClueRemark clueRemark,HttpSession session){
        User user = (User)session.getAttribute(LoginConstant.SESSION_USER);
        clueRemark.setEditFlag(LoginConstant.REMARK_EDIT_FLAG_YES_EDIT);
        clueRemark.setEditTime(DateUtils.DateToStr(new Date()));
        clueRemark.setEditBy(user.getId());
        Object object = activityRemarkService.saveEditActivityRemark(clueRemark);
        return object;
    }


}
