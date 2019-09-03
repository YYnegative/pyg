package com.pinyougou.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.common.util.CookieUtils;
import com.pinyougou.vo.Cart;
import com.pinyougou.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/cart")
@RestController
public class CartController {
    //在浏览器中的cookie的名称
    private static final String COOKIE_CART_LIST = "PYG_CART_LIST";
    //在浏览器中的cookie的生存周期
    private static final int COOKIE_CART_LIST_MAX_AGE = 3600*24;

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @Reference
    private CartService cartService;

    /**
     * 将购买的商品和数量加入到购物车列表
     * @param itemId 商品sku id
     * @param num 购买数量
     * @return 操作结果
     */
    @GetMapping("/addItemToCartList")
    public Result addItemToCartList(Long itemId, Integer num){
        try {
            //1、获取购物车列表；
            List<Cart> cartList = findCartList();
            //2、调用业务往购物车列表中添加购买的商品和数量
            List<Cart> newCartList = cartService.addItemToCartList(cartList, itemId, num);
            //判断用户是否登录；如果没有登录则用户名为 anonymousUser
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            if ("anonymousUser".equals(username)) {
                //3、未登录- 将最新的购物车保存到cookie
                String cartListJsonStr = JSON.toJSONString(newCartList);
                CookieUtils.setCookie(request, response, COOKIE_CART_LIST, cartListJsonStr, COOKIE_CART_LIST_MAX_AGE, true);
            } else {
                //3、已登录- 将最新的购物车保存到redis
                cartService.saveCartListInRedisByUsername(newCartList, username);
            }
            //4、返回操作结果
            return Result.ok("加入购物车成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("加入购物车失败！");
    }

    /**
     * 登录与未登录情况下
     * 获取购物车数据
     * @return 购物车列表
     */
    @GetMapping("/findCartList")
    public List<Cart> findCartList(){
        //判断用户是否登录；如果没有登录则用户名为 anonymousUser
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(username)) {
            //未登录- 从cookie中获取购物车
            String cartListJsonStr = CookieUtils.getCookieValue(request, COOKIE_CART_LIST, true);
            List<Cart> cookieCartList;
            if (!StringUtils.isEmpty(cartListJsonStr)) {
                cookieCartList = JSON.parseArray(cartListJsonStr, Cart.class);
                return cookieCartList;
            } else {
                return new ArrayList<>();
            }
        } else {
            //已登录- 从redis中获取购物车
            List<Cart> redisCartList = cartService.findCartListInRedisByUsername(username);
            return redisCartList;
        }
    }

    /**
     * 获取当前登录用户信息
     * @return 用户信息
     */
    @GetMapping("/getUsername")
    public Map<String, Object> getUsername(){
        Map<String, Object> map = new HashMap<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        map.put("username", username);
        return map;
    }
}
