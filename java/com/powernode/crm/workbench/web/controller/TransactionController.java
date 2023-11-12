package com.powernode.crm.workbench.web.controller;

import com.powernode.crm.commons.constant.LoginConstant;
import com.powernode.crm.commons.domain.ReturnObject;
import com.powernode.crm.settings.mapper.UserMapper;
import com.powernode.crm.settings.pojo.DictionaryValue;
import com.powernode.crm.settings.pojo.User;
import com.powernode.crm.settings.service.DictionaryValueService;
import com.powernode.crm.settings.service.UserService;
import com.powernode.crm.settings.service.impl.UserServiceImpl;
import com.powernode.crm.workbench.pojo.Transaction;
import com.powernode.crm.workbench.pojo.TransactionHistory;
import com.powernode.crm.workbench.pojo.TransactionRemark;
import com.powernode.crm.workbench.service.CustomerService;
import com.powernode.crm.workbench.service.TransactionHistoryService;
import com.powernode.crm.workbench.service.TransactionRemarkService;
import com.powernode.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TransactionController {
    @Autowired
    private TransactionHistoryService transactionHistoryService;
    @Autowired
    private TransactionRemarkService transactionRemarkService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private DictionaryValueService dictionaryValueService;
    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request){
        List<DictionaryValue> transactionType = dictionaryValueService.queryDictionaryValueByTypeCode("transactionType");
        List<DictionaryValue> source = dictionaryValueService.queryDictionaryValueByTypeCode("source");
        List<DictionaryValue> stage = dictionaryValueService.queryDictionaryValueByTypeCode("stage");
        request.setAttribute("stage",stage);
        request.setAttribute("transactionType",transactionType);
        request.setAttribute("source",source);
        return "workbench/transaction/index";
    }
    @RequestMapping("/workbench/transaction/save.do")
    public String save(HttpServletRequest request){
        List<User> users = userService.queryAllUsers();
        List<DictionaryValue> transactionType = dictionaryValueService.queryDictionaryValueByTypeCode("transactionType");
        List<DictionaryValue> source = dictionaryValueService.queryDictionaryValueByTypeCode("source");
        List<DictionaryValue> stage = dictionaryValueService.queryDictionaryValueByTypeCode("stage");
        request.setAttribute("users",users);
        request.setAttribute("source",source);
        request.setAttribute("stage",stage);
        request.setAttribute("transactionType",transactionType);
        return "workbench/transaction/save";
    }

    @ResponseBody
    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    public Object getPossibilityByStage(String stage){
        ResourceBundle bundle = ResourceBundle.getBundle("user/possibility");
        String possibility = bundle.getString(stage);
        return possibility;
    }
    @ResponseBody
    @RequestMapping("/workbench/transaction/queryCustomerNameByName.do")
    public Object queryCustomerNameByName(String search){
        List<String> customerNames = customerService.queryCustomerNameByName(search);
//        根据查询结果返回响应信息
        System.out.println(customerNames);
        return customerNames;//list转成json字符串就是数组
    }
    @ResponseBody
    @RequestMapping("/workbench/transaction/savaCreateTran.do")
    public Object savaCreateTran(@RequestParam Map<String,Object> map, HttpSession session){//加上注解，让他自动封装成map
        map.put(LoginConstant.SESSION_USER,session.getAttribute(LoginConstant.SESSION_USER));
        ReturnObject returnObject=new ReturnObject();
        try{
            transactionService.saveCreateTran(map);
            returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试");
        }
        return returnObject;
    }
    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String tranId,HttpServletRequest request){
        System.out.println(tranId);
        Transaction transaction = transactionService.queryTransactionForDetailById(tranId);
        List<TransactionRemark> transactionRemarks = transactionRemarkService.queryTransactionRemarkForDetailByTransactionId(tranId);
        List<TransactionHistory> transactionHistories = transactionHistoryService.queryTransactionHistoryForDetailByTransactionId(tranId);
        ResourceBundle resourceBundle=ResourceBundle.getBundle("user/possibility");
        String possible = resourceBundle.getString(transaction.getStage());
        List<DictionaryValue> stage = dictionaryValueService.queryDictionaryValueByTypeCode("stage");
        request.setAttribute("stage",stage);
        request.setAttribute("possible",possible);
        request.setAttribute("transaction",transaction);
        request.setAttribute("transactionRemarks",transactionRemarks);
        request.setAttribute("transactionHistories",transactionHistories);
        return "workbench/transaction/detail";
    }
}
