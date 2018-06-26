package com.githang.androidcrash.reporter.httpreporter;

import android.content.Context;
import android.util.Log;

import com.githang.androidcrash.reporter.AbstractCrashHandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * HTTP的post请求方式发送。
 */
public class CrashHttpReporter extends AbstractCrashHandler {
    private String url;
    private Map<String, String> otherParams;
    private String titleParam;
    private String bodyParam;
    private String fileParam;
    private String to;
    private String toParam;
    private HttpReportCallback callback;

    public CrashHttpReporter(Context context) {
        super(context);
//        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
//                HttpVersion.HTTP_1_1);
    }

    @Override
    protected void sendReport(String title, String body, File file) {
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 设置Http请求头信息；（Accept、Connection、Accept-Encoding、Cache-Control、Content-Type、User-Agent），不重要的就不解释了，直接参考抓包的结果设置即可
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Accept", "*/*");
            httpURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
            // 这个参数可以参考浏览器中抓出来的内容写，用chrome或者Fiddler抓吧看看就行
            httpURLConnection.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30)");
            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
            httpURLConnection.connect();
            SimpleMultipartEntity entity = new SimpleMultipartEntity(httpURLConnection);
            // 这个比较重要，按照上面分析的拼装出Content-Type头的内容
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data; boundary=" + entity.generateBoundary());
            entity.addPart(titleParam, title);
            entity.addPart(bodyParam, body);
            entity.addPart(toParam, to);
            if (otherParams != null) {
                for (Map.Entry<String, String> param : otherParams.entrySet()) {
                    entity.addPart(param.getKey(), param.getValue());
                }
            }
            entity.addPart(fileParam, file, true);
            int statusCode = httpURLConnection.getResponseCode();
            byte[] buffer = new byte[8 * 1024];
            int c = 0;
            // 调用HttpURLConnection对象的getResponseCode()获取客户端与服务器端的连接状态码。如果是200，则执行以下操作，否则返回null；
            if (httpURLConnection.getResponseCode() == 200) {
                bis = new BufferedInputStream(httpURLConnection.getInputStream());
                while ((c = bis.read(buffer)) != -1) {
                    baos.write(buffer, 0, c);
                    baos.flush();
                }
            }
            // 将输入流转成字节数组，返回给客户端。
            String responseString = new String(baos.toByteArray(), "utf-8");
            if (callback != null) {
                if (callback.isSuccess(statusCode, responseString)) {
                    deleteLog(file);
                }
            } else if (statusCode == 200) {
                deleteLog(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (baos != null)
                    baos.close();
                httpURLConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void deleteLog(File file) {
        Log.d("CrashHttpReporter", "delete: " + file.getName());
        file.delete();
    }

    public String getUrl() {
        return url;
    }

    /**
     * 发送请求的地址。
     *
     * @param url
     */
    public CrashHttpReporter setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTitleParam() {
        return titleParam;
    }

    /**
     * 标题的参数名
     *
     * @param titleParam
     */
    public CrashHttpReporter setTitleParam(String titleParam) {
        this.titleParam = titleParam;
        return this;
    }

    public String getBodyParam() {
        return bodyParam;
    }

    /**
     * 内容的参数名
     *
     * @param bodyParam
     */
    public CrashHttpReporter setBodyParam(String bodyParam) {
        this.bodyParam = bodyParam;
        return this;
    }

    public String getFileParam() {
        return fileParam;
    }

    /**
     * 文件的参数名
     *
     * @param fileParam
     */
    public CrashHttpReporter setFileParam(String fileParam) {
        this.fileParam = fileParam;
        return this;
    }

    public Map<String, String> getOtherParams() {
        return otherParams;
    }

    /**
     * 其他自定义的参数对（可不设置）。
     *
     * @param otherParams
     */
    public void setOtherParams(Map<String, String> otherParams) {
        this.otherParams = otherParams;
    }

    public String getTo() {
        return to;
    }

    /**
     * 收件人
     *
     * @param to
     */
    public CrashHttpReporter setTo(String to) {
        this.to = to;
        return this;
    }

    public HttpReportCallback getCallback() {
        return callback;
    }

    /**
     * 设置发送请求之后的回调接口。
     *
     * @param callback
     */
    public CrashHttpReporter setCallback(HttpReportCallback callback) {
        this.callback = callback;
        return this;
    }

    public String getToParam() {
        return toParam;
    }

    /**
     * 收件人参数名。
     *
     * @param toParam
     */
    public CrashHttpReporter setToParam(String toParam) {
        this.toParam = toParam;
        return this;
    }

    /**
     * 发送请求之后的回调接口。
     */
    public interface HttpReportCallback {
        /**
         * 判断是否发送成功。它在发送日志的方法中被调用，如果成功，则日志文件会被删除。
         *
         * @param status  状态码
         * @param content 返回的内容。
         * @return
         */
        public boolean isSuccess(int status, String content);
    }
}
