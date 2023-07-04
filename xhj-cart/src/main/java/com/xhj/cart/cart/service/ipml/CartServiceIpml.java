package com.xhj.cart.cart.service.ipml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.common.constant.CartConstant;
import com.common.utils.R;
import com.xhj.cart.cart.feign.ProductFeignService;
import com.xhj.cart.cart.interceptor.CartInterceptor;
import com.xhj.cart.cart.service.CartService;
import com.xhj.cart.cart.vo.CartItemVo;
import com.xhj.cart.cart.vo.CartVo;
import com.xhj.cart.cart.vo.SkuInfoVo;
import com.xhj.cart.cart.vo.UserInfoTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @Author: xhj
 * @Date: 2023/05/23/15:04
 * @Description:
 */
@Service
public class CartServiceIpml implements CartService {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    ThreadPoolExecutor executor;

    // 存入redis里面key的前缀
    private final String CART_PREFIX = "xhj:cart:";

    /**
     * 添加商品到购物车
     *
     * @param skuId
     * @param num
     * @return
     */
    @Override
    public CartItemVo addTooCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {
        BoundHashOperations<String, Object, Object> cartOps = getOperations();
        // 获取redis里该商品的数据
        String res = (String) cartOps.get(skuId.toString());
        // 判断redis购物车是否有该商品
        if (StringUtils.isEmpty(res)) {  // 如果为空就直接插入
            //2、添加新的商品到购物车(redis)
            CartItemVo cartItemVo = new CartItemVo();
            //开启第一个异步任务
            CompletableFuture<Void> getSkuInfoFuture = CompletableFuture.runAsync(() -> {
                //1、远程查询当前要添加商品的信息
                R productSkuInfo = productFeignService.getInfo(skuId);
                SkuInfoVo skuInfo = productSkuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                //数据赋值操作
                cartItemVo.setSkuId(skuInfo.getSkuId());
                cartItemVo.setTitle(skuInfo.getSkuTitle());
                cartItemVo.setImage(skuInfo.getSkuDefaultImg());
                cartItemVo.setPrice(skuInfo.getPrice());
                cartItemVo.setCount(num);
            }, executor);

            //开启第二个异步任务
            CompletableFuture<Void> getSkuAttrValuesFuture = CompletableFuture.runAsync(() -> {
                //2、远程查询skuAttrValues组合信息
                List<String> skuSaleAttrValues = productFeignService.getSkuSaleAttrValues(skuId);
                cartItemVo.setSkuAttrValues(skuSaleAttrValues);
            }, executor);

            //等待所有的异步任务全部完成
            CompletableFuture.allOf(getSkuInfoFuture, getSkuAttrValuesFuture).get();

            String cartItemJson = JSON.toJSONString(cartItemVo);
            cartOps.put(skuId.toString(), cartItemJson);
            return cartItemVo;
        } else {  // 否则修改购物车里该商品的数量
            CartItemVo cartItemVo = JSON.parseObject(res, CartItemVo.class);
            // 修改数量
            cartItemVo.setCount(cartItemVo.getCount() + num);
            // 存入redis
            cartOps.put(skuId.toString(), JSON.toJSONString(cartItemVo));
            return cartItemVo;
        }
    }

    /**
     * 查询加入购物车的商品
     *
     * @param skuId
     * @return
     */
    @Override
    public CartItemVo getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> operations = getOperations();
        String o = (String) operations.get(skuId.toString());
        if (o != null) {
            CartItemVo cartItemVo = JSON.parseObject(o, CartItemVo.class);
            return cartItemVo;
        }
        return null;
    }

    /**
     * 查看购物车
     *
     * @return
     */
    @Override
    public CartVo getCart() throws ExecutionException, InterruptedException {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        CartVo cartVo = new CartVo();
        if (userInfoTo.getUserId() != null) {
            // 登录了，获取redis前缀
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            // 查询临时用户的购物车
            List<CartItemVo> cartItem = getCartItem(CART_PREFIX + userInfoTo.getUserKey());

            if (cartItem!=null&& cartItem.size()>0){
                // 遍历临时用户的商品加入登录用户的购物车
                for (CartItemVo cartItemVo : cartItem) {
                    addTooCart(cartItemVo.getSkuId(),cartItemVo.getCount());
                }
                // 把临时购物车的商品清空
                clearCart(CART_PREFIX + userInfoTo.getUserKey());
            }
            // 获取购物车商品（合并的）
            List<CartItemVo> cartItemVos = getCartItem(cartKey);
            cartVo.setItems(cartItemVos);
            return cartVo;
        } else {
            // 没登录
            String cartKey = CART_PREFIX + userInfoTo.getUserKey();
            // 查出该用户购物车里的所有商品
            List<CartItemVo> cartItem = getCartItem(cartKey);
            // 存入
            cartVo.setItems(cartItem);
        }
        return cartVo;
    }

    /**
     * 清空购物车
     * @param cartKey
     */
    @Override
    public void clearCart(String cartKey) {
        redisTemplate.delete(cartKey);
    }

    /**
     * 勾选购物项
     * @param skuId
     * @param checked
     */
    @Override
    public void checkItem(Long skuId, Integer checked) {
        BoundHashOperations<String, Object, Object> operations = getOperations();
        CartItemVo cartItem = getCartItem(skuId);
        cartItem.setCheck(checked==1?true:false);
        String s = JSON.toJSONString(cartItem);
        operations.put(skuId.toString(),s);
    }

    /**
     * 对固定的文件夹进行操作
     *
     * @return
     */
    private BoundHashOperations<String, Object, Object> getOperations() {
        // 获取当前线程存入的用户信息
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        // 把redis的key加上前缀
        String cartKey = "";
        if (userInfoTo.getUserId() != null) {
            // 登录用户
            cartKey = CART_PREFIX + userInfoTo.getUserId();
        } else {
            // 临时用户
            cartKey = CART_PREFIX + userInfoTo.getUserKey();
        }
        // 对固定的文件夹进行redis操作
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        return operations;
    }

    public List<CartItemVo> getCartItem(String cartKey){
        BoundHashOperations<String, Object, Object> ops = redisTemplate.boundHashOps(cartKey);
        List<Object> values = ops.values();
        if (values != null && values.size() > 0){
            List<CartItemVo> cartItemVoList = values.stream().map((obj) -> {
                // 转成CartItemVo
                String s = (String) obj;
                CartItemVo cartItemVo = JSON.parseObject(s, CartItemVo.class);
                return cartItemVo;
            }).collect(Collectors.toList());
            return cartItemVoList;
        }
        return null;
    }

    /**
     * 修改购物项数量
     * @param skuId
     * @param num
     */
    @Override
    public void changeItemCount(Long skuId, Integer num) {

        //查询购物车里面的商品
        CartItemVo cartItem = getCartItem(skuId);
        cartItem.setCount(num);

        BoundHashOperations<String, Object, Object> cartOps = getOperations();
        //序列化存入redis中
        String redisValue = JSON.toJSONString(cartItem);
        cartOps.put(skuId.toString(),redisValue);
    }

    /**
     * 删除购物项
     * @param skuId
     */
    @Override
    public void deleteIdCartInfo(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getOperations();
        cartOps.delete(skuId.toString());
    }

    @Override
    public List<CartItemVo> getUserCartItems() {
        List<CartItemVo> cartItemVoList = new ArrayList<>();
        //获取当前用户登录的信息
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        //如果用户未登录直接返回null
        if (userInfoTo.getUserId() == null) {
            return null;
        } else {
            //获取购物车项
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            //获取所有的
            List<CartItemVo> cartItems = getCartItem(cartKey);
//            if (cartItems == null) {
//                throw new CartExceptionHandler();
//            }
            //筛选出选中的
            cartItemVoList = cartItems.stream()
                    .filter(items -> items.getCheck())
                    .map(item -> {
                        //更新为最新的价格（查询数据库）
                        BigDecimal price = productFeignService.getPrice(item.getSkuId());
                        item.setPrice(price);
                        return item;
                    })
                    .collect(Collectors.toList());
        }

        return cartItemVoList;
    }
}
