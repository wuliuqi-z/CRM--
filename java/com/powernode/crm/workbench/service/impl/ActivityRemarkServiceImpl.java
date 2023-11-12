package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.commons.constant.LoginConstant;
import com.powernode.crm.commons.domain.ReturnObject;
import com.powernode.crm.workbench.mapper.ClueRemarkMapper;
import com.powernode.crm.workbench.pojo.ClueRemark;
import com.powernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service("activityRemarkService")
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Override
    public List<ClueRemark> queryActivityRemarkForDetailByActivityId(String activityId) {
        return clueRemarkMapper.selectActivityRemarkForDetailById(activityId);
    }

    @Override
    @Transactional
    public Object saveCreatedActivityRemark(ClueRemark remark) {
        ReturnObject returnObject=new ReturnObject();
        try{
            int count=clueRemarkMapper.insertActivityRemark(remark);
            if(count==1){
                returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetDate(remark);
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

    @Override
    @Transactional
    public Object saveEditActivityRemark(ClueRemark clueRemark) {
        ReturnObject returnObject=new ReturnObject();
        try{
            int count = clueRemarkMapper.updateActivityRemark(clueRemark);
            if(count==1){
                returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setRetDate(clueRemark);
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

    @Override
    public Object deleteActivityRemarkById(String id) {
        int count=0;
        ReturnObject returnObject=new ReturnObject();
        try{
             count= clueRemarkMapper.deleteActivityMarkById(id);
             if(count==1){
                 returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
             }else{
                 returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
                 returnObject.setMessage("系统忙，你稍后重试");
             }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，你稍后重试");
        }
        return returnObject;


    }
}
