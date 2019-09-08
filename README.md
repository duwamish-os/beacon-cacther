
Layouts
------

* [iBeacon](https://developer.apple.com/ibeacon/)

```bash
m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24
```

https://beaconlayout.wordpress.com

FIXME
----

```bash
2019-09-07 15:27:09.293 14718-14718/com.duwamish.radio.transmitter I/zygote: Rejecting re-init on previously-failed class java.lang.Class<android.support.v4.view.ViewCompat$OnUnhandledKeyEventListenerWrapper>: java.lang.NoClassDefFoundError: Failed resolution of: Landroid/view/View$OnUnhandledKeyEventListener;
2019-09-07 15:27:09.293 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void android.support.v4.view.ViewCompat.setOnApplyWindowInsetsListener(android.view.View, android.support.v4.view.OnApplyWindowInsetsListener) (ViewCompat.java:2203)
2019-09-07 15:27:09.293 14718-14718/com.duwamish.radio.transmitter I/zygote:     at android.view.ViewGroup android.support.v7.app.AppCompatDelegateImpl.createSubDecor() (AppCompatDelegateImpl.java:637)
2019-09-07 15:27:09.293 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void android.support.v7.app.AppCompatDelegateImpl.ensureSubDecor() (AppCompatDelegateImpl.java:518)
2019-09-07 15:27:09.293 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void android.support.v7.app.AppCompatDelegateImpl.setContentView(int) (AppCompatDelegateImpl.java:466)
2019-09-07 15:27:09.293 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void android.support.v7.app.AppCompatActivity.setContentView(int) (AppCompatActivity.java:140)
2019-09-07 15:27:09.293 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void com.duwamish.radio.transmitter.BeaconCatcherController.onCreate(android.os.Bundle) (BeaconCatcherController.kt:46)
2019-09-07 15:27:09.293 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void android.app.Activity.performCreate(android.os.Bundle, android.os.PersistableBundle) (Activity.java:6999)
2019-09-07 15:27:09.293 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void android.app.Activity.performCreate(android.os.Bundle) (Activity.java:6990)
2019-09-07 15:27:09.293 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void android.app.Instrumentation.callActivityOnCreate(android.app.Activity, android.os.Bundle) (Instrumentation.java:1214)
2019-09-07 15:27:09.293 14718-14718/com.duwamish.radio.transmitter I/zygote:     at android.app.Activity android.app.ActivityThread.performLaunchActivity(android.app.ActivityThread$ActivityClientRecord, android.content.Intent) (ActivityThread.java:2731)
2019-09-07 15:27:09.294 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void android.app.ActivityThread.handleLaunchActivity(android.app.ActivityThread$ActivityClientRecord, android.content.Intent, java.lang.String) (ActivityThread.java:2856)
2019-09-07 15:27:09.294 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void android.app.ActivityThread.-wrap11(android.app.ActivityThread, android.app.ActivityThread$ActivityClientRecord, android.content.Intent, java.lang.String) (ActivityThread.java:-1)
2019-09-07 15:27:09.294 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void android.app.ActivityThread$H.handleMessage(android.os.Message) (ActivityThread.java:1589)
2019-09-07 15:27:09.294 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void android.os.Handler.dispatchMessage(android.os.Message) (Handler.java:106)
2019-09-07 15:27:09.294 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void android.os.Looper.loop() (Looper.java:164)
2019-09-07 15:27:09.294 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void android.app.ActivityThread.main(java.lang.String[]) (ActivityThread.java:6494)
2019-09-07 15:27:09.294 14718-14718/com.duwamish.radio.transmitter I/zygote:     at java.lang.Object java.lang.reflect.Method.invoke(java.lang.Object, java.lang.Object[]) (Method.java:-2)
2019-09-07 15:27:09.294 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run() (RuntimeInit.java:438)
2019-09-07 15:27:09.294 14718-14718/com.duwamish.radio.transmitter I/zygote:     at void com.android.internal.os.ZygoteInit.main(java.lang.String[]) (ZygoteInit.java:807)
2019-09-07 15:27:09.294 14718-14718/com.duwamish.radio.transmitter I/zygote: Caused by: java.lang.ClassNotFoundException: Didn't find class "android.view.View$OnUnhandledKeyEventListener" on path: DexPathList[[zip file "/data/app/com.duwamish.radio.transmitter-FtcoIOHH2kvjqqHIpQiWGw==/base.apk", zip file "/data/app/com.duwamish.radio.transmitter-FtcoIOHH2kvjqqHIpQiWGw==/split_lib_dependencies_apk.apk", zip file "/data/app/com.duwamish.radio.transmitter-FtcoIOHH2kvjqqHIpQiWGw==/split_lib_resources_apk.apk", zip file "/data/app/com.duwamish.radio.transmitter-FtcoIOHH2kvjqqHIpQiWGw==/split_lib_slice_0_apk.apk", zip file "/data/app/com.duwamish.radio.transmitter-FtcoIOHH2kvjqqHIpQiWGw==/split_lib_slice_1_apk.apk", zip file "/data/app/com.duwamish.radio.transmitter-FtcoIOHH2kvjqqHIpQiWGw==/split_lib_slice_2_apk.apk", zip file "/data/app/com.duwamish.radio.transmitter-FtcoIOHH2kvjqqHIpQiWGw==/split_lib_slice_3_apk.apk", zip file "/data/app/com.duwamish.radio.transmitter-FtcoIOHH2kvjqqHIpQiWGw==/split_lib_slice_4_apk.apk", zip file "/data/app/com.duwamish.radio.transmitter-FtcoIOHH2kvjqqHIp
2019-09-07 15:27:09.295 14718-14718/com.duwamish.radio.transmitter I/zygote:     at java.lang.Class dalvik.system.BaseDexClassLoader.findClass(java.lang.String) (BaseDexClassLoader.java:125)

```

run on device
--

- tap `Build number` 7(?) times
- enable Debugging
- restart debug bridge

https://developer.android.com/studio/run/device

example beacon

```bash
2019-09-07 21:33:45.809 7141-7141/com.duwamish.radio.transmitter I/StandardBeaconCacther: UUID: P1F54E02-1E23-44P0-9C3D-512PB56ADED9\nmajor: 100\nminor100
```

references
---

- https://github.com/kiteflo/iBeaconAndroidDemo
- https://developer.radiusnetworks.com/2015/09/29/is-your-beacon-app-ready-for-android-6.html
