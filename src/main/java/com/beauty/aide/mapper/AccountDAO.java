package com.beauty.aide.mapper;

import com.beauty.aide.common.model.so.AccountSO;
import com.beauty.aide.common.model.entity.AccountDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author xiaoliu
 */
public interface AccountDAO {

    void insert(AccountDO accountDO);

    int update(AccountDO accountDO);

    AccountDO getById(Long id);

    AccountDO getByAccount(String account);

    int updateStatusById(Long id, String status);

    int updateName(@Param("id") Long id, @Param("nickName") String nickName);

    List<AccountDO> search(@Param("so") AccountSO search);

    int searchCount(@Param("so") AccountSO search);

    AccountDO getInfo(String accout, String password);
}




