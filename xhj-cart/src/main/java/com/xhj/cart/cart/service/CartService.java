package com.xhj.cart.cart.service;

import com.xhj.cart.cart.vo.CartItemVo;
import com.xhj.cart.cart.vo.CartVo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Author: xhj
 * @Date: 2023/05/23/15:03
 * @Description:
 */
public interface CartService {
    /**
     * 加入购物车
     * @param skuId
     * @param num
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    CartItemVo addTooCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 购物车信息，防止重复插入
     * @param skuId
     * @return
     */
    CartItemVo getCartItem(Long skuId);

    /**
     * 查看购物车
     * @return
     */
    CartVo getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车
     * @param cartKey
     */
    void clearCart(String cartKey);

    /**
     * 勾选购物项
     * @param skuId
     * @param checked
     */
    void checkItem(Long skuId, Integer checked);
    /**
     * 修改购物项数量
     * @param skuId
     * @param num
     */
    void changeItemCount(Long skuId, Integer num);
    /**
     * 删除购物项
     * @param skuId
     */
    void deleteIdCartInfo(Long skuId);

    List<CartItemVo> getUserCartItems();
}
