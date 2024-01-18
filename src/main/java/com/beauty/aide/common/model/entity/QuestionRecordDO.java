package com.beauty.aide.common.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xiaoliu
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("question_record")
public class QuestionRecordDO extends BaseDO {

    private String sessionId;
    private String question;
    private String answer;
    private Long createUserId;

}
