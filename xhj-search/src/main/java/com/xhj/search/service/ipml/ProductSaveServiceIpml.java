package com.xhj.search.service.ipml;

import com.alibaba.fastjson.JSON;
import com.common.to.es.SkuEsModel;
import com.xhj.search.config.XhjElasticSearchConfig;
import com.xhj.search.constant.EsConstant;
import com.xhj.search.feign.ProductFeignService;
import com.xhj.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: xhj
 * @Date: 2023/04/15/13:02
 * @Description:
 */
@Service
@Slf4j
public class ProductSaveServiceIpml implements ProductSaveService {

    @Autowired
    RestHighLevelClient highLevelClient;

    @Autowired
    ProductFeignService productFeignService;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        // 在es中建立索引，建立好映射关系

        // 在es中保存这些数据
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            List<SkuEsModel.Attrs> attrs = productFeignService.infoList(skuEsModel.getSpuId());
            log.info("attrs{}",attrs);
            skuEsModel.setAttrs(attrs);
            String s = JSON.toJSONString(skuEsModel);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }

        BulkResponse bulk = highLevelClient.bulk(bulkRequest, XhjElasticSearchConfig.COMMON_OPTIONS);

        //TODO 如果批量错误
        boolean hasFailures = bulk.hasFailures();

        List<String> collect = Arrays.asList(bulk.getItems()).stream().map(item -> {
            return item.getId();
        }).collect(Collectors.toList());

        log.info("商品上架完成：{}",collect);

        return hasFailures;
    }
}
