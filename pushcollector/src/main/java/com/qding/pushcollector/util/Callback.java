package com.qding.pushcollector.util;

import android.graphics.Bitmap;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by ning.dai on 14-9-22.
 */
public interface Callback<Result extends Object> {

	void onFinish(Result result);

	void onError();

	public interface ProgressCallback {
		void onProgress(int percent);
	}
	public interface DownloadCallback{
		void onSuccess();
		void onFailure();
	}
	public interface StreamCallback extends Callback<InputStream> {

	}

	public interface JSONCallback extends Callback<JSONObject> {

	}

	public interface StringCallback extends Callback<String> {

	}

	public interface BitmapCallback extends Callback<Bitmap> {

	}

}
