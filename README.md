#DakraZxing QR reader

这是基于Zxing的一个简单的1D/2D条码扫描器。

### How to Use/Import

由于bintray的下载速度实在不太敢恭维，那我就懒一点了，直接把maven repository直接放在项目仓库中，目录为repo，所以我们使用时，可以把repo目录复制到项目目录里使用。

```groovy
repositories {
        maven {
            name 'localRepo'
            url '../repo' //这里是你存放repo的相对目录，请酌情调整
        }
}
```

然后在主项目中，添加以下依赖

```groovy
implementation 'org.xellossryan.qrlib:qrzxing-lib:1.1.1'
implementation 'com.google.zxing:core:3.2.1'
```



### Links

[https://github.com/zxing/zxing](https://github.com/zxing/zxing)



### LICENSE

> zxinglib库中有些代码不是我写的，来源已不可考，就签一个MIT协议吧。



