package com.zhaoyun.docmanager.dal.dao;

import com.zhaoyun.docmanager.model.dvo.DocApi;

public interface MBGDocApiDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_api
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_api
     *
     * @mbggenerated
     */
    int insert(DocApi record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_api
     *
     * @mbggenerated
     */
    int insertSelective(DocApi record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_api
     *
     * @mbggenerated
     */
    DocApi selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_api
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DocApi record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_api
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(DocApi record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_api
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DocApi record);
}