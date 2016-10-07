package com.groomer.volley;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.groomer.GroomerApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class CustomJsonRequest extends Request<JSONObject> {

    private Listener<JSONObject> listener;
    private Map<String, String> params;
    private String cacheKey;

    public CustomJsonRequest(String url, Map<String, String> params,
                             Listener<JSONObject> reponseListener,
                             ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        cacheKey = params.get("action");
        this.listener = reponseListener;
        this.params = params;
    }

    public CustomJsonRequest(int method, String url, Map<String, String> params,
                             Listener<JSONObject> reponseListener,
                             ErrorListener errorListener) {
        super(method, url, errorListener);
        cacheKey = params.get("action");
        this.listener = reponseListener;
        this.params = params;
    }


    @Override
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    }


    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            //JSONObject jsonObject = new JSONObject(jsonString);


            // force response to be cached
            //Map<String, String> headers = response.headers;
            //long cacheExpiration = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
            //long now = System.currentTimeMillis();
            //Cache.Entry entry = new Cache.Entry();
            //entry.data = response.data;
            //entry.etag = headers.get("ETag");
            //entry.ttl = now + cacheExpiration;
            //entry.serverDate = HttpHeaderParser.parseDateAsEpoch(headers.get("Date"));
            //entry.responseHeaders = headers;
            //entry = HttpHeaderParser.parseCacheHeaders(response);
            //GroomerApplication.getInstance().getRequestQueue().getCache().put(cacheKey, entry);
            Cache.Entry entry = HttpHeaderParser.parseCacheHeaders(response);
            GroomerApplication.getInstance().getRequestQueue().getCache().put(cacheKey, entry);
            return Response.success(new JSONObject(jsonString),
                    entry);
            //return Response.success(jsonObject, entry);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            je.printStackTrace();
            return Response.error(new ParseError(je));
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {

        listener.onResponse(response);
    }


}