package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.pojo.Transaction;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.Map;

public interface TransactionService {
    void saveCreateTran(Map<String,Object> map);
    Transaction queryTransactionForDetailById(String id);
}
