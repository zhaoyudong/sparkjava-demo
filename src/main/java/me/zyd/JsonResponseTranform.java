package me.zyd;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Created by zhaoyudong on 2018/1/31.
 */
public class JsonResponseTranform implements ResponseTransformer {

    private Gson gson = new Gson();


    public String render(Object o) throws Exception {
        return gson.toJson(o);
    }
}
