package com.atguigu.gulimall.search.service.ipml;

import com.atguigu.gulimall.search.config.GulimallElasticSearchConfig;
import com.atguigu.gulimall.search.constant.EsConstant;
import com.atguigu.gulimall.search.service.MallSearchService;
import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author: xhj
 * @Date: 2023/05/05/19:26
 * @Description:
 */
@Service
public class MallSearchServiceIpml implements MallSearchService {

    @Autowired
    RestHighLevelClient highLevelClient;

    @Override
    public SearchResult search(SearchParam param) {

        // 动态构建出查询需要得DSL语句
        SearchResult result = null;

        // 1、准备检索请求
        SearchRequest searchRequest = buildSearchRequest(param);

        try {
            // 2、执行检索请求
            SearchResponse search = highLevelClient.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

            // 3、分析响应数据，封装成我们想要得数据
            result = buildSearchResult(search);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 构建结果数据
     * @param search
     * @return
     */
    private SearchResult buildSearchResult(SearchResponse search) {
        return null;
    }

    /**
     * 执行检索请求
     * @param param
     * @return
     */
    private SearchRequest buildSearchRequest(SearchParam param) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        /**
         * 查询：过滤（按照属性、分类、品牌、价格区间、库存）
         */
        // 1、构建bool - query
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 1.1、must-模糊匹配
        if (!StringUtils.isEmpty(param.getKeyword())){
            boolQuery.must(QueryBuilders.matchQuery("skuTitle",param.getKeyword()));
        }
        // 1.2、bool - filter - 按照三级分类id查询
        if (param.getCatalog3Id()!=null){
            boolQuery.filter(QueryBuilders.termQuery("catalogId",param.getCatalog3Id()));
        }
        // 1.3、bool - filter - 按照品牌d查询
        if (param.getBrandId()!=null){
            boolQuery.filter(QueryBuilders.termQuery("brandId",param.getBrandId()));
        }
        // 1.4、bool - filter - 按照所有指定的属性查询
        if (param.getAttrs()!=null && param.getAttrs().size()>0){
            param.getAttrs().forEach(item->{
                //attrs=1_5寸:8寸&2_16G:8G
                BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

                //attrs=1_5寸:8寸
                String[] s = item.split("_");
                String attrId = s[0];
                String[] attrs = s[1].split(":");
                boolQuery.must(QueryBuilders.termQuery("attrs.attrId",attrId));
                boolQuery.must(QueryBuilders.termsQuery("attrs.attrValue",attrs));

                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", boolQuery, ScoreMode.None);
                boolQuery.filter(nestedQuery);
            });
        }
        // 1.5、bool - filter - 按照是否有库存查询
        if (param.getHasStock()!=null){
            boolQuery.filter(QueryBuilders.termQuery("hasStock",param.getHasStock()==1));
        }
        // 1.6、bool - filter - 按照价格区间查询
        if (!StringUtils.isEmpty(param.getSkuPrice())){
            //skuPrice形式为：1_500或_500或500_
            RangeQueryBuilder skuPrice = QueryBuilders.rangeQuery("skuPrice");
            String[] peice = param.getSkuPrice().split("_");
            if (peice.length==2){
                skuPrice.gte(peice[0]).lte(peice[1]);
            }else if (peice.length==1){
                if (param.getSkuPrice().startsWith("_")){
                    skuPrice.lte(peice[1]);
                }
                if (param.getSkuPrice().endsWith("_")){
                    skuPrice.gte(peice[0]);
                }
            }
            boolQuery.filter(skuPrice);
        }
        //把以前的所有条件都拿来进行封装
        sourceBuilder.query(boolQuery);

        /**
         * 排序，分页，高亮
         */

        //排序
        //形式为sort=hotScore_asc/desc
        if (!StringUtils.isEmpty(param.getSort())){
            String[] sort = param.getSort().split("_");
            SortOrder sortOrder = "asc".equalsIgnoreCase(sort[1]) ? SortOrder.ASC : SortOrder.DESC;

            sourceBuilder.sort(sort[0],sortOrder);
        }

        // 分页
        sourceBuilder.from((param.getPageNum()-1)* EsConstant.PRODUCT_PAGESIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);

        // 高亮
        if (!StringUtils.isEmpty(param.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }

        /**
         * 聚合分析
         */

        String s = sourceBuilder.toString();
        System.out.println("构建的DSL：{}"+s);

        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX},sourceBuilder);
        return searchRequest;
    }
}
