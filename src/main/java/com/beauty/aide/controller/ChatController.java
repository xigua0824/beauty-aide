package com.beauty.aide.controller;

import com.beauty.aide.adaptor.TongYiAdaptor;
import com.beauty.aide.common.errors.UserErrorCode;
import com.beauty.aide.common.model.vo.AccountVO;
import com.beauty.aide.common.result.ResultDO;
import com.beauty.aide.manager.AccountManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author xiaoliu
 */
@RestController
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    @Resource
    private HttpServletRequest request;
    @Resource
    private AccountManager accountManager;
    @Resource
    private TongYiAdaptor tongYiAdaptor;
//    @Resource
//    private ChatRecordDAO chatRecordDAO;

    // @TODO 创建一个新会话

    // @TODO 发送问题
    @PostMapping("/sendMessage")
    public ResultDO<String> sendMessage(String question,String uuid) {
        AccountVO user = accountManager.getLoginUser(request);
        if (user == null) {
            return ResultDO.errorOf(UserErrorCode.USER_NOT_LOGIN);
        }

        String messgae = tongYiAdaptor.sendChatRequest(question, uuid);
        // @TODO 存储问题数据库

        return ResultDO.succOf(messgae);
    }

    // @TODO 查看我的问题

    // @TODO 清除问题记录

}
