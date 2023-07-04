package com.xhj.cart.cart.controller;

import com..common.constant.AuthServerConstant;
import com.xhj.cart.cart.service.CartService;
import com.xhj.cart.cart.vo.CartItemVo;
import com.xhj.cart.cart.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Author: xhj
 * @Date: 2023/05/23/15:05
 * @Description:
 */
@Controller
public class CartController {

    @Autowired
    CartService cartService;

    /**
     * 获取当前用户的购物车商品项
     * @return
     */
    @GetMapping(value = "/currentUserCartItems")
    @ResponseBody
    public List<CartItemVo> getCurrentCartItems() {
        List<CartItemVo> cartItemVoList = cartService.getUserCartItems();
        return cartItemVoList;
    }

    /**
     * 修改数量
     * @param skuId
     * @return
     */
    @GetMapping("/deleteItem")
    public String deleteIdCartInfo(@RequestParam("skuId") Long skuId){
        cartService.deleteIdCartInfo(skuId);
        return "redirect:http://cart.xhj.com/cart.html";
    }

    /**
     * 修改数量
     * @param skuId
     * @param num
     * @return
     */
    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num){
        cartService.changeItemCount(skuId,num);
        return "redirect:http://cart.xhj.com/cart.html";
    }

    /**
     * 勾选购物项
     * @param skuId
     * @param checked
     * @return
     */
    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,@RequestParam("checked") Integer checked){
        cartService.checkItem(skuId,checked);
        return "redirect:http://cart.xhj.com/cart.html";
    }

    /**
     * 查看购物车
     * @param model
     * @return
     */
    @GetMapping("/cart.html")
    public String carListPage(Model model) throws ExecutionException, InterruptedException {
        CartVo cartVo = cartService.getCart();
        model.addAttribute("cart",cartVo);
        return "cartList";
    }

    /**
     * 添加商品到购物车
     * @param skuId
     * @param num
     * @param attributes
     * @return
     */
    @GetMapping("/addCartItem")
    public String addCartItem(@RequestParam("skuId")Long skuId,
                              @RequestParam("num")Integer num,
                              RedirectAttributes attributes) throws ExecutionException, InterruptedException {
        CartItemVo cartItemVo = cartService.addTooCart(skuId,num);
//        model.addAttribute("cartItem",cartItemVo);
        attributes.addAttribute("skuId",skuId);
        return "redirect:http://cart.xhj.com/addToCartSuccess.html";
    }

    /**
     * 购物车信息，防止重复插入
     * @param skuId
     * @param model
     * @return
     */
    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccess(@RequestParam("skuId") Long skuId,Model model){
        CartItemVo cartItemVo = cartService.getCartItem(skuId);
        model.addAttribute("cartItem",cartItemVo);
        return "success";
    }



}
