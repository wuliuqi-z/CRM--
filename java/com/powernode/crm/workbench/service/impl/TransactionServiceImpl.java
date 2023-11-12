package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.commons.constant.LoginConstant;
import com.powernode.crm.commons.utils.DateUtils;
import com.powernode.crm.commons.utils.UUIDUtils;
import com.powernode.crm.settings.pojo.User;
import com.powernode.crm.workbench.mapper.CustomerMapper;
import com.powernode.crm.workbench.mapper.TransactionHistoryMapper;
import com.powernode.crm.workbench.mapper.TransactionMapper;
import com.powernode.crm.workbench.pojo.Customer;
import com.powernode.crm.workbench.pojo.Transaction;
import com.powernode.crm.workbench.pojo.TransactionHistory;
import com.powernode.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    @Transactional
    public void saveCreateTran(Map<String, Object> map) {
//        根据name精确查询客户
        String customerName=(String) map.get("customerName");
        User user=(User)map.get(LoginConstant.SESSION_USER);
        Customer customer = customerMapper.selectCustomerByName(customerName);
        if(customer==null){
            customer=new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setName(customerName);
            customer.setOwner(user.getId());
            customer.setCreateTime(DateUtils.DateToStr(new Date()));
            customer.setCreateBy(user.getId());
            customerMapper.insertCustomer(customer);
        }
            String id=customer.getId();
            Transaction transaction=new Transaction();
            transaction.setStage((String) map.get("stage"));
            transaction.setOwner((String)map.get("owner"));
            transaction.setNextContactTime((String)map.get("nextContactTime"));
            transaction.setName((String)map.get("name"));
            transaction.setMoney((String)map.get("money"));
            transaction.setId(UUIDUtils.getUUID());
            String expectedDate=(String)map.get("expectedDate") ;
            transaction.setExpectedDate(expectedDate);
            System.out.println((String)map.get("expectedDate"));
            transaction.setCustomerId(id);
            transaction.setCreateTime(DateUtils.DateToStr(new Date()));
            transaction.setCreateBy(user.getId());
            transaction.setContactSummary((String)map.get("contactSummary"));
            transaction.setContactsId((String)map.get("contactsId"));
            transaction.setActivityId((String)map.get("activityId"));
            transaction.setDescription((String)map.get("description"));
            transaction.setSource((String) map.get("source"));
            transaction.setType((String)map.get("type"));
        System.out.println(transaction.getExpectedDate());
            transactionMapper.insertTran(transaction);
//            保存交易历史
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.setCreateBy(user.getId());
            transactionHistory.setCreateTime(DateUtils.DateToStr(new Date()));
            transactionHistory.setExpectedDate(transaction.getExpectedDate());
            transactionHistory.setId(UUIDUtils.getUUID());
            transactionHistory.setMoney(transaction.getMoney());
            transactionHistory.setStage(transaction.getStage());
            transactionHistory.setTranId(transaction.getId());
            transactionHistoryMapper.insertTranHistory(transactionHistory);

    }

    @Override
    public Transaction queryTransactionForDetailById(String id) {
        return transactionMapper.selectTransactionForDetailById(id);
    }
}
