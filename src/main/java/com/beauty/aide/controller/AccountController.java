package com.beauty.aide.controller;

import com.beauty.aide.common.enums.AccountStatusEnum;
import com.beauty.aide.common.enums.UserRoleEnum;
import com.beauty.aide.common.errors.CommonErrorCode;
import com.beauty.aide.common.errors.UserErrorCode;
import com.beauty.aide.common.result.ResultDO;
import com.beauty.aide.constant.UserConstant;
import com.beauty.aide.manager.AccountManager;
import com.beauty.aide.mapper.AccountDAO;
import com.beauty.aide.common.model.entity.AccountDO;
import com.beauty.aide.common.model.vo.AccountVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author xiaoliu
 */
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {

    private static final String DEFAULT_HEADIMG = "https://img.alicdn.com/imgextra/i4/O1CN01zJMPkf1vvDtzGGQKW_!!6000000006234-2-tps-144-144.png";

    @Resource
    private HttpServletRequest request;

    @Resource
    private AccountDAO accountDAO;

    @Resource
    private AccountManager accountManager;

    /**
     * 注册账户
     * @return
     */
    @PostMapping("/register")
    public ResultDO<Long> register() {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String nickName = request.getParameter("nickName");

        if (StringUtils.isAnyBlank(account,password,nickName)) {
            return ResultDO.errorOf(CommonErrorCode.PARAM_EMPTY);
        }

        AccountDO accountDO = new AccountDO();
        accountDO.setAccount(account);
        accountDO.setHeadImg(DEFAULT_HEADIMG);
        accountDO.setNickName(nickName);
        accountDO.setPassword(password);
        accountDO.setRoleId(UserRoleEnum.COMMON_USER.getCode());
        accountDO.setStatus(AccountStatusEnum.ACTIVE.name());

        // 检查重复账号
        if (!accountManager.checkRepeatAccount(account)) {
            return ResultDO.errorOf(UserErrorCode.ACCOUNT_EXISTS);
        }

        // 检查账户参数
        if (!accountManager.checkAccountParam(accountDO)) {
            return ResultDO.errorOf(CommonErrorCode.PARAM_INVALID.getCode(), "请检查注册参数");
        }

        // 加密密码
        String encryptPassword = accountManager.encryptPassword(accountDO.getPassword());
        accountDO.setPassword(encryptPassword);
        accountDAO.insert(accountDO);

        return ResultDO.succOf(accountDO.getId());
    }

    /**
     * 登录账户
     * @return
     */
    @PostMapping("/login")
    public ResultDO<Void> login() {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        if (StringUtils.isAnyBlank(account, password)) {
            return ResultDO.errorOf(CommonErrorCode.PARAM_EMPTY);
        }
        AccountDO accountDO = accountDAO.getByAccount(account);
        if (accountDO == null) {
            return ResultDO.errorOf(UserErrorCode.USER_NOT_EXIST);
        }
        if (AccountStatusEnum.DISABLED.name().equals(accountDO.getStatus())) {
            return ResultDO.errorOf(UserErrorCode.ACCOUNT_SUSPENDED);
        }
        String encryptPassword = accountManager.encryptPassword(password);
        if (!Objects.equals(accountDO.getPassword(), encryptPassword)) {
            return ResultDO.errorOf(UserErrorCode.PASSWORD_ERROR);
        }
        AccountVO vo = accountManager.do2Vo(accountDO);
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, vo);
        return ResultDO.succOf();
    }

    /**
     * 查看个人信息
     * @return
     */
    @GetMapping("/getInfo")
    public ResultDO<AccountVO> getInfo() {
        AccountVO accountVO = accountManager.getLoginUser(request);
        if (accountVO == null) {
            return ResultDO.errorOf(UserErrorCode.USER_NOT_EXIST);
        }
        return ResultDO.succOf(accountVO);

//        Long accountId = Long.valueOf(request.getParameter("accountId"));
//        assert accountId != null;
//        AccountDO account = accountDAO.getById(accountId);
//        if (account == null) {
//            return ResultDO.errorOf(UserErrorCode.USER_NOT_EXIST);
//        }
//
//        account.setPassword(null);
//        return ResultDO.succOf(account);
    }

    /**
     * 更改个人信息
     * @return
     */
    @PostMapping("/update")
    public ResultDO<Void> update() {
        AccountVO accountVO = accountManager.getLoginUser(request);
        String nickName = request.getParameter("nickName");
        String headImg = request.getParameter("headImg");
        if (accountVO == null) {
            return ResultDO.errorOf(UserErrorCode.USER_NOT_EXIST);
        }
        if (StringUtils.isAnyBlank(nickName, headImg)) {
            return ResultDO.errorOf(CommonErrorCode.PARAM_EMPTY);
        }
        AccountDO accountDO = accountManager.vo2Do(accountVO);
        if (StringUtils.isNotBlank(nickName)) {
            accountDO.setNickName(nickName);
        }
        if (StringUtils.isNotBlank(headImg)) {
            accountDO.setHeadImg(headImg);
        }
        accountDAO.update(accountDO);
        // 更新session信息
        AccountVO vo = accountManager.do2Vo(accountDO);
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, vo);
        return ResultDO.succOf();
    }

    /**
     * 更改用户密码
     * @return
     */
    @PostMapping("/updatePassword")
    public ResultDO<Void> updatePassword() {
        AccountVO accountVO = accountManager.getLoginUser(request);
        String password = request.getParameter("password");
        String newPassword = request.getParameter("newPassword");
        if (accountVO == null) {
            return ResultDO.errorOf(UserErrorCode.USER_NOT_EXIST);
        }
        if (StringUtils.isAnyBlank(password,newPassword)) {
            return ResultDO.errorOf(CommonErrorCode.PARAM_EMPTY);
        }
        AccountDO accountDO = accountManager.vo2Do(accountVO);
        String encryptPassword = accountManager.encryptPassword(password);
        accountDO.setPassword(encryptPassword);
        accountDAO.update(accountDO);
        return ResultDO.succOf();
    }

    /**
     * 登出账户
     * @return
     */
    @PostMapping("/logout")
    public ResultDO<Void> logout() {
        if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null) {
            return ResultDO.errorOf(UserErrorCode.USER_NOT_LOGIN);
        }

        // 移除登录状态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return ResultDO.succOf();
    }

    /**
     * 注销
     * @return
     */
    @PostMapping("/disable")
    public ResultDO<Void> disable() {
        AccountVO accountVO = accountManager.getLoginUser(request);
        if (accountVO == null) {
            return ResultDO.errorOf(UserErrorCode.USER_NOT_EXIST);
        }
        // 移除登录状态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        accountDAO.updateStatusById(accountVO.getId(),AccountStatusEnum.DISABLED.name());
        return ResultDO.succOf();
    }

}
