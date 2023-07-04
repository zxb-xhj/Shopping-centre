package com.xhj.search.feign;

import com..common.to.es.SkuEsModel;
import com..common.utils.R;
import com.xhj.search.feign.fallback.ProductServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/04/15/13:07
 * @Description:
 */
@FeignClient(value = "xhj-product",fallback = ProductServiceFallback.class)
public interface ProductFeignService {

    @GetMapping("/product/attr/info/{attrId}")
    public R attrInfo(@PathVariable("attrId") Long attrId);

    @RequestMapping("/product/productattrvalue/infoList/{id}")
    public List<SkuEsModel.Attrs> infoList(@PathVariable("id") Long id);

}
