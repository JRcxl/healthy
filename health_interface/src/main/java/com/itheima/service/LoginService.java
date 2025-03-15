package com.itheima.service;

import com.itheima.pojo.Member;

public interface LoginService {

    Member findByTelephone(String telephone);

    Member loginPassword(String telephone);


    Member loginEmail(String telephone);
}
