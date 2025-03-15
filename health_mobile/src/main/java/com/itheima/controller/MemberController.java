package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 登录表现层
 * @author pc
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;

    /**
     * 登录
     * @param map 页面传过来的数据
     * @return
     */
    @RequestMapping("/login")
    public Result check(HttpServletResponse response, @RequestBody Map map){

        //1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String validateInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (validateInRedis != null && validateCode != null && !validateInRedis.equals(validateCode)){
            //验证码不匹配，直接返回
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }else {
            //2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
              Member member =  memberService.findByTelephone(telephone);
              if (member == null ){
                  //说明不是会员,将其添加为会员
                  member = new Member();
                  member.setPhoneNumber(telephone);
                  member.setRegTime(new Date());
                  memberService.add(member);
              }
            //3、向客户端写入Cookie，内容为用户手机号
            Cookie cookie = new Cookie("cookie_member2_telephone",telephone);
              cookie.setPath("/");//设置路径
              cookie.setMaxAge(60*60*24*30);//有有效期30天
            //将cookie添加到respone
            response.addCookie(cookie);
            //4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,60*30,json);
            //5.返回结果
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }
    }


    /**
     * 个人信息回显
     *
     * @return
     */
    @RequestMapping("/findPersonalInformation")
    public Result findPersonalInformation(HttpServletRequest request,String name) {
        try {

            Member member = memberService.findByUsername(name);
            if (member != null){
                return new Result(true,"查询会员信息成功",member);
            }
            //1.获取到cookie
//            Cookie[] cookies = request.getCookies();
//            if (cookies != null) {
//                //2.遍历cookie
//                for (Cookie cookie : cookies) {
//                    //3.对取出来的cookie进行对比
//                    if ("cookie".equals(cookie.getName())) {
//                        //4.取出cookie的值
//                        String cookieValue = cookie.getValue();
//                        //5.获取到用户名
//                        String username = jedisPool.getResource().get(cookieValue);
//                        //6.根据用户名查询会员，是否存在
//                        Member member = memberService.findByUsername(username);
//                        //7.返回结果
//                        return new Result(true,"获取当前用户信息成功",member);
//                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"获取当前用户信息失败");
        }
        return null;
    }

    /**
     * 根据密码登录
     * @param map
     * @param resp
     * @param req
     * @return
     */
    @RequestMapping("/loginByNameAndPwd")
    public Result loginByNameAndPwd(@RequestBody Map map, HttpServletResponse resp, HttpServletRequest req) {
        String name = (String) map.get("name");
        String password = (String) map.get("password");

        Member member = memberService.findByNameAndPassword(name, password);

        if (member != null) {
            String idCard = member.getIdCard();

            String uuid = UUID.randomUUID().toString();
            //存入cookie
            Cookie cookie = new Cookie("cookie", uuid);
            cookie.setMaxAge(60 * 60 * 24 * 7);
            cookie.setPath("/");
            resp.addCookie(cookie);
            //存入redis
            jedisPool.getResource().setex(uuid, 60 * 60 * 24 * 7, name);

            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } else {
            return new Result(false, "登录失败");
        }
    }

    /**
     * 修改会员用户信息
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Member member){
        try {
            memberService.edit(member);
            return new Result(true,MessageConstant.EDIT_MEMBER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_MEMBER_FAIL);
        }
    }
}
