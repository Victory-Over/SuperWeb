## FNSuperWeb 超级浏览器

#### 基于腾讯X5内核

#### 目前已有功能：

* 1、文件浏览（支持pdf/ppt/doc/excel/txt）
* 2、文件上传（JS）
* 3、文件下载
* 4、Android与JS交互
* 5、自定义交互(进度条、下拉回弹)
* 6、视频播放


#### 1、添加依赖和配置
* 根目录build.gradle文件添加如下配置：

```Java
allprojects {
    repositories {
        maven { url "https://source.enncloud.cn/FNAndroidTeam/FNSuperWeb/raw/master/superweb" }
    }
}
```

* APP目录build.gradle文件添加如下配置：

```Java
dependencies {
    implementation 'com.fanneng.android:superweb:1.0.0@aar'
}
```

* 申请权限：

```Java
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.CAMERA" />
```



#### 2、基本功能
* 文件浏览（支持pdf/ppt/doc/excel/txt）

![点我查看效果图](https://github.com/Victory-Over/SuperWeb/blob/master/file_open.gif)

* 文件上传（Android与JS通信）

![点我查看效果图](https://github.com/Victory-Over/SuperWeb/blob/master/file_upload.gif)

* 文件下载

![点我查看效果图](https://github.com/Victory-Over/SuperWeb/blob/master/file_download.gif)

* Android与JS交互

![点我查看效果图](https://github.com/Victory-Over/SuperWeb/blob/master/js.gif)

* 自定义交互(进度条、下拉回弹)

![点我查看效果图](https://github.com/Victory-Over/SuperWeb/blob/master/interactive.gif)

* 视频播放

![点我查看效果图](https://github.com/Victory-Over/SuperWeb/blob/master/video.gif)



#### 3、 作者
* 鲁宇峰   邮箱：466708987@qq.com
