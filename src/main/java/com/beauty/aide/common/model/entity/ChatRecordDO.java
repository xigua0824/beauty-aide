package com.beauty.aide.common.model.entity;

import lombok.Data;

/**
 * @author xiaoliu
 */
@Data
public class ChatRecordDO extends BaseDO {

    private String sessionId;
    private String content;
    private String answer;
    private Long userId;
    private String status;

}
