package lsm.addressToLocation;

import com.alibaba.fastjson.JSONObject;
import lsm.util.HttpUtils;
import lsm.util.ReadExcelUtil;
import lsm.util.WriteExcelUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shenmingli on 2018/4/23.
 */
public class Client {

    @Test
    public void translateAddressToLocation() throws Exception{
        List<Map<String, String>> mapList = ReadExcelUtil.readExcel("d:\\Users\\shenmingli\\Desktop\\公司.xlsx");
        Map<String, String> locationMap = new HashMap<>(10);
        for (Map<String, String> stringStringMap : mapList) {
            for (Map.Entry<String, String> stringStringEntry : stringStringMap.entrySet()) {
                String response = HttpUtils.sendGet("http://api.map.baidu.com/geocoder/v2/", "address=" + stringStringEntry.getValue() + "&output=json&ak=kR4pItRapPGRhlU1q1N38FSBB5GnP0om");
                BaiduResult baiduResult = JSONObject.parseObject(response, BaiduResult.class);

                if ("0".equals(baiduResult.getStatus())) {
                    locationMap.put(stringStringEntry.getValue(), baiduResult.getResult().getLocation().toString());
                }
            }

        }
        WriteExcelUtils.writeExcel(locationMap, "d:\\Users\\shenmingli\\Desktop\\公司经纬度.xlsx", "经纬度");
    }
}
