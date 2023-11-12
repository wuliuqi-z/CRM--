package com.powernode.crm.workbench.mapper;

import com.powernode.crm.workbench.pojo.ContactsActivityRelation;

import java.util.List;

public interface ContactsActivityRelationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_activity_relation
     *
     * @mbggenerated Sat Nov 04 09:14:06 CDT 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_activity_relation
     *
     * @mbggenerated Sat Nov 04 09:14:06 CDT 2023
     */
    int insert(ContactsActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_activity_relation
     *
     * @mbggenerated Sat Nov 04 09:14:06 CDT 2023
     */
    int insertSelective(ContactsActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_activity_relation
     *
     * @mbggenerated Sat Nov 04 09:14:06 CDT 2023
     */
    ContactsActivityRelation selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_activity_relation
     *
     * @mbggenerated Sat Nov 04 09:14:06 CDT 2023
     */
    int updateByPrimaryKeySelective(ContactsActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_activity_relation
     *
     * @mbggenerated Sat Nov 04 09:14:06 CDT 2023
     */
    int updateByPrimaryKey(ContactsActivityRelation record);

    /**
     * 批量保存创建的联系人和市场活动的关联关系
     * @param list
     * @return
     */
    int insertContactsActivityRelationByList(List<ContactsActivityRelation> list);
}