## FNSuperWeb 超级浏览器
* 二维码扫一波下载APK看看效果
![https://source.enncloud.cn/FNAndroidTeam/FNSuperWeb/src/b0c65b37cad705aa7b537bb54b5b9956a3282257/1533718400.png](https://source.enncloud.cn/FNAndroidTeam/FNSuperWeb/src/b0c65b37cad705aa7b537bb54b5b9956a3282257/1533718400.png)
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
* 文件上传（Android与JS通信）
* 文件下载
* 未完待续。。。


#### 3、 作者
* 鲁宇峰   邮箱：466708987@qq.com
