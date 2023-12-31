package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.workbench.mapper.CustomerMapper;
import com.powernode.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<String> queryCustomerNameByName(String search) {
        return customerMapper.selectCustomerNameByName(search);
    }
}
