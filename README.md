<snippet>
  <content>
# Nanorep widget
Welcome to Nanorep Android SDK. This demo app demonstrates how to use the Nanorep Mobile SDK to build native support into your mobile application.
## Installation

Add to your Project's build.gradle :
```
allprojects {
    repositories {
        maven {
            url 'https://dl.bintray.com/nissop/maven/'
        }
        jcenter()
    }
}
```

Add the library as a dependency in your build.gradle file.
```java
dependencies {
    compile 'com.nanorep:nanowidget:1.3.2'
}
```
## Usage
Init Nanorep SDK with your account name and knowledge base in Application class:
```
Nanorep.getInstance().init(getApplicationContext(),new AccountParams(_accountName, _kb));
```

Create a new instance of NRMainFragment and open it:
```
NRMainFragment mainFragment = NRMainFragment.newInstance();
```



</content>
</snippet>
