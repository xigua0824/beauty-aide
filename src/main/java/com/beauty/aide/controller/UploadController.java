package com.beauty.aide.controller;

import com.beauty.aide.common.errors.CommonErrorCode;
import com.beauty.aide.common.result.ResultDO;
import com.beauty.aide.manager.AccountManager;
import com.beauty.aide.common.model.vo.AccountVO;
import com.beauty.aide.oss.SimpleOssService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author xiaoliu
 */
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private AccountManager accountManager;

    @Resource
    private SimpleOssService simpleOssService;

    private static final Set<String> IMAGE_EXT = new HashSet<>(Arrays.asList("jpg", "jpeg", "png"));


    @PostMapping("/image")
    public ResultDO<String> uploadImage(MultipartFile file) {
        AccountVO accountVO = accountManager.getLoginUser(request);
        if (file.isEmpty()) {
            return ResultDO.errorOf("文件不能为空");
        }
        // 获取文件后缀
        String fileExt = StringUtils.substringAfterLast(file.getOriginalFilename().toLowerCase(), ".");
        if (!IMAGE_EXT.contains(fileExt)) {
            return ResultDO.errorOf("文件类型有误, 只接受图片类型文件");
        }

        // 拼接osskey
        String ossKey = String.format("headImg/%s/%s.%s", accountVO.getId(),
                UUID.randomUUID().toString().replace("-", ""), fileExt);

        try {
            simpleOssService.putObject(ossKey, file.getBytes());
            return ResultDO.succOf(simpleOssService.generateUrl(ossKey));
        } catch (Exception e) {
            log.error("upload_error fileName:{}", file.getOriginalFilename(), e);
        }

        return ResultDO.errorOf(CommonErrorCode.SYS_ERROR);
    }

}
