package com.beauty.aide.controller;


import com.beauty.aide.common.enums.AccountStatusEnum;
import com.beauty.aide.common.enums.UserRoleEnum;
import com.beauty.aide.common.errors.CommonErrorCode;
import com.beauty.aide.common.errors.UserErrorCode;
import com.beauty.aide.common.result.PageResultDO;
import com.beauty.aide.common.result.ResultDO;
import com.beauty.aide.manager.AccountManager;
import com.beauty.aide.mapper.AccountDAO;
import com.beauty.aide.common.model.entity.AccountDO;
import com.beauty.aide.common.model.so.AccountSO;
import com.beauty.aide.common.model.vo.AccountVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoliu
 */
@RestController
@RequestMapping("/admin/account")
@Slf4j
public class AdminAccountController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private AccountDAO accountDAO;

    @Resource
    private AccountManager accountManager;

    /**
     * 查看用户列表
     *
     * @param search
     * @return
     */
    @GetMapping("/getList")
    public PageResultDO<List<AccountVO>> getList(AccountSO search) {
        int count = accountDAO.searchCount(search);
        search.calSkip();

        if (count == 0) {
            return PageResultDO.succOf(Collections.emptyList(), search.getCurrentPage(), search.getPageSize(), count);
        }

        List<AccountDO> accountList = accountDAO.search(search);
        List<AccountVO> vos = accountList.stream().map(u -> accountManager.do2Vo(u)).collect(Collectors.toList());
        return PageResultDO.succOf(vos, search.getCurrentPage(), search.getPageSize(), count);
    }

    /**
     * 查看用户详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getInfo")
    public ResultDO<AccountVO> getInfo(Long id) {
        AccountDO accountDO = accountDAO.getById(id);
        if (accountDO == null) {
            return ResultDO.errorOf(UserErrorCode.USER_NOT_EXIST);
        }
        AccountVO vo = accountManager.do2Vo(accountDO);
        return ResultDO.succOf(vo);
    }

    /**
     * 用户赋权
     *
     * @param id
     * @return
     */
    @PostMapping("/updateRole")
    public ResultDO<Void> updateRole(Long id) {
        // 校验当前操作者权限
        if (!accountManager.isSuperAdmin(request)) {
            return ResultDO.errorOf(CommonErrorCode.NOT_PERMISSION.getCode(), "您没有权限操作,请联系超级管理员");
        }

        AccountDO accountDO = accountDAO.getById(id);
        if (accountDO == null) {
            return ResultDO.errorOf(UserErrorCode.USER_NOT_EXIST);
        }
        if (AccountStatusEnum.DISABLED.name().equals(accountDO.getStatus())) {
            return ResultDO.errorOf(UserErrorCode.ACCOUNT_SUSPENDED);
        }

        accountDO.setRoleId(UserRoleEnum.ADMIN.getCode());
        accountDAO.update(accountDO);
        return ResultDO.succOf();
    }

    /**
     * 禁用账户
     *
     * @return
     */
    @PostMapping("/disable")
    public ResultDO<Void> disable(Long id) {
        AccountDO accountDO = accountDAO.getById(id);
        if (accountDO == null) {
            return ResultDO.errorOf(UserErrorCode.USER_NOT_EXIST);
        }
        if (AccountStatusEnum.DISABLED.name().equals(accountDO.getStatus())) {
            return ResultDO.errorOf(UserErrorCode.ACCOUNT_SUSPENDED);
        }
        if (accountManager.isAdmin(accountDO.getRoleId())) {
            return ResultDO.errorOf(CommonErrorCode.NOT_PERMISSION.getCode(), "您没有权限操作,请联系超级管理员");
        }
        accountDO.setStatus(AccountStatusEnum.DISABLED.name());
        return ResultDO.succOf();
    }

    /**
     * 激活账号
     *
     * @return
     */
    @PostMapping("/active")
    public ResultDO<Void> active(Long id) {
        AccountDO accountDO = accountDAO.getById(id);
        if (accountDO == null) {
            return ResultDO.errorOf(UserErrorCode.USER_NOT_EXIST);
        }
        if (AccountStatusEnum.DISABLED.name().equals(accountDO.getStatus())) {
            return ResultDO.errorOf(UserErrorCode.ACCOUNT_SUSPENDED);
        }
        if (accountManager.isAdmin(accountDO.getRoleId())) {
            return ResultDO.errorOf(CommonErrorCode.NOT_PERMISSION.getCode(), "您没有权限操作,请联系超级管理员");
        }
        accountDO.setStatus(AccountStatusEnum.ACTIVE.name());
        return ResultDO.succOf();
    }


}
