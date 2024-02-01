package com.beauty.aide.common.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author xiaoliu
 */
@Data
public class QuestionRecordVO {

    private String sessionId;
    private String question;
    private String answer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
