package com.powernode.crm.settings.service;

import com.powernode.crm.settings.pojo.DictionaryValue;

import java.util.List;

public interface DictionaryValueService {
    List<DictionaryValue> queryDictionaryValueByTypeCode(String typeCode);
}
