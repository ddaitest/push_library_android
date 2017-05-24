# push_library_android
include Huawei, Mi and UMeng Push SDK.  Choose the right one according to the device.


## setup

### 1. modify build.gradle
https://jitpack.io/#ddaitest/push_library_android/

### 2. init push library
2.1 in application onCreate()

```Java
@Override
    public void onCreate() {
    ...
	PushManager.init(this);
    ...
}
```


2.2  in Main activity

```Java
@Override
    protected void onCreate(Bundle savedInstanceState) {
    ...
	PushManager.onCreate(this, new TokenListener() {
	@Override
	public void gotToken(@Constants.OSType int type, String token) {
		//TODO handle the token.
            }
        });
    ...
    }

    @Override
    protected void onStart() {
        super.onStart();
        PushManager.onStart(this);
    }
```

2.4 create activity for receive intent

```Java
@Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent != null) {
            StringBuilder builder = new StringBuilder();
            String message = intent.getStringExtra("notify_hw");
            if (!TextUtils.isEmpty(message)) {
                builder.append("notify_hw=");
                builder.append(message);
                builder.append(";");
            }
            message = intent.getStringExtra("notify_mi");
            if (!TextUtils.isEmpty(message)) {
                builder.append("notify_mi=");
                builder.append(message);
                builder.append(";");
            }
            message = intent.getStringExtra("notify_um");
            if (!TextUtils.isEmpty(message)) {
                builder.append("notify_um=");
                builder.append(message);
                builder.append(";");
            }

            tv.setText(builder.toString());
        }
    }
```

### 3. modify manifest.xml

3.1. add permissions

```XML
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.GET_TASKS" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.RECEIVE_USER_PRESENT" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />

```

3.2. add custom permissions.
replace ==com.xxx.xxx== to your packagename.

```XML
    <permission
        android:name="com.xxx.xxx.permission.MIPUSH_RECEIVE"
        android:protectionLevel="signature" />

    <uses-permission android:name="com.xxx.xxx.permission.MIPUSH_RECEIVE" />

```
3.3. Add Activity
```XML
<activity
            android:name="com.xxx.xxx.ParserActivity"
            android:screenOrientation="portrait">
            <intent-filter>
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <data
                    android:host="com.qding.push"
                    android:path="/parser"
                    android:scheme="QDPUSH" />
            </intent-filter>
        </activity>
```

3.4. Add Platform Setting

replace ==com.xxx.xxx== to your packagename.
modify app infos.

```XML
<!-- Hua wei setting -->
        <meta-data
            android:name="com.huawei.hms.client.appid"
            android:value="@string/push_hw_app_id" />

        <!-- "com.qding.pushcollector" 替换为 app 包名 -->
        <provider
            android:name="com.huawei.hms.update.provider.UpdateProvider"
            android:authorities="com.xxx.xxx.hms.update.provider"
            android:exported="false"
            android:grantUriPermissions="true" />
        <!-- end Hua wei setting -->


        <!-- umeng setting -->
        <meta-data
            android:name="UMENG_APPKEY"
            android:value="@string/push_um_app_key" />
        <meta-data
            android:name="UMENG_MESSAGE_SECRET"
            android:value="@string/push_um_app_secret" />
        <meta-data
            android:name="UMENG_CHANNEL"
            android:value="DDAI" />
        <!-- end umeng setting -->


        <!-- MI Setting -->
        <meta-data
            android:name="MI_APP_ID"
            android:value="@string/push_mi_app_id" />
        <meta-data
            android:name="MI_APP_KEY"
            android:value="@string/push_mi_app_key" />
```

## keep setting

### MI

```
-keep class com.qding.push.MiPushReceiver {*;}
-dontwarn com.xiaomi.push.**
```


### umeng

```
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }

-keep public class com.umeng.fb.ui.ThreadView { }

```

### 华为

```
-keepattributes *Annotation*
-keepattributes Exceptions -keepattributes InnerClasses -keepattributes Signature
# hmscore-support: remote transport
-keep class * extends com.huawei.hms.core.aidl.IMessageEntity { *; }
# hmscore-support: remote transport
-keepclasseswithmembers class * implements com.huawei.hms.support.api.transport.DatagramTransport {
<init>(...); }
# manifest: provider for updates
-keep public class com.huawei.hms.update.provider.UpdateProvider { public *; protected *; }


```