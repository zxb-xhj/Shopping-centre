package com.xhj.ware.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: xhj
 * @Date: 2023/06/03/12:48
 * @Description:
 */

@Data
public class FareVo {

    private MemberAddressVo address;

    private BigDecimal fare;

}
