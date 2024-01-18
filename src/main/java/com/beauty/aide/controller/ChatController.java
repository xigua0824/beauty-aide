package com.beauty.aide.controller;

import com.beauty.aide.adaptor.TongYiAdaptor;
import com.beauty.aide.common.errors.UserErrorCode;
import com.beauty.aide.common.model.entity.QuestionRecordDO;
import com.beauty.aide.common.model.vo.AccountVO;
import com.beauty.aide.common.result.ResultDO;
import com.beauty.aide.manager.AccountManager;
import com.beauty.aide.mapper.QuestionRecordDAO;
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
    @Resource
    private QuestionRecordDAO questionRecordDAO;

    // @TODO 创建一个新会话

    /**
     * 发送提问
     * @param question 问题
     * @param uuid 会话唯一标识
     * @return 答案
     */
    @PostMapping("/sendMessage")
    public ResultDO<String> sendMessage(String question, String uuid) {
        AccountVO user = accountManager.getLoginUser(request);
        if (user == null) {
            return ResultDO.errorOf(UserErrorCode.USER_NOT_LOGIN);
        }
        String message = tongYiAdaptor.sendChatRequest(question, uuid);
        // 存储问题数据库
        QuestionRecordDO questionRecord = new QuestionRecordDO();
        questionRecord.setQuestion(question);
        questionRecord.setAnswer(message);
        questionRecord.setSessionId(uuid);
        questionRecord.setCreateUserId(user.getId());
        questionRecordDAO.insert(questionRecord);
        return ResultDO.succOf(message);
    }

    // @TODO 查看我的问题

    // @TODO 清除问题记录

}
