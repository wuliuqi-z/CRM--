package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.pojo.TransactionHistory;

import java.util.List;

public interface TransactionHistoryService {
    List<TransactionHistory> queryTransactionHistoryForDetailByTransactionId(String transactionId);
}
