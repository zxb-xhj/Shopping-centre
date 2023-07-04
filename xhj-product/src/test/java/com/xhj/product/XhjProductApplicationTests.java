package com.xhj.product;


import com.xhj.product.entity.BrandEntity;
import com.xhj.product.entity.CategoryBrandRelationEntity;
import com.xhj.product.entity.SkuInfoEntity;
.xhj.product.service.*;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class XhjProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryBrandRelationService CategoryBrandRelationService;

    @Autowired
    AttrService attrService;

    @Autowired
    SkuInfoService skuInfoService;


//    @Autowired
//    OSSClient ossClient;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void testredissonClient(){

        System.out.println(redissonClient);
    }

    @Test
    public void teststringRedisTemplate(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("hello","word "+ UUID.randomUUID().toString());

        String hello = ops.get("hello");
        System.out.println(hello);
    }

    @Test
    public void getListCategory(){
        List<Long> listCatelogIds = categoryService.getListCatelogIds(173L);
        log.info("数据为：{}"+listCatelogIds);

        CategoryBrandRelationEntity categoryBrandRelationEntities = CategoryBrandRelationService.getById(9L);
        log.info("======"+categoryBrandRelationEntities);

    }

    @Test
    public void getListCategory1(){
//        List<Long> list = new ArrayList<Long>(10);
//        list.add(1L);
//        list.add(2L);
//        System.out.println(attrService.selectSearchAttrs(list));
        List<SkuInfoEntity> entities = skuInfoService.getSkusBySpuId(29L);
        System.out.println(entities);
    }


    @Test
    public void testUpload() throws FileNotFoundException {

    }

    @Test
    void contextLoads() {
//        QueryChainWrapper<BrandEntity> query = brandService.query();
//        System.out.println(query);
        /*BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("菠萝");
        brandService.save(brandEntity);
        System.out.println("保存成功");*/

        List<BrandEntity> brandId = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 12l));
        brandId.forEach((brand)->{
            System.out.println(brand);
        });

    }


}



