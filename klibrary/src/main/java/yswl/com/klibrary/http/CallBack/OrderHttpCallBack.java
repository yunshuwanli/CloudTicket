package yswl.com.klibrary.http.CallBack;

import org.json.JSONObject;

public interface OrderHttpCallBack {

    void onSucceed(int requestId, JSONObject result,Object o);

    void onFail(int requestId, String errorMsg,Object o);
}
