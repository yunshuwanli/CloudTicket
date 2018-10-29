### 开票通打包流程
---
####1.代码环境检测

  `App.java`类中有测试环境与debug环境的开关 发布时应将开关关掉

     @Override
    public boolean getDebugSetting() {
        return true;//为debug
    }

    @Override
    public boolean isTestUrl() {
        return true;//为测试
    }


####2.签名
签名文件

     doc/android_studio_strokey.jks

签名已配置，可在build/build APK 菜单中打包



####3.应用升级
具体请看`bugly：https://bugly.qq.com/v2/upgrade/`


