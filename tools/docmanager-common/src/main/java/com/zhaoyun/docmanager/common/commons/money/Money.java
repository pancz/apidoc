/**
 * com Inc
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.zhaoyun.docmanager.common.commons.money;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;

import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;

/**
 * 金额表现类，用于表示系统中的金额及其计算逻辑的封装
 * 
 * 支持 毫钱（long）、分（long）、元（double）  
 * 
 * @version $Id: Money.java, v 1.0.1 2015年12月31日 下午2:05:04 user Exp $
 *
 */
public class Money implements Serializable, Comparable<Money> {
	private static final long serialVersionUID = -1459240821831878390L;
	private static final long hundred = 100;
//	private static final double hundredD = 100.0;

	/** 金额, 精度到毫钱  */
	private long milliAmount;
	
	/** 金额, 精度到分  */
	private long amount;
	
	
	public Money() {}
	
	/** 通过毫钱构造Money类 */
	public static Money ofMilli(long milli) {
		Money money = new Money();
		money.setMilliAmount(milli);
		return money;
	}
	
	/** 通过分构造Money类 */
	public static Money ofCent(long cent) {
		Money money = new Money();
		money.setMilliAmount(convertCentToMilli(cent));
		return money;
	}
	
	/** 通过元构造Money类 */
	public static Money ofYuan(double yuan) {
		Money money = new Money();
		money.setMilliAmount( convertYuanToMilli(yuan));
		return money;
	}

	/** 金额转化为分 */
	public long getMilli() {
		return getMilliAmount();
	}
	
	/** 金额转化为分 */
	public long getCent() {
		return getMilliAmount() / hundred;
	}
	
	/** 金额转化为元 */
	public double getYuan() {	
		return BigDecimal.valueOf(getMilliAmount()).movePointLeft(4)
		.setScale(2, RoundingMode.HALF_UP).doubleValue();
	}
	
	/** 通过毫钱构造Money类 */
	public void setMilli(long milli) {
		setMilliAmount(milli);
	}
	
	/** 通过分构造Money类 */
	public void setCent(long cent) {
		setMilliAmount(cent * hundred);
	}
	
	/** 通过元构造Money类 */
	public void setYuan(double yuan) {
		setMilliAmount(convertYuanToMilli(yuan));
	}

	/**
	 * 计算总金额
	 * 
	 * @param monies 需要计算总金额的Money实例
	 * @return 包含总金额的Money结果实例
	 */
	public static Money total(Money... monies) {
		if (monies.length == 0) {
            throw new IllegalArgumentException("Money array must not be empty");
        }
        return total(Lists.newArrayList(monies));
	}
	
	/**
	 * 计算总金额
	 * 
	 * @param monies 需要计算总金额的Money实例
	 * @return 包含总金额的Money结果实例
	 */
	public static Money total(Iterable<? extends Money> monies) {
        checkNotNull(monies, "Money iterator must not be null");
        Iterator<? extends Money> it = monies.iterator();
        if (it.hasNext() == false) {
            throw new IllegalArgumentException("Money iterator must not be empty");
        }
        Money total = it.next();
        checkNotNull(total, "Money iterator must not contain null entries");
        while (it.hasNext()) {
            total = total.plus(it.next());
        }
        return total;
    }

	/** 
	 * 增加金额
	 * 
	 * @param money 包含要增加的额度的Money实例
	 * @return 增加完额度后的Money实例
	 */
	public Money plus(Money money) {
		checkNotNull(money, "money must not be null");
		return plusCent(money.getCent());
	}
	/**
	 * 以毫钱的形式增加金额
	 * 
	 * @param cent 要增加的额度，以分表示
	 * @return 增加完额度后的Money实例
	 */
	public Money plusMilli(long Milli) {
		setMilliAmount(this.milliAmount + Milli);
		return this;
	}
	
	/**
	 * 以分的形式增加金额
	 * 
	 * @param cent 要增加的额度，以分表示
	 * @return 增加完额度后的Money实例
	 */
	public Money plusCent(long cent) {
		setMilliAmount(this.milliAmount + convertCentToMilli(cent));
		return this;
	}
	
	/**
	 * 以元的形式增加金额
	 * 
	 * @param yuan 要增加的额度，以元表示
	 * @return  增加完额度后的Money实例
	 */
	public Money plusYuan(double yuan) {
		if(yuan == 0.0) { return this; }
		setMilliAmount(this.milliAmount + convertYuanToMilli(yuan));
		return this;
	}

	/**
	 * 减少金额
	 * 
	 * @param money 包含要减少的额度的Money实例
	 * @return 减少金额后的Money实例
	 */
	public Money minus(Money money) {
		checkNotNull(money, "money must not be null");
		return minusCent(money.getCent());
	}

	/**
	 * 以分的形式减少金额
	 * 
	 * @param cent 要减少的额度，以分表示
	 * @return 减少金额后的Money实例
	 */
	public Money minusMilli(long milli) {
		setMilliAmount(this.milliAmount - milli);
		return this;
	}
	
	/**
	 * 以分的形式减少金额
	 * 
	 * @param cent 要减少的额度，以分表示
	 * @return 减少金额后的Money实例
	 */
	public Money minusCent(long cent) {
		setMilliAmount(this.milliAmount - convertCentToMilli(cent));
		return this;
	}

	/**
	 * 以元的形式减少金额
	 * 
	 * @param cent 要减少的额度，以元表示
	 * @return 减少金额后的Money实例
	 */
	public Money minusYuan(double yuan) {
		if (yuan == 0.0) { return this; }
		setMilliAmount(this.milliAmount - convertYuanToMilli(yuan));
		return this;
	}

	/**
	 * 当前额度做乘法计算
	 * 
	 * @param valueToMultiplyBy 乘数  double
	 * @return 进行乘法计算后的Moeny结果实例
	 */
	public Money multipliedBy(double valueToMultiplyBy) {
		if (valueToMultiplyBy == 1) {
			return this;
		}
		double newAmountCent = getMilliAmount() * valueToMultiplyBy;
		setMilliAmount(BigDecimal.valueOf(newAmountCent)
						.setScale(0, RoundingMode.HALF_UP).longValue()); 
		return this;
	}
	
	/**
	 * 当前额度做乘法计算
	 * 
	 * @param valueToMultiplyBy  long
	 * @return 进行乘法计算后的Moeny结果实例
	 */
	public Money multipliedBy(long valueToMultiplyBy) {
		if (valueToMultiplyBy == 1) {
			return this;
		}
		setMilliAmount(getMilliAmount() * valueToMultiplyBy); 
		return this;
	}
	
	/** 判断该Money实例的金额是否为负 */
	public boolean isNegative() {
		return getMilliAmount() < 0;
	}

	/** 判断该Money实例的金额是否为负或者为零 */
	public boolean isNegativeOrZero() {
		return getMilliAmount() <= 0;
	}
	
	/** 判断两个Money实例是否额度相等 */
	public boolean isEqual(Money other) {
        return compareTo(other) == 0;
    }

	/** 判断Money实例的金额是否大于要比较的Money实例 */
    public boolean isGreaterThan(Money other) {
        return compareTo(other) > 0;
    }

    /** 判断Money实例的金额是否小于要比较的Money实例 */
    public boolean isLessThan(Money other) {
        return compareTo(other) < 0;
    }
    
    /** 构造金额为零的Money实例  */
	public static Money zero() {
		return Money.ofCent(0);
	}

	/** 将金额从元转为分*/
	public static Long convertYuanToCent(double yuan) {
		return BigDecimal.valueOf(yuan).movePointRight(2)
				.setScale(0, RoundingMode.HALF_UP).longValue();
	}

	/** 将金额从元转为毫*/
	public static Long convertYuanToMilli(double yuan) {
		return BigDecimal.valueOf(yuan).movePointRight(4)
				.setScale(0, RoundingMode.HALF_UP).longValue();
	}
	
	/** 将金额从分转为元 */
	public static double convertCentToYuan(long cent) {
		return cent / 100.0;
	}
	
	/** 将金额从分转为 毫 */
	public static long convertCentToMilli(long cent) {
		return cent * hundred;
	}

	
	public long getMilliAmount() {
		return this.milliAmount;
	}
	
	
	/** milliamount的单位为毫分 */
	public void setMilliAmount(long milliAmount) {
		this.amount = milliAmount /hundred;
        this.milliAmount = milliAmount;
    }
	
	public long getAmount() {
		return this.amount;
	}
	
	/** amount的单位为分 */
	public void setAmount(long amount) {
		this.milliAmount = amount * hundred;
		this.amount = amount;
	}

    @Override
	public int hashCode() {
		return Longs.hashCode(milliAmount);
	}
	 
	@Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Money) {
            Money otherMoney = (Money) other;
            return milliAmount == otherMoney.milliAmount;
        }
        return false;
    }

	@Override
	public int compareTo(Money otherMoney) {
		if(otherMoney == null) { return 1; }

		return Longs.compare(milliAmount, otherMoney.milliAmount);
	}
	
	@Override
	public String toString() {
		return getMilliAmount() + " milli";
	}
	
}
