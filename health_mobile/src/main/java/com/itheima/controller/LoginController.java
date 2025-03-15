package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.LoginService;
import com.itheima.service.MemberService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    private LoginService loginService;

    @Reference
    private MemberService memberService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${mail.smtp.username}")
    private String emailForm;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/loginByPhone")
    public Result loginByPhone(HttpServletResponse response,@RequestBody Map map) throws UnsupportedEncodingException {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //从Redis中拿到验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        //将用户输入的验证码和保存在Redis中的验证码进行比较
        if (validateCodeInRedis!=null&&validateCode!=null&&validateCode.equals(validateCodeInRedis)){
            //对比成功,完成登录,跳转到当前页面,存储用户信息
           Member member = loginService.findByTelephone(telephone);
           addClientCookie(response,member);
            return new Result(true,"登录成功",member);
        }else {
            //对比不成功,返回结果给页面
            return new Result(false,"登录失败");
        }
    }

    @RequestMapping("/loginPassword")
    public Result loginPassword( HttpServletResponse response,@RequestBody Map map) throws UnsupportedEncodingException {
        String telephone = (String) map.get("account");
        String validateCode = (String) map.get("validateCode");
        Member member = loginService.loginPassword(telephone);
        if (validateCode!=null&&member!=null&&validateCode.equals(member.getPassword())) { //member对象也要判断是否为空否则后面判定报错
            addClientCookie(response,member);
            return new Result(true,"登录成功");
        }else {
            return new Result(false,"您输入的密码有误");
        }

    }

    @RequestMapping("/loginEmail")
    public Result loginEmail( HttpServletResponse response,@RequestBody Map map) throws UnsupportedEncodingException {
        String email = (String) map.get("email");
        String validateCode = (String) map.get("validateCode");
        Member member = loginService.loginEmail(email);
        if (member!=null){
            //将qq邮箱验证码与会员的验证进行比较
            if ("qq邮箱验证码".equals(validateCode)){
                addClientCookie(response,member);
                return new Result(true,"登录成功");
            }else {
                return new Result(false,"您输入的验证码有误");
            }
        }else {
            return new Result(false,"您尚未注册");
        }
    }

    /*验证码发送*/
    @RequestMapping("/sendEmailCode")
    public Result senMsg(HttpSession httpSession, @RequestParam("email") String email) {
        //生成六位数验证码
        String Captcha = String.valueOf(new Random().nextInt(899999) + 100000);
        //存储验证码
        httpSession.setAttribute("Captcha", Captcha);

        SimpleMailMessage message = new SimpleMailMessage();
        //发件人的邮箱地址
        message.setFrom(emailForm);
        //收件人的邮箱地址
        message.setTo(email);
        //邮件主题
        message.setSubject("验证码");
        //邮件内容
        message.setText("验证码:" + Captcha);
        //发送邮件
        javaMailSender.send(message);
        return new Result(true,"发送邮件成功");
    }

    @RequestMapping("/isLogin")
    public Result isLogin(@NotNull HttpServletRequest request) throws UnsupportedEncodingException {
        boolean flag=false;
        String name =null ;
        Cookie[] cookies = request.getCookies();
        if (cookies==null){
            return new Result(false,"会员判断失败");
        }

        for (Cookie cookie : cookies) {
            if ("memberName".equals(cookie.getName())){
                URLDecoder.decode(String.valueOf(cookie),"UTF-8");
                name = cookie.getValue();
                flag=true;
            }
        }
        if(flag&&name!=null)
          return new Result(flag,"会员判断成功",name);
        else
            return new Result(flag,"会员判断失败",name);
    }

    @RequestMapping("/registerByPhone")
    public Result registerByPhone(@RequestBody Member member,String validateCode){
        //从Redis中拿到验证码
        String validateCodeInRedis = jedisPool.getResource().get(member.getPhoneNumber() + RedisMessageConstant.SENDTYPE_LOGIN);
        //将用户输入的验证码和保存在Redis中的验证码进行比较
        if (validateCodeInRedis!=null&&validateCode!=null&&validateCode.equals(validateCodeInRedis)){
            memberService.add(member);
            return new Result(true,"注册成功");
        }else {
            return new Result(false,"验证码输入有误");
        }

    }

    @RequestMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map map){
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //从Redis中拿到验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //将用户输入的验证码和保存在Redis中的验证码进行比较
        if (validateCodeInRedis!=null&&validateCode!=null&&validateCode.equals(validateCodeInRedis)){
            //对比成功,完成登录,跳转到当前页面,存储用户信息
            return new Result(true,"密码修改验证成功");
        }else {
            //对比不成功,返回结果给页面
            return new Result(false,"密码修改验证失败");
        }
    }

    public void addClientCookie(HttpServletResponse response,Member member) throws UnsupportedEncodingException {
        //进行转码
        String memberName = member.getName();
        String memberId= String.valueOf(member.getId());
        String encodeid=URLEncoder.encode(memberId, "UTF-8");
        String encode = URLEncoder.encode(memberName, "UTF-8");
        //登录成功把用户名和性别写入客户端
        Cookie cookie = new Cookie("memberName",encode);
        Cookie idcookie=new Cookie("memberId",encodeid);
        cookie.setPath("/");  // 设置 cookie 的作用路径为根路径
        idcookie.setPath("/");
        //设置最大存活时间
        cookie.setMaxAge(-1);
        //将cookie对象添加到客户端
        response.addCookie(idcookie);
        response.addCookie(cookie);
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:80");  // 允许的前端域
        response.setHeader("Access-Control-Allow-Credentials", "true");

    }
}
