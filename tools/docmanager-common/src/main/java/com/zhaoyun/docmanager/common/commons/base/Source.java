/**
 * com Inc
 * Copyright (c) 2014-2016 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.base;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
/**
 * 系统源枚举
 * <p>用于列举团队中的各个系统的基础信息</p>
 * 
 * @author user
 * @since $Revision:1.0.0, $Date: 2016年1月18日 上午10:59:49 $
 */
public enum Source {
    // Superion - 基础平台 200
    MARKETCENTER(200, "营销中心"),
    /** 不规范的code */ 
    CRM(201, "销售管理系统"),
    GOODSCENTER(202, "商品中心"),
    PAYCENTER(204, "支付中心"),
    MEMBERCENTER(205, "用户中心"),
    MERCHANTCENTER(206, "商户中心"),
    ORDERCENTER(207, "订单中心"),
    GROWTH(208, "成长体系"),
    
    
    BMARKETCENTER(210, "B端营销中心"),

    // SERVICEPROD(211, "产品服务"),
    SECURITYCENTER(212, "安全中心"),
    BMESSAGECENTER(214, "B端消息中心"),
    DATABANKCOFFERS(215, "数据银行"),
    LOGISTICCENTER(216, "物流中心"),
	RISKCENTER(217, "风控中心"),
    
    // 业务聚合层C端300
    TRADE(300, "C端交易系统"),
    /** 淘宝、美团、京东*/
    TTRADE(301, "第三方交易系统"),
    USERPROD(302, "C端用户产品"),
    CORDERPROD(303, "C端订单管理产品"),
    GROWTHPROD(304, "成长体系产品"),
    CONTENTPROD(305, "C端内容管理"),
    COMMENTPROD(306, "C端评价系统"),
    MARKETPROD(307, "C端营销产品"),
    CMESSAGEPROD(308, "C端消息系统"),
    CARDPROD(309, "卡券系统"),
    CASHIERPROD(310, "收银台"),

    SERVICEPROD(341, "C端服务产品"),
    MALLPROD(342, "商品商城产品"),
    UPKEEPPROD(343, "保养产品"),
    INSURANCEPROD(344, "保险业务"),
    NEWSPROD(345, "咨询产品"), 
    LIFESERVICEPROD(346, "生活服务业务"),
    FUELCARDPROD(347, "加油业务"),
    PECCANCYPROD(348, "违章业务"),
    FOURSPROD(349, "4S业务"),
    
    
    
    
    // 业务聚合层B端400
    BTRADE(400, "B端交易系统"),
    BORDERPROD(401, "B端订单产品"),
    BMERCHANTPROD(402, "B端商户产品"),
    BGOODSPROD(403, "B端商品产品"),
    BMARKETPROD(404, "B端营销产品"),
    BSTOREPROD(405, "B端门店产品"),
    BMESSAGEPROD(406, "B端消息产品"),   
    BCONTENTPROD(407, "B内容产品"),  
    CRMPROD(408, "CRM产品"),   
    SHPROD(409, "商户产品"),
	RISKPROD(410, "风控产品"),



		// BOSS - 支撑系统公共500
    AUTHMANAGER(500, "权限管理平台"),
    OMS(501, "订单管理系统"),
    KEFU(502, "客服管理系统"),
    WMS(503, "仓储管理系统"),
    APIMANAGER(504, "超人网关后台"),
    ORDERMANAGER(505, "订单管理平台"),
    RESOURCECENTER(506, "支撑资源项目"),
    FINANCECENTER(507, "财务系统"),
    DISPATCHPROD(508, "调度中心"),
    DISPATCHMANAGER(509, "调度管理"),
	RISKMANAGER(510, "风控管理"),
	PMS(511, "采购系统"),
	SELLERLOGISTICS(512, "卖家物流管理模块"),
	SELLERCENTER(513, "卖家中心"),  
	SELLERMANAGE(514,  "卖家后台"),  
	SELLERCENTERLOGIN(515,  "卖家登录中心"),
	SELLERMANAGELOGIN(516, "后台登录中心"),
	DATABANK(517, "数据银行"),
	MANAGERPROD(518, "管理员系统"),
	OFFICIAL(519, "SEM推广"),
	NAV(520, "后台系统导航"),
	
	   
    
    // BOSS - 支撑系统C端600
    BACKEND(600, "运营后台"),
    SHOPADMIN(601, "商城管理系统"),
    CONTENTMANAGER(602, "内容管理系统"),
    ITEMMANAGER(603, "商品管理系统"),
    MARKETMANAGER(604, "营销管理系统"),
    CARDMANAGER(605, "卡券管理系统"),
    ICSS(606, "保险客服系统"),  //(insurance customer service system)
    SOXUL(607, "任务服务系统"),
    FOURSBACKEND(608, "4S运营平台"),
    
    // BOSS - 支撑系统B端700
    BOB(700, "B端运营后台"),
    SECURITYMANAGER(701, "安全管理系统"),

	//O2O - 对外应用
	CARMAN(900, "C端APP网关"),
	MARKET(901, "营销活动"),
	SHOP(902, "商城系统"),
	OPENAPI(903, "开放平台"),
	EXTGATEWAY(904, "第三方业务网关"),
	SUPERAPI(905, "超人网关"),
	CASHCOW(950, "B端APP网关"),
	SH(951, "商户系统"),
	SSHOP(952, "商户商城"),
    SUPEROPENAPI(953,"超人开放平台"),
    OPENAPIMANAGER(954,"超人开放平台后台"),
    CARDTICKET(955, "卡券商城"),
    FOURSMANAGER(956, "4S店管理平台"),
    DIRECTSTORE(957, "直营门店系统"),

	//业务中间件 800
	SUPERSEARCH(801, "超人搜索"),
	DATACENTER(802, "数据中心"),
	GOTONE(803, "沟通平台"),
	MESSAGECENTER(804, "消息中心"),
	DATABUS(805, "数据总线"),

	//纯中间件系统
	IKEEPER(1000, "管控平台"),
    SUPERRADAR(1001, "监控平台"),
	SUPERCONFIG(1002, "配置中心"),
	MOCKER(1003, "测试中心"),
    IPORTAL(1004, "技术中心"),
    DBSERVICE(1005, "数据库服务");

	private int code;
	private String desc;

	Source(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	//获取系统码
	public int getCode() {
		return code;
	}

	//获取系统中文名
	public String getDesc() {
		return desc;
	}

	//根据APP_NAME获取Source实例
	public static Source lookup(String appName) {
		checkArgument(isNotBlank(appName), "appName must not be blank");
		return Source.valueOf(appName.toUpperCase());
	}
}
