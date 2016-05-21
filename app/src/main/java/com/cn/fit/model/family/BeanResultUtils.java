package com.cn.fit.model.family;

import org.json.JSONException;
import org.json.JSONObject;

public class BeanResultUtils {
    public static String getPropertyFromResult(BeanFamilyResult re, String property) {
        if (re == null || re.getData() == null) {
            return null;
        }
        String data = re.getData().toString();
        try {
            JSONObject json = new JSONObject(data);
            String success = json.getString(property);
            return success;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BeanFamilyResult parseResult(String s) {
        BeanFamilyResult re = new BeanFamilyResult();
        try {
            JSONObject js;
            js = new JSONObject(s);
            re.setValid(js.getInt("valid"));
            re.setStatusCode(js.getInt("statusCode"));
            re.setDataType(js.getInt("dataType"));
            re.setData(js.getString("data"));
            return re;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isResultSuccess(BeanFamilyResult re) {
        String data = getPropertyFromResult(re, "success");
        return "true".equals(data);
    }
}
