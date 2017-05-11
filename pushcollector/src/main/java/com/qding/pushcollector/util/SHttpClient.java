package com.qding.pushcollector.util;

import android.os.Build;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

/**
 *
 */
public class SHttpClient {

    private static final String BOUNDARY = "3i2ndDfv2rTHiSisAbouNdArYfORhtTPEefj3q2f";
    private final int DEFAULT_TIMEOUT = 20000;
    private int timeout = DEFAULT_TIMEOUT;

    public SHttpClient(int timeout) {
        this.timeout = timeout;
    }

    public SHttpClient() {

    }

    private static String encodePostBody(
            Collection<TextParam> parameters, String boundary) {
        if (parameters == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (TextParam parameter : parameters) {
            sb.append("Content-Disposition: form-data; name=\"");
            sb.append(parameter.key);
            sb.append("\"\r\n\r\n");
            sb.append(parameter.value);
            sb.append("\r\n--");
            sb.append(boundary);
            sb.append("\r\n");
        }
        return sb.toString();
    }

    private static HttpURLConnection getConnection(String url) throws IOException {
        java.net.Proxy p = ProxyUtil.getProxy();

        return (HttpURLConnection) (p == null ? new URL(url)
                .openConnection() : new URL(url).openConnection(p));
    }

    public static TextParam text(String key, String value) {
        TextParam param = new TextParam();
        param.key = key;
        param.value = value;
        return param;
    }

    public static FileParam file(String key, File file, String fileType) {
        final String filename = (file != null && file.exists()) ? file.getName() : "";
        return file(key, file, filename, fileType);
    }

    public static FileParam file(String key, File file, String filename, String fileType) {
        FileParam param = new FileParam();
        param.key = key;
        param.file = file;
        param.fileType = fileType;
        param.filename = filename;
        return param;
    }

    private void fixBug(HttpURLConnection conn) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
            conn.setRequestProperty("Connection", "close");
        }
    }

    public HttpURLConnection post(String url, Collection<TextParam> params, Collection<FileParam>
            files) throws IOException {

        StringBuilder log = new StringBuilder();
        for (TextParam parameter : params) {
            log.append(parameter.key);
            log.append("=");
            log.append(parameter.value);
            log.append(";");
        }
            Log.e("DDAI", "Request = " + url);
            Log.e("DDAI", "Params = " + log);
        final String endLine = "\r\n";
        OutputStream os;
        HttpURLConnection conn = getConnection(url);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="
                + BOUNDARY);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        fixBug(conn);
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
//        String cookieString = CookiePool.INSTANCE.getCoookieString();
//        if (!TextUtils.isEmpty(cookieString)) {
//            conn.addRequestProperty("Cookie", cookieString);
//        }
        // conn.setRequestProperty("Cache-Control", "no-cache");
        conn.connect();
        os = new BufferedOutputStream(conn.getOutputStream());
        os.write(("--" + BOUNDARY + endLine).getBytes());
        String content = (encodePostBody(params, BOUNDARY));
        os.write(content.getBytes());
        // write file
        //		Set<String> keys = files.keySet();
        File file;
        for (FileParam fileParam : files) {
            file = fileParam.file;
            if (file != null && file.exists()) {
                os.write((endLine + "--" + BOUNDARY + endLine).getBytes());
                FileInputStream fis = null;
                try {
                    os.write(("Content-Disposition: form-data; name=\"" + fileParam.key
                            + "\"; filename=\"" + fileParam.filename + "\""
                            + endLine)
                            .getBytes());
                    os.write(("Content-Type: " + fileParam.fileType + endLine + endLine)
                            .getBytes());
                    fis = new FileInputStream(file);
                    byte[] buffer = new byte[1024 * 50];
                    int flag;
                    while ((flag = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, flag);
                    }
                    fis.close();
                } catch (IOException e) {
                    if (fis != null) {
                        fis.close();
                    }
                    throw e;
                }
            }
        }
        os.write((endLine + "--" + BOUNDARY + endLine).getBytes());
        os.flush();
        return conn;
    }

    public HttpURLConnection get(String urlString)
            throws IOException {
        HttpURLConnection conn = getConnection(urlString);
        fixBug(conn);
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        return conn;
    }

    public static class FileParam {
        public String key;
        private String filename;
        //		private String fileType = "image/jpeg";
        private String fileType = "application/octet-stream";
        private File file;
    }

    public static class TextParam {
        private String key;
        private String value;
    }

}
