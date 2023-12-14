package com.beauty.aide.oss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * @author xiaoliu
 */
@Slf4j
@Service
public class SimpleOssService implements InitializingBean {

    @Value("aliyun.oss.bucket")
    private String bucket;
    @Value("aliyun.oss.endpoint")
    private String endpoint;
    @Value("aliyun.cloud.accesskey")
    private String adminAk;
    @Value("aliyun.cloud.accesskey.secret")
    private String adminSk;
    private OSS oss;

    private OSS getOssClient() {
        return oss;
    }

    @Override
    public void afterPropertiesSet() {
        oss = new OSSClient(endpoint, adminAk, adminSk);
    }

    /**
     * 上传文件
     */
    public PutObjectResult putObject(String fileKey, InputStream is) {
        return this.getOssClient().putObject(bucket, fileKey, is);
    }

    /**
     * 生成URL
     */
    public String generateUrl(String fileKey) {
        return doGenerateUrl(fileKey, getExpireTime());
    }

    public String generateUrl(String bucket, String fileKey) {
        return doGenerateUrl(bucket, fileKey, getExpireTime());
    }

    public String generateUrl(String fileKey, int seconds) {
        return doGenerateUrl(fileKey, new Date(System.currentTimeMillis() + seconds * 1000));
    }

    private String doGenerateUrl(String fileKey, Date expireTime) {
        if (fileKey == null || fileKey.trim().isEmpty()) {
            return null;
        }
        String url = getOssClient().generatePresignedUrl(bucket, fileKey, expireTime).toString();
        if (StringUtils.startsWith(url, "http://")) {
            url = StringUtils.replaceOnce(url, "http://", "https://");
        }

        return url;
    }

    private String doGenerateUrl(String bucket, String fileKey, Date expireTime) {
        if (fileKey == null || fileKey.trim().isEmpty()) {
            return null;
        }
        String url = getOssClient().generatePresignedUrl(bucket, fileKey, expireTime).toString();
        if (StringUtils.startsWith(url, "http://")) {
            url = StringUtils.replaceOnce(url, "http://", "https://");
        }

        return url;
    }

    public PutObjectResult putObject(String fileKey, byte[] bytes) {
        return putObject(fileKey, new ByteArrayInputStream(bytes));
    }

    public PutObjectResult putObject(String fileKey, File file) {
        return this.getOssClient().putObject(bucket, fileKey, file);
    }


    /**
     * 文件url提取osskey
     *
     * @param fileUrl
     * @return
     * @throws MalformedURLException
     */
    public String getOssKeyFromUrl(String fileUrl) throws MalformedURLException {
        URL url = new URL(fileUrl);
        String ossKey = url.getPath();
        if (ossKey.startsWith("/")) {
            ossKey = ossKey.substring(1);
        }
        return ossKey;
    }

    /**
     * helper
     */
    private Date getExpireTime() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.YEAR, 10);
        return instance.getTime();
    }

}
