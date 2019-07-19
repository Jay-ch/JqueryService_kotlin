package com.jquery.service.android.logger;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Json日志
 * Created by J.query on 15/11/18.
 */
public class JsonLog {

    public static void printJson(String tag, String msg, String headString) {

        String message;

        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(LogUtil.JSON_INDENT);
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(LogUtil.JSON_INDENT);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }

        LogUtilHelper.printLine(tag, true);
        message = headString + LogUtil.LINE_SEPARATOR + message;
        String[] lines = message.split(LogUtil.LINE_SEPARATOR);
        for (String line : lines) {
            Log.d(tag, "║ " + line);
        }
        LogUtilHelper.printLine(tag, false);
    }
}
