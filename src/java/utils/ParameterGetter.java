package utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.servlet.http.HttpServletRequest;


public class ParameterGetter {
    public static String get(String parameter,HttpServletRequest req){
        return new Gson().fromJson(req.getParameter("dados"), JsonObject.class).get(parameter).getAsString();
    }
}
