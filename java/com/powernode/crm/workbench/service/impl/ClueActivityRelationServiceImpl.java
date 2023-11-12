package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.commons.constant.LoginConstant;
import com.powernode.crm.commons.domain.ReturnObject;
import com.powernode.crm.workbench.mapper.ClueActivityRelationMapper;
import com.powernode.crm.workbench.pojo.ClueActivityRelation;
import com.powernode.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Transactional
    @Override
    public int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list) {
        return clueActivityRelationMapper.insertClueActivityRelationByList(list);
    }

    @Override
    @Transactional
    public Object deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation clueActivityRelation) {
        ReturnObject returnObject=new ReturnObject();
        try{
            int count = clueActivityRelationMapper.deleteClueActivityRelationByClueActivityId(clueActivityRelation);
            if(count!=0){
                returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后再试");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后再试");
        }
        return returnObject;
    }
}
