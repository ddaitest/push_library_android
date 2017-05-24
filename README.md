# push_library_android
include Huawei, Mi and UMeng Push SDK.  Choose the right one according to the device.


## setup
1. modify build.gradle
https://jitpack.io/#ddaitest/push_library_android/

2. modify manifest.xml

add part 1

```
    <permission
        android:name="com.daivp.pushcollector.permission.MIPUSH_RECEIVE"
        android:protectionLevel="signature" />

    <uses-permission android:name="com.daivp.pushcollector.permission.MIPUSH_RECEIVE" />
```


3. 接受push intent



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