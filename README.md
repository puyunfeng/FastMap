1. 在AndroidManifest中添加权限
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.CHANGE_CONFIGURATION" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.WRITE_SETTINGS" />
2. 加入
<meta-data android:name="com.amap.api.v2.apikey" android:value="高德key"/>
3. 加入
 <!-- 定位需要的服务 -->
 <service android:name="com.amap.api.location.APSService"></service>

How to
To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

gradle
maven
sbt
leiningen
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.puyunfeng:FastMap:0.0.1'
	}
引入布局文件

         <include layout="@layout/map_view"></include>