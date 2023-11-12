package com.powernode.crm.workbench.service.impl;

import com.powernode.crm.commons.constant.LoginConstant;
import com.powernode.crm.commons.domain.ReturnObject;
import com.powernode.crm.commons.utils.DateUtils;
import com.powernode.crm.commons.utils.UUIDUtils;
import com.powernode.crm.settings.pojo.User;
import com.powernode.crm.workbench.mapper.*;
import com.powernode.crm.workbench.pojo.*;
import com.powernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ContactsRemarkMapper  contactsRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Override
    @Transactional
    public Object saveCreateClue(Clue clue) {
        ReturnObject returnObject=new ReturnObject();
        try{
            int count = clueMapper.insert(clue);
            if(count!=1){
                returnObject.setMessage("系统忙请稍后重试");
                returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
            }else{
                returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_SUCCESS);
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setMessage("系统忙请稍后重试");
            returnObject.setCode(LoginConstant.RETURN_OBJECT_CODE_FAIL);
        }
        return returnObject;

    }

    @Override
    public Map<String,Object> queryClueByConditionForPage(Map<String, Object> map) {
        Map<String,Object> res=new HashMap<>();
        List<Clue> clues = clueMapper.selectClueByConditionForPage(map);
        res.put("clueList",clues);
        res.put("totalRow",clueMapper.countRowByConditionForPage(map));
        return res;
    }

    @Override
    public void queryClueForDetailById(String id, HttpServletRequest request) {
        Clue clue = clueMapper.selectClueForDetailById(id);
        request.setAttribute("clue",clue);
    }

    @Override
    @Transactional
    public void saveConvert(Map<String, Object> map) {
//        根据id查询线索的信息
        String clueId =(String)map.get("clueId");
        User user=(User) map.get(LoginConstant.SESSION_USER);
        Clue clue = clueMapper.selectClueById(clueId);
//        筛选出线索里面有关客户的信息，并且封装在客户对象中，并且插入到客户表中
        Customer customer=new Customer();
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.DateToStr(new Date()));
        customer.setDescription(clue.getDescription());
        customer.setId(UUIDUtils.getUUID());
        customer.setName(clue.getCompany());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setOwner(user.getId());
        customer.setPhone(clue.getPhone());
        customer.setWebsite(clue.getWebsite());
        customerMapper.insertCustomer(customer);
//        筛选出线索里面有关客户的信息，并且封装在客户对象中，并且插入到客户表中
        Contacts contacts=new Contacts();
        contacts.setAppellation(clue.getAppellation());
        contacts.setAppellation(clue.getAddress());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.DateToStr(new Date()));
        contacts.setCustomerId(customer.getId());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setFullname(clue.getFullname());
        contacts.setId(UUIDUtils.getUUID());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(user.getId());
        contactsMapper.insert(contacts);
//        根据clueId查询该线索下所有的备注
        List<ClueRemark> clueRemarks = clueRemarkMapper.selectClueRemarkByClueId(clueId);
        if(clueRemarks!=null&&clueRemarks.size()>0){
            CustomerRemark customerRemark=null;
            ContactsRemark contactsRemark=null;
            List<CustomerRemark> customerRemarks=new ArrayList<>();
            List<ContactsRemark> contactsRemarks=new ArrayList<>();
            for(ClueRemark cr:clueRemarks){
                customerRemark=new CustomerRemark();
                customerRemark.setCreateBy(cr.getCreateBy());
                customerRemark.setCreateTime(cr.getCreateTime());
                customerRemark.setCustomerId(customer.getId());
                customerRemark.setEditBy(cr.getEditBy());
                customerRemark.setEditFlag(cr.getEditFlag());
                customerRemark.setEditTime(cr.getEditTime());
                customerRemark.setNoteContent(cr.getNoteContent());
                customerRemark.setId(UUIDUtils.getUUID());
                customerRemarks.add(customerRemark);
//                封装联系人备注
                contactsRemark=new ContactsRemark();
                contactsRemark.setCreateBy(cr.getCreateBy());
                contactsRemark.setCreateTime(cr.getCreateTime());
                contactsRemark.setContactsId(contacts.getId());
                contactsRemark.setEditBy(cr.getEditBy());
                contactsRemark.setEditFlag(cr.getEditFlag());
                contactsRemark.setEditTime(cr.getEditTime());
                contactsRemark.setNoteContent(cr.getNoteContent());
                contactsRemark.setId(UUIDUtils.getUUID());
            }
             contactsRemarkMapper.insertContactRemarkByList(contactsRemarks);
             customerRemarkMapper.insertCustomerRemarkByList(customerRemarks);
        }
//        根据clueId将查询与该线索关联的市场活动，然后将其转到联系人与市场活动的关联关系
        List<ClueActivityRelation> clueActivityRelations = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);
        if(clueActivityRelations!=null&&clueActivityRelations.size()>0){
            ContactsActivityRelation contactsActivityRelation=null;
            List<ContactsActivityRelation> contactsActivityRelations=new ArrayList<>();
            for(ClueActivityRelation car:clueActivityRelations){
                contactsActivityRelation=new ContactsActivityRelation();
                contactsActivityRelation.setActivityId(car.getActivityId());
                contactsActivityRelation.setContactsId(contacts.getId());
                contactsActivityRelation.setId(UUIDUtils.getUUID());
                contactsActivityRelations.add(contactsActivityRelation);
            }
            contactsActivityRelationMapper.insertContactsActivityRelationByList(contactsActivityRelations);
        }
//        如果需要创建交易，往交易表中添加字段，顺便将线索备注加到交易表的线索备注
        if(((String)map.get("isCreatedTran")).equals("true")){
            Transaction transaction = new Transaction();
            transaction.setActivityId((String) map.get("activityId"));
            transaction.setContactsId(contacts.getId());
            transaction.setId(UUIDUtils.getUUID());
            transaction.setCreateBy(user.getId());
            transaction.setCreateBy(DateUtils.DateToStr(new Date()));
            transaction.setCustomerId(customer.getId());
            transaction.setExpectedDate((String)map.get("exceptedDate"));
            transaction.setMoney((String)map.get("money"));
            transaction.setName(((String)map.get("name")));
            transaction.setStage((String)map.get("stage"));
            transaction.setOwner(user.getId());
            transactionMapper.insertTran(transaction);
            if(clueRemarks!=null&&clueRemarks.size()>0){
                TransactionRemark transactionRemark=null;
                List<TransactionRemark> transactionRemarks=new ArrayList<>();
                for(ClueRemark cr:clueRemarks){
                    transactionRemark.setCreateBy(cr.getCreateBy());
                    transactionRemark.setCreateTime(cr.getCreateTime());
                    transactionRemark.setEditBy(cr.getEditBy());
                    transactionRemark.setEditFlag(cr.getEditFlag());
                    transactionRemark.setEditTime(cr.getEditTime());
                    transactionRemark.setId(UUIDUtils.getUUID());
                    transactionRemark.setTranId(transaction.getId());
                    transactionRemark.setNoteContent(cr.getNoteContent());
                    transactionRemarks.add(transactionRemark);
                }
                transactionRemarkMapper.insertTranRemarkByList(transactionRemarks);
            }
            clueRemarkMapper.deleteClueRemarkByClueId(clueId);
            clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
            clueMapper.deleteClueByClueId(clueId);
        }

    }
}
