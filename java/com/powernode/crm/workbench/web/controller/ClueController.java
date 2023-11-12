package com.powernode.crm.workbench.web.controller;

import com.powernode.crm.commons.constant.LoginConstant;
import com.powernode.crm.commons.domain.ReturnObject;
import com.powernode.crm.commons.utils.DateUtils;
import com.powernode.crm.commons.utils.UUIDUtils;
import com.powernode.crm.settings.pojo.DictionaryValue;
import com.powernode.crm.settings.pojo.User;
import com.powernode.crm.settings.service.DictionaryValueService;
import com.powernode.crm.settings.service.UserService;
import com.powernode.crm.workbench.pojo.Activity;
import com.powernode.crm.workbench.pojo.Clue;
import com.powernode.crm.workbench.pojo.ClueActivityRelation;
import com.powernode.crm.workbench.service.ActivityService;
import com.powernode.crm.workbench.service.ClueActivityRelationService;
import com.powernode.crm.workbench.service.ClueRemarkService;
import com.powernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private DictionaryValueService dictionaryValueService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClueService clueService;
    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        List<User> users = userService.queryAllUsers();
        List<DictionaryValue> appellations = dictionaryValueService.queryDictionaryValueByTypeCode("appellation");
        List<DictionaryValue> clueStates = dictionaryValueService.queryDictionaryValueByTypeCode("clueState");
        List<DictionaryValue> sources = dictionaryValueService.queryDictionaryValueByTypeCode("source");
        request.setAttribute("users",users);
        request.setAttribute("appellations",appellations);
        request.setAttribute("clueStates",clueStates);
        request.setAttribute("sources",sources);
        return "workbench/clue/index";
    }
    @ResponseBody
    @RequestMapping("/workbench/clue/queryClueByConditionForPage.do")
    public Object queryClueByConditionForPage(Integer pageNo,Integer pageSize,String company,String fullname,String mphone,String owner,String phone,String source,String state){
        int beginNo=(pageNo-1)*pageSize;
        Map<String,Object> map=new HashMap<>();
        map.put("company",company);
        map.put("fullname",fullname);
        map.put("mphone",mphone);
        map.put("owner",owner);
        map.put("phone",phone);
        map.put("source",source);
        map.put("state",state);
        map.put("beginNo",beginNo);
        map.put("pageSize",pageSize);
        Map<String, Object> res = clueService.queryClueByConditionForPage(map);
        return res;
    }
    @ResponseBody
    @RequestMapping("/workbench/clue/saveCreateClue.do")
    public Object saveCreateClue(Clue clue, HttpSession session){
        User user = (User)session.getAttribute(LoginConstant.SESSION_USER);
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateBy(user.getId());
        clue.setCreateTime(DateUtils.DateToStr(new Date()));
        Object object = clueService.saveCreateClue(clue);
        return object;
    }
    @RequestMapping("/workbench/clue/lookDetailClue.do")
    public String lookDetailClue(HttpServletRequest request,String clueId){
        activityService.queryActivityForDetailByClueId(clueId,request);
        clueService.queryClueForDetailById(clueId,request);
        clueRemarkService.queryClueRemarkForDetailByClueId(clueId,request);
        Clue clue = (Clue) request.getAttribute("clue");
        return "workbench/clue/detail";
    }
    @ResponseBody
    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    public Object queryActivityForDetailByNameClueId(String activityName,String clueId){
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        Object object = activityService.queryActivityForDetailByNameClueId(map);
        return object;
    }
    @ResponseBody
    @RequestMapping("/workbench/clue/saveBind.do")
    public Object saveBind(String[] activityId,String clueId){
        System.out.println(activityId);
        List<ClueActivityRelation> list=new ArrayList<>();
        ReturnObject returnObject=new ReturnObject();
        for(String id:activityId){
            ClueActivityRelation clueActivityRelation=new ClueActivityRelation();
            clueActivityRelation.setActivityId(id);
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setId(UUIDUtils.getUUID());
            list.add(clueActivityRelation);
        }
        try{
            int count = clueActivityRelationService.saveCreateClueActivityRelationByList(list);
            if(count!=0){
                returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
                List<Activity> activities = activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetDate(activities);
            }else{
                returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试");
        }
        return returnObject;
    }
    @ResponseBody
    @RequestMapping("/workbench/clue/saveUnbind.do")
    public Object saveUnbind(ClueActivityRelation clueActivityRelation){
        return clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(clueActivityRelation);
    }
    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String clueId,HttpServletRequest request){
        clueService.queryClueForDetailById(clueId,request);
        List<DictionaryValue> stageList = dictionaryValueService.queryDictionaryValueByTypeCode("stage");
        request.setAttribute("stageList",stageList);
        return "workbench/clue/convert";
    }
    @RequestMapping("/workbench/clue/queryActivityByNameClueId.do")
    @ResponseBody
    public Object queryActivityByNameClueId(String activityName,String clueId){
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        Object object = activityService.queryActivityByActivityNameClueId(map);
        return object;
    }
    @ResponseBody
    @RequestMapping("/workbench/clue/convertClue.do")
    public Object convertClue(String clueId,HttpSession session,String money,String name,String exceptedDate,String activityId,String stage,String isCreatedTran){
//        封装参数
        System.out.println("###################################"+isCreatedTran);
        Map<String,Object> map=new HashMap<>();
        ReturnObject returnObject=new ReturnObject();
        User user=(User)session.getAttribute(LoginConstant.SESSION_USER);
        map.put("clueId",clueId);
        map.put(LoginConstant.SESSION_USER,user);
        map.put("money",money);
        map.put("name",name);
        map.put("exceptedDate",exceptedDate);
        map.put("activityId",activityId);
        map.put("stage",stage);
        map.put("isCreatedTran",isCreatedTran);
        System.out.println("##############################"+map.get("isCreatedTran"));
        try{
            clueService.saveConvert(map);
            returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试.....");
        }
        return returnObject;
    }
}
