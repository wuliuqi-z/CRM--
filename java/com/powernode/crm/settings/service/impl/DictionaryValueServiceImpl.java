package com.powernode.crm.settings.service.impl;

import com.powernode.crm.settings.mapper.DictionaryValueMapper;
import com.powernode.crm.settings.pojo.DictionaryValue;
import com.powernode.crm.settings.service.DictionaryValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DictionaryValueServiceImpl implements DictionaryValueService {
    @Autowired
    private DictionaryValueMapper dictionaryValueMapper;
    @Override
    public List<DictionaryValue> queryDictionaryValueByTypeCode(String typeCode) {
        List<DictionaryValue> dictionaryValues = dictionaryValueMapper.selectDictionaryValueByTypeCode(typeCode);
        return dictionaryValues;
    }
}
