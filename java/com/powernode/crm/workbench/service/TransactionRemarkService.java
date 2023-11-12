package com.powernode.crm.workbench.service;

import com.powernode.crm.workbench.pojo.TransactionRemark;

import java.util.List;

public interface TransactionRemarkService {
    List<TransactionRemark> queryTransactionRemarkForDetailByTransactionId(String transactionId);
}
