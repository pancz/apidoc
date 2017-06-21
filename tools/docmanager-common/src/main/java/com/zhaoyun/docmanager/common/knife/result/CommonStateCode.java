/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.knife.result;

/**
 * 公共/遗留状态码
 * 
 * @author user
 * @version $Id: CommonStateCode.java, v 0.1 2015年9月10日 下午5:18:51 user Exp $
 */
public interface CommonStateCode {
	//基础状态码
	StateCode SUCCESS = new StateCode(0, "成功");
	StateCode ILLEGAL_PARAMETER = new StateCode(-102, "无效的参数");
	StateCode DUPLICATE_INVOKE = new StateCode(-108,"重复调用");
	StateCode PARAMETER_LACK =  new StateCode(-101, "请求参数缺失");
	StateCode BODY_LACK =  new StateCode(-100, "post请求消息体缺失");
	StateCode ILLEGAL_TOKEN =  new StateCode(-106, "非法的token");
	StateCode NO_RELATED_DATA =  new StateCode(-107,"无相关数据");
	StateCode FAILED =  new StateCode(-110, "操作失败");
	StateCode OPERATE_UP_LIMIT =  new StateCode(-111, "操作上限");
	StateCode DATA_EMPTY =  new StateCode(-302,"返回数据为空");
	StateCode PARSE_ERROR =  new StateCode(-306,"参数解析失败");
	StateCode DATA_ERROR =  new StateCode(-307,"返回数据格式错误");
	StateCode ILLEGAL_SIGN = new StateCode(-701, "无效的签名");
	StateCode OVERDUE_TIME = new StateCode(-702, "超时的时间戳");
	StateCode ERROR_REQUEST = new StateCode(-995,"错误的请求");
	StateCode DB_ERROR = new StateCode(-996,"数据库异常");
	StateCode INNER_SERVER_ERROR = new StateCode(-997, "非正常的内部服务");
	StateCode SERVERS_LINK_ERROR = new StateCode(-998, "服务器间通信异常");
	StateCode OTHER_SERVER_ERROR = new StateCode(-999, "亲，服务器君又开小差了.您放心，我们的程序猿正批评教育之");

	//优惠券
	StateCode COUPON_USELESS_UNUSED =  new StateCode(101, "优惠券失效或已用，未使用优惠券");
	StateCode COUPON_USELESS =  new StateCode(-503, "优惠券过期或已使用");
	StateCode COUPON_NOT_SUIT_AREA =  new StateCode(-504, "亲，活动不支持当前城市，详见优惠券规则");
	StateCode COUPON_UP_LIMIT =  new StateCode(-532, "优惠券领取上限");
	StateCode COUPON_BIGGER_THAN_ORDER =  new StateCode(-509, "优惠券金额大于订单金额");//优惠券不适用
	StateCode COUPON_NO_RIGHT =  new StateCode(-512, "不能领取优惠券");
	StateCode COUPON_NOT_SUIT =  new StateCode(-514, "优惠券不适用");

	//用户
	StateCode NEED_LOGIN =  new StateCode(-200,"需要先登录");
	StateCode USER_UNREGISTERED =  new StateCode(-201,"用户不存在");
	StateCode ERROR_PASSWORD =  new StateCode(-202,"密码不正确");
	StateCode USELESS_VALIDATE_CODE =  new StateCode(-203,"无效的验证码");
	StateCode USER_EXISTS =  new StateCode(-204,"用户已存在");
	StateCode ERROR_YPASSWORD =  new StateCode(-205,"原密码不正确");
	StateCode USERCAR_LIMIT  =  new StateCode(-50, "我的车型库超过上限");
	
	//门店、服务
	StateCode NO_COEXIST = new StateCode(-303,"type和item这两个参数不能同时存在");
	StateCode VALUE1_ERROR = new StateCode(-304,"否则返回参数数值错误。只能选0或1");
	StateCode VALUE2_ERROR = new StateCode(-305,"否则返回参数数值错误。只能选2或1");
	StateCode DATA_EXIT = new StateCode(-308,"商品服务已经存在");
	StateCode PLEASE_OPEN_LBS = new StateCode(-309, "亲，请启动定位服务");
	StateCode STORE_MAY_OFFLINE = new StateCode(-310, "您的门店已下架或待审核，请联系业务人员");
	StateCode STORE_NOT_SUIT_CENT_WASH = new StateCode(-513, "该门店不符合一分钱洗车活动参与条件");
	StateCode ORDER_ILLEGAL_REQUEST = new StateCode(-501, "非法获取订单信息");
	StateCode FAKE_ORDER_DATA = new StateCode(-502, "相关门店的该服务可能临时下线,请选择其他门店");
	
	//订单、评价
	//StateCode ORDER_STAT_NOT_SUIT = new StateCode(-515, "订单状态不符合");
	StateCode ORDER_REFUSE_REFUND = new StateCode(-506, "不允许退款类型。如一分钱洗车/订单已取消等");
	StateCode ORDER_BELONG_OTHER_STORE = new StateCode(-507, "订单属于其他门店");
	StateCode ALREADY_VERIFY = new StateCode(-508, "该验证码已使用");
	StateCode ORDER_DUPLI_SUBMIT = new StateCode(-510, "订单重复提交");
	StateCode ORDER_EXISTS_COMMENT = new StateCode(-511, "重复的评价提交");

	//商品
	StateCode GOODS_NOT_EXIST = new StateCode(-601, "商品不存在");
	StateCode GOODS_COUNT_ZERO = new StateCode(-602, "库存不足");
	StateCode GOODS_COUNT_FAIL = new StateCode(-603, "商品数量不合法");
	StateCode GOODS_EXCEED_LIMIT_NUM = new StateCode(-604, "购买商品已超过限购数量");
	StateCode GOODS_NOT_EXIST_IN_CART = new StateCode(-605, "购物车中不存在此商品");
	StateCode PACKAGE_UP_LIMIT = new StateCode(-606, "套餐目前每人每天限购一次");
	
	//App版本
	StateCode MUST_UPDATE = new StateCode(-114, "您的版本过旧或者存在严重隐患，请务必更新");
	StateCode COULD_UPDATE = new StateCode(-115, "汽车超人推出新版本啦~");
	//其他
//	StateCode UPLOAD_IMAGE_NOT_LEGAL = new StateCode(-112, "上传图片格式错误，只支持"+FileUploadUtil.getLegalExtFileName(),FileUploadUtil.FILE_TYPE_IMAGE));
//	StateCode UPLOAD_STATIC_NOT_LEGAL = new StateCode(-113, "上传静态资源格式错误，只支持"+FileUploadUtil.getLegalExtFileName = new StateCode(FileUploadUtil.FILE_TYPE_STATIC));
}
