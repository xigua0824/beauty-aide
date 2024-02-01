package com.beauty.aide.manager;

import com.beauty.aide.common.enums.AccountStatusEnum;
import com.beauty.aide.common.enums.UserRoleEnum;
import com.beauty.aide.common.errors.ErrorCode;
import com.beauty.aide.common.model.vo.AccountVO;
import com.beauty.aide.constant.UserConstant;
import com.beauty.aide.exception.BusinessException;
import com.beauty.aide.mapper.AccountDAO;
import com.beauty.aide.common.model.entity.AccountDO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xiaoliu
 */
@Component
public class AccountManager {

    private static final String SALT = "550e8400e29b41d4a716446655440000";

    @Resource
    private AccountDAO accountDAO;

    /**
     * 检查重复账户
     *
     * @param account
     * @return
     */
    public boolean checkRepeatAccount(String account) {
        AccountDO accountDO = accountDAO.getByAccount(account);
        if (accountDO != null) {
            return false;
        }
        return true;
    }

    /**
     * 检查账户参数
     *
     * @param accountDO
     * @return
     */
    public boolean checkAccountParam(AccountDO accountDO) {
        if (accountDO.getAccount().length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (accountDO.getPassword().length() < 5) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        return true;
    }

    /**
     * 加密密码
     *
     * @param password
     * @return
     */
    public String encryptPassword(String password) {
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes());
    }

    public AccountVO do2Vo(AccountDO accountDO) {
        AccountVO vo = new AccountVO();
        BeanUtils.copyProperties(accountDO, vo);
        vo.setStatusDesc(AccountStatusEnum.getByName(accountDO.getStatus()));
        vo.setRoleName(UserRoleEnum.getByCode(accountDO.getRoleId()));
        return vo;
    }

    public AccountDO vo2Do(AccountVO accountVO) {
        AccountDO accountDO = new AccountDO();
        BeanUtils.copyProperties(accountVO, accountDO);
        return accountDO;
    }

    /**
     * 获取当前登录账户
     *
     * @param request
     * @return
     */
    public AccountVO getLoginUser(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        AccountVO accountVO = (AccountVO) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (accountVO == null) {
//            throw new BusinessException(UserErrorCode.USER_NOT_LOGIN.getMessage());
            return null;
        }
        return accountVO;
    }

    /**
     * 是否为管理员
     *
     * @return
     */
    public boolean isAdmin(Long roleId) {
        return roleId.equals(UserRoleEnum.SUPER_ADMIN.getCode()) || roleId.equals(UserRoleEnum.ADMIN.getCode());
    }

    /**
     * 是否为超级管理员
     * @param request
     * @return
     */
    public boolean isSuperAdmin(HttpServletRequest request) {
        AccountVO accountVO = (AccountVO) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (accountVO == null) {
            return false;
        }
        return accountVO.getRoleId().equals(UserRoleEnum.SUPER_ADMIN.getCode());
    }
}
