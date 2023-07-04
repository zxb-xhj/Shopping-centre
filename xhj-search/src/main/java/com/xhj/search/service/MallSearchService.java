package com.xhj.search.service;

import com.xhj.search.vo.SearchParam;
import com.xhj.search.vo.SearchResult;

/**
 * @Author: xhj
 * @Date: 2023/05/05/19:19
 * @Description:
 */
public interface MallSearchService {

    /**
     * @param param 检索的所有参数
     * @return  返回检索的结果，里面包含页面需要的所有信息
     */
    SearchResult search(SearchParam param);

}