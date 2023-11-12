package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.workbench.mapper.TransactionHistoryMapper;
import com.powernode.crm.workbench.pojo.TransactionHistory;
import com.powernode.crm.workbench.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TransactionHistoryServiceImpl implements TransactionHistoryService {
    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;
    @Override
    public List<TransactionHistory> queryTransactionHistoryForDetailByTransactionId(String transactionId) {
        return transactionHistoryMapper.selectTransactionHistoryForDetailByTransactionId(transactionId);
    }
}
