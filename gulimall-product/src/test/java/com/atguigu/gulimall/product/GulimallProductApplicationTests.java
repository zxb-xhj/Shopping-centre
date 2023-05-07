package com.atguigu.gulimall.product;


import com.atguigu.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.service.*;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class GulimallProductApplicationTests {

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
        /*// Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-guangzhou.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tJJDu3Y2T5tBY916yBu";
        String accessKeySecret = "kBQi2R0XLRN9ogANZTN2nt6vwDkYoO";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);*/

        /*// 填写Bucket名称，例如examplebucket。
        String bucketName = "gulimall-xhj";
        // 上传文件流
        InputStream inputStream = new FileInputStream("C:\\Users\\DELL\\Desktop\\资料\\caipi\\海参拼盘.jpeg");
        ossClient.putObject(bucketName,"海参拼盘.jpeg",inputStream);
        // 关闭ossClient
        ossClient.shutdown();
        System.out.println("上传成功....");*/
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
