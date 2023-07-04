package com.xhj.product.feign;

import com..common.to.es.SkuEsModel;
import com..common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Author: xhj
 * @Date: 2023/04/15/13:20
 * @Description:
 */
@FeignClient("xhj-search")
public interface SearchFeignService {

    @PostMapping(value = "/search/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);

}
