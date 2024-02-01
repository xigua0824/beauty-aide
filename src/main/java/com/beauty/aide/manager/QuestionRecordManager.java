package com.beauty.aide.manager;

import com.beauty.aide.common.model.entity.QuestionRecordDO;
import com.beauty.aide.common.model.vo.QuestionRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author xiaoliu
 */
@Component
public class QuestionRecordManager {

    public QuestionRecordVO do2Vo(QuestionRecordDO recordDO) {
        QuestionRecordVO recordVO = new QuestionRecordVO();
        BeanUtils.copyProperties(recordDO, recordVO);
        return recordVO;
    }

}
