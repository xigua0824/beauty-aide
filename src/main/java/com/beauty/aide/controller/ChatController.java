package com.beauty.aide.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.beauty.aide.adaptor.TongYiAdaptor;
import com.beauty.aide.common.enums.CommonStatusEnum;
import com.beauty.aide.common.model.entity.AccountDO;
import com.beauty.aide.common.model.entity.QuestionRecordDO;
import com.beauty.aide.common.model.vo.AccountVO;
import com.beauty.aide.common.model.vo.QuestionRecordVO;
import com.beauty.aide.common.result.ResultDO;
import com.beauty.aide.manager.AccountManager;
import com.beauty.aide.manager.QuestionRecordManager;
import com.beauty.aide.mapper.AccountDAO;
import com.beauty.aide.mapper.QuestionRecordDAO;
import com.beauty.aide.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

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
    private QuestionRecordManager questionRecordManager;
    @Resource
    private TongYiAdaptor tongYiAdaptor;
    @Resource
    private QuestionRecordDAO questionRecordDAO;
    @Resource
    AccountDAO accountDAO;

    /**
     * 发送提问
     *
     * @param question 问题
     * @param uuid     会话唯一标识
     * @return 答案
     */
    @PostMapping("/sendMessage")
    public ResultDO<String> sendMessage(String question, String uuid) {
//        AccountVO user = accountManager.getLoginUser(request);
        AccountDO user = accountDAO.getByAccount("admin2");

        String message = tongYiAdaptor.sendChatRequest(question, uuid);
        // 存储问题数据库
        QuestionRecordDO questionRecord = new QuestionRecordDO();
        questionRecord.setQuestion(question);
        questionRecord.setAnswer(message);
        questionRecord.setSessionId(uuid);
        questionRecord.setCreateUserId(user.getId());
        questionRecord.setStatus(CommonStatusEnum.ACTIVE.name());
        questionRecordDAO.insert(questionRecord);
        return ResultDO.succOf(message);
    }

    /**
     * 查看会话列表
     *
     * @return
     */
    @GetMapping("/getSessionList")
    public ResultDO<List<QuestionRecordVO>> getSessionList() {
        AccountVO user = accountManager.getLoginUser(request);
        QueryWrapper<QuestionRecordDO> wrapper = new QueryWrapper<>();
        wrapper.eq("status", CommonStatusEnum.ACTIVE.name())
                .eq("create_user_id", user.getId())
                .orderByAsc("create_time");

        List<QuestionRecordDO> questionRecordDOS = questionRecordDAO.selectList(wrapper);

        // MySQL 5.7+默认开启ONLY_FULL_GROUP_BY, 无法正确使用GROUP BY, 故使用Java去重。
        List<QuestionRecordDO> recordDOList = questionRecordDOS.stream()
                .filter(CommonUtil.distinctByKey(recordDO -> recordDO.getSessionId()))
                .collect(Collectors.toList());

        List<QuestionRecordVO> vos = recordDOList.stream()
                .map(recordDO -> questionRecordManager.do2Vo(recordDO))
                .collect(Collectors.toList());

        return ResultDO.succOf(vos);
    }

    /**
     * 查看会话消息列表
     *
     * @param sessionId
     * @return
     */
    @GetMapping("/getMessageList")
    public ResultDO<List<QuestionRecordVO>> getMessageList(String sessionId) {
        QueryWrapper<QuestionRecordDO> wrapper = new QueryWrapper<>();
        wrapper.eq("status", CommonStatusEnum.ACTIVE.name())
                .eq("session_id", sessionId)
                .orderByAsc("create_time");

        List<QuestionRecordDO> recordDOList = questionRecordDAO.selectList(wrapper);

        List<QuestionRecordVO> vos = recordDOList.stream()
                .map(recordDO -> questionRecordManager.do2Vo(recordDO))
                .collect(Collectors.toList());

        return ResultDO.succOf(vos);
    }

    /**
     * 清除会话
     *
     * @param sessionId 会话ID
     * @return
     */
    @PostMapping("/delSession")
    public ResultDO<Void> delSession(String sessionId) {
        QuestionRecordDO entity = new QuestionRecordDO();
        entity.setStatus(CommonStatusEnum.DISABLED.name());
        QueryWrapper<QuestionRecordDO> updateWrapper = new QueryWrapper<>();
        updateWrapper.eq("session_id", sessionId);
        questionRecordDAO.update(entity, updateWrapper);
        return ResultDO.succOf();
    }

}
