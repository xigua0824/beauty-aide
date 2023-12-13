package com.beauty.aide.mapper;

import com.beauty.aide.common.so.AccountSO;
import com.beauty.aide.common.so.UserSO;
import com.beauty.aide.model.entity.AccountDO;
import org.apache.ibatis.annotations.Param;
import org.checkerframework.checker.units.qual.A;

import java.util.List;


public interface AccountDAO {

    void insert(AccountDO accountDO);

    int update(AccountDO accountDO);

    AccountDO getById(Long id);

    AccountDO getByAccount(String account);

    int updateStatusById(Long id, String status);

    int updateName(@Param("id") Long id, @Param("nickName") String nickName);

    List<AccountDO> search(@Param("so") UserSO search);

    int searchCount(@Param("so") AccountSO search);

    AccountDO getInfo(String accout, String password);
}




