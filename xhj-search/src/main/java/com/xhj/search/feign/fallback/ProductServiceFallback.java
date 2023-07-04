package com.xhj.search.feign.fallback;

import com..common.exception.BizCodeEnum;
import com..common.to.es.SkuEsModel;
import com..common.utils.R;
import com.xhj.search.feign.ProductFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/05/30/19:18
 * @Description:
 */
@Component
@Slf4j
public class ProductServiceFallback implements ProductFeignService {
    @Override
    public R attrInfo(Long attrId) {
        log.info("熔断方法调用");
        return R.error(BizCodeEnum.TO_MANY_REQUEST.getCode(), BizCodeEnum.TO_MANY_REQUEST.getMsg());
    }

    @Override
    public List<SkuEsModel.Attrs> infoList(Long id) {
        return null;
    }
}
