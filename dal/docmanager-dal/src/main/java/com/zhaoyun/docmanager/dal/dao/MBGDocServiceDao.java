package com.zhaoyun.docmanager.dal.dao;

import com.zhaoyun.docmanager.model.dvo.DocService;

public interface MBGDocServiceDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_service
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_service
     *
     * @mbggenerated
     */
    int insert(DocService record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_service
     *
     * @mbggenerated
     */
    int insertSelective(DocService record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_service
     *
     * @mbggenerated
     */
    DocService selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_service
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(DocService record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table doc_service
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(DocService record);
}