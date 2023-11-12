package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.workbench.mapper.TransactionRemarkMapper;
import com.powernode.crm.workbench.pojo.TransactionRemark;
import com.powernode.crm.workbench.service.TransactionRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionRemarkServiceImpl implements TransactionRemarkService {
    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;
    @Override
    public List<TransactionRemark> queryTransactionRemarkForDetailByTransactionId(String transactionId) {
        return transactionRemarkMapper.selectTransactionRemarkForDetailByTransactionId(transactionId);
    }
}
