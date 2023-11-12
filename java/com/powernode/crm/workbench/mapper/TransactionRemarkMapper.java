package com.powernode.crm.workbench.mapper;

import com.powernode.crm.workbench.pojo.TransactionRemark;

import java.util.List;

public interface TransactionRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Sat Nov 04 10:00:19 CDT 2023
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Sat Nov 04 10:00:19 CDT 2023
     */
    int insert(TransactionRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Sat Nov 04 10:00:19 CDT 2023
     */
    int insertSelective(TransactionRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Sat Nov 04 10:00:19 CDT 2023
     */
    TransactionRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Sat Nov 04 10:00:19 CDT 2023
     */
    int updateByPrimaryKeySelective(TransactionRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_tran_remark
     *
     * @mbggenerated Sat Nov 04 10:00:19 CDT 2023
     */
    int updateByPrimaryKey(TransactionRemark record);

    /**
     * 批量保存创建的交易备注
     * @param list
     * @return
     */
    int insertTranRemarkByList(List<TransactionRemark> list);
    List<TransactionRemark> selectTransactionRemarkForDetailByTransactionId(String transactionId);
}