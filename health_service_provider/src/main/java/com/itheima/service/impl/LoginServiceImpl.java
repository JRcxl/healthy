package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.LoginDao;
import com.itheima.pojo.Member;
import com.itheima.service.LoginService;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
@Service(interfaceClass = LoginService.class)
@Transactional
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginDao loginDao;

    @Autowired
    private MemberService memberService;

    @Override
    public Member findByTelephone(String telephone) {
        return  memberService.findByTelephone(telephone);
    }

    @Override
    public Member loginPassword(String account) {
        Member member = findByTelephone(account);
       return member;
    }

    @Override
    public Member loginEmail(String email) {
        Member member = memberService.findByEmail(email);
       return member;
    }


}
