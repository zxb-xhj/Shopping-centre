package com.atguigu.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.gulimall.product.dao.CategoryBrandRelationDao;
import com.atguigu.gulimall.product.entity.AttrGroupEntity;
import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;
import com.atguigu.gulimall.product.vo.Category2Vo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationDao categoryBrandRelationDao;

    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;

    /**
     * 分页查询所有分类
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //查出所有分类
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);

        //查出一级分类
        List<CategoryEntity> collect = categoryEntities.stream().filter((categoryEntitie) -> {
            return categoryEntitie.getParentCid() == 0;
        }).map((menu)->{
            menu.setChildren(getCildrens(menu,categoryEntities));
            return menu;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return collect;
    }

    /**
     * 根据id删除分类
     * @param asList
     */
    @Override
    public void removeMenuByIds(List<Long> asList) {
        //ToDo 检查当前删除的菜单是否被引用
        baseMapper.deleteBatchIds(asList);
    }

    /**
     * 根据id查询分类
     * @param catelogId
     * @return
     */
    @Override
    public List<Long> getListCatelogIds(Long catelogId) {
        List<Long> longList = new ArrayList<Long>();
        //select cat_id from pms_category where cat_id = catelogId
        this.getListCatelogId(catelogId,longList);
        Collections.reverse(longList);
        return longList;
    }

    /**
     * 更新分类信息
     * @param category
     */
//    @CacheEvict(value="category", key = "'leve1Categorys'")
    @Caching(evict = {
            @CacheEvict(value="category", key = "'leve1Categorys'"),
            @CacheEvict(value="category", key = "'getListCategoryJson'")
    })
    @Override
    public void updateCategoryBrandRelation(CategoryEntity category) {
        UpdateWrapper<CategoryBrandRelationEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("catelog_name",category.getName()).eq("catelog_id",category.getCatId());
        categoryBrandRelationDao.update(null,updateWrapper);
    }

    /**
     * 批量更新分类信息
     * @param category
     */
    @Override
    public void updateBatchByIdCategoryBrandRelation(CategoryEntity[] category) {
        for (CategoryEntity categoryEntity : category) {
            UpdateWrapper<CategoryBrandRelationEntity> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("catelog_name",categoryEntity.getName()).eq("catelog_id",categoryEntity.getCatId());
            categoryBrandRelationDao.update(null,updateWrapper);
        }
    }

    /**
     * 查询一级分类
     * @return
     */
    @Override
    public List<CategoryEntity> getListOneCategory() {
        return this.baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid",0));
    }

    /**
     * @Cacheable({"Category"}) 如果方法中有则不调用方法，如果没有会调用方法，最后把返回值写入缓存中
     * 默认行为
     *      1、默认自动生成key，缓存名字simpleket：[]（自动生成的key值）
     *      2、缓存的value值，默认使用jdk序列化机制，将数据序列化之后存入redis
     *      3、默认ttl时间 -1 无限制
     * 自定义
     *      1、指定生成缓存使用的key， key属性指定，接受spel
     *      2、指定缓存过期时间，配置文件中修改
     *      3、指定缓存value的值为json类型
     * @return
     */
    @Cacheable(value = {"category"}, key = "'leve1Categorys'")
    @Override
    public List<CategoryEntity> getLeve1Categorys(){
        System.out.println("getLeve1Categorys.....");
        long l = System.currentTimeMillis();
        List<CategoryEntity> listOneCategory = getListOneCategory();
        return listOneCategory;
    }

    //
    @Cacheable(value = {"category"}, key = "#root.methodName")
    @Override
    public Map<String, List<Category2Vo>> getListCategoryJson() {
        Map<String, List<Category2Vo>> stringListMapdb = getStringListMapdb();
        return stringListMapdb;
    }

    /**
     * 缓存里的数据如何和数据库的数据保持一致？？
     * 缓存数据一致性
     * 1)、双写模式
     * 2)、失效模式
     * @return
     */

    public Map<String, List<Category2Vo>> getCatalogJsonFromDbWithRedissonLock() {

        //1、占分布式锁。去redis占坑
        //（锁的粒度，越细越快:具体缓存的是某个数据，11号商品） product-11-lock
        //RLock catalogJsonLock = redissonClient.getLock("catalogJson-lock");
        //创建读锁
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("catalogJson-lock");

        RLock rLock = readWriteLock.readLock();

        Map<String, List<Category2Vo>> dataFromDb = null;
        try {
            rLock.lock();
            //加锁成功...执行业务
            dataFromDb = getStringListMapdb();
        } finally {
            rLock.unlock();
        }
        //先去redis查询下保证当前的锁是自己的
        //获取值对比，对比成功删除=原子性 lua脚本解锁
        // String lockValue = stringRedisTemplate.opsForValue().get("lock");
        // if (uuid.equals(lockValue)) {
        //     //删除我自己的锁
        //     stringRedisTemplate.delete("lock");
        // }

        return dataFromDb;

    }


    //从数据库中查询数据封装数据：：分布式锁
    public Map<String, List<Category2Vo>> getListCategoryJson2() {
        // redis查询key为categorysJson的值
        String categorysJson = redisTemplate.opsForValue().get("categorysJson");
        // 判断redis是否有这个数据
        if (StringUtils.isEmpty(categorysJson)){
            System.out.println("缓存不命中");
            // 执行业务
            Map<String, List<Category2Vo>> categoryJsondb = getListCategoryJsondbRedisLock();
            return categoryJsondb;
        }
        //缓存命中
        System.out.println("缓存命中");
        // 取出redis的值 反序列化
        Map<String, List<Category2Vo>> stringListMap = JSON.parseObject(
                categorysJson, new TypeReference<Map<String, List<Category2Vo>>>() {});
        return stringListMap;
    }

    // redis加锁
    public Map<String, List<Category2Vo>> getListCategoryJsondbRedisLock() {
        String uuid = UUID.randomUUID().toString();
        // 加锁 原子操作 如果没有key lock 则插入 返回true 反之false
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 30, TimeUnit.SECONDS);
        if (lock){
            System.out.println("加锁成功。。。。。。");
            // 执行业务
            Map<String, List<Category2Vo>> stringListMapdb = null;
            try{
                stringListMapdb = getStringListMapdb();
            }finally {
                // 删锁 原子操作
                String scripe = "if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else return 0 end";
                redisTemplate.execute(new DefaultRedisScript<Long>(scripe,Long.class),Arrays.asList("lock"),uuid);
            }
            return stringListMapdb;
        }else {
            try{
                Thread.sleep(200);
            }catch (Exception e){}
            // 加锁失败，重试
            System.out.println("加锁失败。。。。。。");
            return getListCategoryJsondbRedisLock();
        }


    }

    // 加锁
    public Map<String, List<Category2Vo>> getListCategoryJsondb() {
        synchronized (this){
            return getStringListMapdb();
        }
    }

    // 从数据库查询
    @Nullable
    private Map<String, List<Category2Vo>> getStringListMapdb() {
//        String categorysJson = redisTemplate.opsForValue().get("categorysJson");
//        if (!StringUtils.isEmpty(categorysJson)){
//            Map<String, List<Category2Vo>> stringListMap = JSON.parseObject(
//                    categorysJson, new TypeReference<Map<String, List<Category2Vo>>>() {});
//            return stringListMap;
//        }
        System.out.println("查数据库。。。。。。");
        //查出所有分类
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        // 查出所有一级分类
        List<CategoryEntity> categoryEntitie1s = getListCategory(categoryEntities,0L);
        // 把 数据封装成map集合
        Map<String, List<Category2Vo>> listMap = categoryEntitie1s.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            // 根据一级分类id 查出二级分类的信息
            List<CategoryEntity> category2 = getListCategory(categoryEntities,v.getCatId());
            List<Category2Vo> category2Vos = null;
            if (category2!=null){
                // 把二级封装成list集合
                category2Vos = category2.stream().map(c2 -> {
                    // 根据二级分类id 查询三级分类信息
                    List<CategoryEntity> category3s = getListCategory(categoryEntities,c2.getCatId());
                    List<Category2Vo.Category3Vo> category3VoList = null;
                    if (category3s!=null){
                        // 把三级封装成list集合
                        category3VoList = category3s.stream().map(c3 -> {
                            // 封装 三级分类vo 信息
                            Category2Vo.Category3Vo category3Vo = new Category2Vo.Category3Vo(
                                    c2.getCatId().toString(), c3.getCatId().toString(), c3.getName().toString());
                            return category3Vo;
                        }).collect(Collectors.toList());
                    }
                    // 封装 二级分类vo 信息
                    Category2Vo category2Vo = new Category2Vo(v.getCatId().toString(),c2.getCatId().toString(),c2.getName(),category3VoList);
                    return category2Vo;
                }).collect(Collectors.toList());
            }
            return category2Vos;
        }));
//        String jsonString = JSON.toJSONString(listMap);
//        redisTemplate.opsForValue().set("categorysJson",jsonString,1, TimeUnit.DAYS);
        return listMap;
    }

    public List<CategoryEntity> getListCategory(List<CategoryEntity> categorys, Long parent_cid){
        return categorys.stream().filter(category -> category.getParentCid()==parent_cid).collect(Collectors.toList());
    }



    public Long getListCatelogId(Long catelogId,List<Long> longList){
        CategoryEntity category = this.getById(catelogId);
        longList.add(category.getCatId());//173 24
        if (category.getParentCid()!=0){
            this.getListCatelogId(category.getParentCid(),longList);
        }
        return catelogId;
    }

    private List<CategoryEntity> getCildrens(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> collect = all.stream().filter((category) -> {
            return category.getParentCid() == root.getCatId();
        }).map((menu)->{
            menu.setChildren(getCildrens(menu,all));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());
        return collect;
    }

}