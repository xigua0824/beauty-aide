package com.beauty.aide.adaptor;

import com.aliyun.broadscope.bailian.sdk.AccessTokenClient;
import com.aliyun.broadscope.bailian.sdk.ApplicationClient;
import com.aliyun.broadscope.bailian.sdk.models.BaiLianConfig;
import com.aliyun.broadscope.bailian.sdk.models.CompletionsRequest;
import com.aliyun.broadscope.bailian.sdk.models.CompletionsResponse;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xiaoliu
 */
@Component
public class TongYiAdaptor implements InitializingBean {

    @Value("${aliyun.cloud.adminAk}")
    private String adminAk;
    @Value("${aliyun.cloud.adminSk}")
    private String adminSk;
    @Value("${aliyun.tongyi.agentKey}")
    private String agentKey;
    @Value("${aliyun.tongyi.appId}")
    private String appId;
    private ApplicationClient client;

    @Override
    public void afterPropertiesSet() {
        AccessTokenClient tokenClient = new AccessTokenClient(adminAk, adminSk, agentKey);
        String token = tokenClient.getToken();
        BaiLianConfig config = new BaiLianConfig().setApiKey(token);
        ApplicationClient client = new ApplicationClient(config);
        client = new ApplicationClient(config);
    }


    /**
     * 发送请求
     *
     * @param prompt
     * @return
     */
    public String sendChatRequest(String prompt) {
        CompletionsRequest request = new CompletionsRequest()
                .setAppId(appId)
                .setPrompt(prompt);
        CompletionsResponse response = client.completions(request);
        String content = response.getData().getText();
        content = content.substring(0, content.lastIndexOf("<sup>"));
        return content;
    }

}
