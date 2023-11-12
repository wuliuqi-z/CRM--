package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.pojo.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int saveCreateClueActivityRelationByList(List<ClueActivityRelation> list);
    Object deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation clueActivityRelation);
}
