## FNSuperWeb 超级浏览器
#### 1、添加依赖和配置
* 根目录build.gradle文件添加如下配置：

>>```
allprojects {
    repositories {
        maven { url "https://source.enncloud.cn/FNAndroidTeam/FNSuperWeb/raw/master/superweb" }
    }
}
```

* APP目录build.gradle文件添加如下配置：

>>```
dependencies {
    implementation 'com.fanneng.android:superweb:1.0.0@aar'
}
```

#### 2、基本功能
* 文件浏览（支持pdf/ppt/doc/excel/txt）
![img](https://source.enncloud.cn/FNAndroidTeam/FNSuperWeb/src/bb5d8aeed3888043606bfc5833ea8aff4f336167/file_open.gif)
* 文件上传（Android与JS通信）
![img](https://source.enncloud.cn/FNAndroidTeam/FNSuperWeb/src/bb5d8aeed3888043606bfc5833ea8aff4f336167/file_upload.gif)
* 文件下载
![img](https://source.enncloud.cn/FNAndroidTeam/FNSuperWeb/src/bb5d8aeed3888043606bfc5833ea8aff4f336167/file_downloader.gif)
* 未完待续。。。


#### 3、 作者
* 鲁宇峰   邮箱：466708987@qq.com
