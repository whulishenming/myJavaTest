package lsm.util;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lishenming
 * @version 1.0
 * @date 2019-01-07 14:19
 **/
@Data
public class WxTemplateMessage {

    @JSONField(name = "touser")
    private String toUser;
    @JSONField(name = "template_id")
    private String templateId;
    private String page;
    @JSONField(name = "form_id")
    private String formId;
    private Map<String, KeywordBean> data;
    @JSONField(name = "emphasis_keyword")
    private String emphasisKeyword;

    @Setter
    @Getter
    @AllArgsConstructor
    private static class KeywordBean {
        private String value;
    }

    @Test
    public void test() {
        WxTemplateMessage wxTemplateMessage = new WxTemplateMessage();
        wxTemplateMessage.setFormId("FORMID");
        wxTemplateMessage.setEmphasisKeyword("keyword1.DATA");
        wxTemplateMessage.setPage("index");
        wxTemplateMessage.setTemplateId("TEMPLATE_ID");
        wxTemplateMessage.setToUser("OPENID");

        HashMap<String, KeywordBean> keywordMap = new HashMap<>(4);
        keywordMap.put("keyword1", new KeywordBean("339208499"));
        keywordMap.put("keyword2", new KeywordBean("2015年01月05日 12:30"));
        keywordMap.put("keyword3", new KeywordBean("腾讯微信总部"));
        keywordMap.put("keyword4", new KeywordBean("广州市海珠区新港中路397号"));
        wxTemplateMessage.setData(keywordMap);

        System.out.println(JSONObject.toJSONString(wxTemplateMessage));
    }
}
