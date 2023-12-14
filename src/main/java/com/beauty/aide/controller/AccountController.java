package com.beauty.aide.controller;

import com.beauty.aide.common.errors.CommonErrorCode;
import com.beauty.aide.common.errors.UserErrorCode;
import com.beauty.aide.common.result.ResultDO;
import com.beauty.aide.constant.UserConstant;
import com.beauty.aide.manager.AccountManager;
import com.beauty.aide.mapper.AccountDAO;
import com.beauty.aide.model.entity.AccountDO;
import com.beauty.aide.model.vo.AccountVO;
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
        String headImg = request.getParameter("headImg");
        String nickName = request.getParameter("nickName");

        if (StringUtils.isAnyBlank(account,password,headImg,nickName)) {
            return ResultDO.errorOf(CommonErrorCode.PARAM_EMPTY);
        }

        AccountDO accountDO = new AccountDO();
        accountDO.setAccount(account);
        accountDO.setHeadImg(headImg);
        accountDO.setNickName(nickName);
        accountDO.setPassword(password);

        // 检查重复账号
        if (!accountManager.checkRepeatAccount(account)) {
            return ResultDO.errorOf(UserErrorCode.ACCOUNT_EXISTS);
        }

        // 检查账户参数
        if (!accountManager.checkAccountParam(accountDO)) {
            return ResultDO.errorOf(CommonErrorCode.PARAM_INVALID.getCode(), "请检查注册参数");
        }
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
        AccountVO accountVO = (AccountVO) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
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
        AccountVO accountVO = (AccountVO) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        String nickName = request.getParameter("nickName");
        String headImg = request.getParameter("headImg");
        if (accountVO == null) {
            return ResultDO.errorOf(UserErrorCode.USER_NOT_EXIST);
        }
        if (StringUtils.isAnyBlank(nickName, headImg)) {
            return ResultDO.errorOf(CommonErrorCode.PARAM_EMPTY);
        }
        AccountDO accountDO = accountManager.vo2Do(accountVO);
        accountDO.setNickName(nickName);
        accountDO.setHeadImg(headImg);
        accountDAO.update(accountDO);
        return ResultDO.succOf();
    }

    /**
     * 更改用户密码
     * @return
     */
    @PostMapping("/updatePassword")
    public ResultDO<Void> updatePassword() {
        AccountVO accountVO = (AccountVO) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
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

    // @TODO 注销账户

}
