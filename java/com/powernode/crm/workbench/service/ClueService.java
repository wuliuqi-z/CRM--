package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.pojo.Clue;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface ClueService {
    /**
     * 根据条件查询所有的满足条件的线索
     *
     */
    Map<String,Object> queryClueByConditionForPage(Map<String,Object> map);
    Object saveCreateClue(Clue clue);
    void queryClueForDetailById(String id, HttpServletRequest request);
    void saveConvert(Map<String,Object> map);
}
