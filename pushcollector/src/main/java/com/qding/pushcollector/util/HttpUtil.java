package com.qding.pushcollector.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ning.dai on 14-9-22.
 */
public class HttpUtil {

    public static HttpRequest get(String url) {
        return new HttpRequest(url);
    }

    public static HttpPostRequest post(String url) {
        return new HttpPostRequest(url);
    }

    private static void read(InputStream in, OutputStream out, Callback.ProgressCallback
            callback, long length)
            throws IOException {
        byte[] buffer = new byte[4 * 1024];
        int b = -1;
        if (length > 0 && callback != null) {
            int last = 0;
            long loaded = 0;
            int tmp;
            while ((b = in.read(buffer)) != -1) {
                out.write(buffer, 0, b);
                loaded += b;
                tmp = (int) ((loaded * 100) / length);
                if (last < tmp) {
                    last = tmp;
                    callback.onProgress(last);
                }
            }

        } else {
            while ((b = in.read(buffer)) != -1) {
                out.write(buffer, 0, b);
            }
        }

    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager
                .getActiveNetworkInfo();
        return mNetworkInfo != null && mNetworkInfo.isAvailable();
    }

    public static void download(final String url, final File file) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUtil.get(url).open().toFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void download(final String url, final File file,
                                final Callback.ProgressCallback listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUtil.get(url).open().setProgressListener(listener).toFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void download(final String url, final File file,
                                final Callback.DownloadCallback listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUtil.get(url).open().setDownLoadListener(listener).toFile(
                            file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static class HttpRequest {
        String url;
        int timeout;

        HttpRequest(String url) {
            this.url = url;
        }

        public Response open() throws IOException {
            SHttpClient client = new SHttpClient();
            HttpURLConnection connection = client.get(url);
            return new Response(connection);
            //Type loadType = ClassTool.getParameterizedType(callback.getClass(), Callback.class,
            // 0);
        }

    }

    public static class HttpPostRequest extends HttpRequest {
        ConcurrentHashMap<String, SHttpClient.TextParam> texts = new ConcurrentHashMap<String,
                SHttpClient.TextParam>();
        ConcurrentHashMap<String, SHttpClient.FileParam> files = new ConcurrentHashMap<String,
                SHttpClient.FileParam>();

        HttpPostRequest(String url) {
            super(url);
        }

        public HttpPostRequest setParam(String key, String value) {
            texts.putIfAbsent(key, SHttpClient.text(key, value));
            return this;
        }

        public HttpPostRequest setParam(Map<String, String> params) {
            Set<String> keys = params.keySet();
            for (String key : keys) {
                texts.putIfAbsent(key, SHttpClient.text(key, params.get(key)));
            }
            return this;
        }

        public HttpPostRequest setFileParam(String key, File file, String type) {
            files.putIfAbsent(key, SHttpClient.file(key, file, ""));
            return this;
        }

        public HttpPostRequest setFileParam(List<SHttpClient.FileParam> params) {
            for (SHttpClient.FileParam param : params) {
                files.putIfAbsent(param.key, param);
            }
            return this;
        }

        public HttpPostRequest setTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        @Override
        public Response open() throws IOException {
            SHttpClient client = (timeout > 1) ? new SHttpClient(timeout) : new SHttpClient();
            HttpURLConnection connection = client.post(url, texts.values(), files.values());
            return new Response(connection);

        }
    }

    public static class Response {
        HttpURLConnection connection;
        Callback.ProgressCallback progressCallback;
        Callback.DownloadCallback downloadCallback;
        Brake brake;

        protected Response(HttpURLConnection connection) {
            this.connection = connection;
        }

        protected Response(HttpURLConnection connection, Brake brake) {
            this.connection = connection;
            this.brake = brake;
        }

        public Response setProgressListener(Callback.ProgressCallback listener) {
            this.progressCallback = listener;
            return this;
        }

        public Response setDownLoadListener(Callback.DownloadCallback listener) {
            this.downloadCallback = listener;
            return this;
        }

        public boolean toFile(File file) {
            FileOutputStream out = null;
            //save response content into file.
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            try {
                out = new FileOutputStream(file);
                InputStream is = connection.getInputStream();
                read(is, out, progressCallback, connection.getContentLength());
                file.setLastModified(System.currentTimeMillis());
                if (downloadCallback != null) {
                    downloadCallback.onSuccess();
                }
                return true;
            } catch (IOException e) {
                if (downloadCallback != null) {
                    downloadCallback.onFailure();
                }
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                    }
                }
            }
            return false;
        }

        public InputStream getInputStream() throws IOException {
            return connection.getInputStream();
        }

        public String getString() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (connection.getResponseCode() != 200) {
                read(connection.getErrorStream(), baos, progressCallback, connection
                        .getContentLength
                                ());
            } else {
                read(connection.getInputStream(), baos, progressCallback, connection
                        .getContentLength
                                ());
            }
            String response = new String(baos.toByteArray());
            return response;
        }

        public byte[] getByteArray() throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            read(connection.getInputStream(), baos, progressCallback, connection.getContentLength
                    ());
            return baos.toByteArray();
        }

        public JSONObject getJSONObject() throws IOException, JSONException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            read(connection.getInputStream(), baos, progressCallback, connection.getContentLength
                    ());
            String log = (new String(baos.toByteArray()));
                Log.e("DDAI", "Response = " + log);
            return new JSONObject(log);
        }
    }

    public static class Brake {
        private volatile boolean stop = false;

        public synchronized void stop() {
            stop = true;
        }

        public boolean hasStop() {
            return stop;
        }
    }

}
