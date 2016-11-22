<snippet>
  <content>
# Nanorep widget
Welcome to Nanorep Android SDK. This demo app demonstrates how to use the Nanorep Mobile SDK to build native support into your mobile application.
## Installation
Add the library as a dependency in your build.gradle file.
```java
dependencies {
    compile ''
}
```
## Usage
Init Nanorep SDK with your account name and knowledge base in Application class:
```java
NRImpl.getInstance().init(getApplicationContext(), _accountName, _kb);
```

Create new instance of NRMainFragment and open it:
```java
NRMainFragment mainFragment = NRMainFragment.newInstance();
```
# UI Customization
You can customize the UI of the following fragment's parts:

 - for Search Bar extend NRCustomSearchBarView abstract class.
 - for Suggestions View extend NRCustomSuggestionsView abstract class.
 - for Article's title extend NRCustomTitleView abstract class.
 - for Article's content extend NRCustomContentView abstract class.
 - for Like view extend NRCustomLikeView abstract class.
 - for Channel view extend NRCustomChannelView abstract class.
 
 Implement NRCustomViewAdapter interface and implement its methods.
 For example, to customize article's title:
 
 First, create a class that extends NRCustomTitleView
 
```java
  @Override
    public NRCustomTitleView getTitle(Context context) {
      NRTitleView titleView = new TitleView(context);
      TitleView titleView = new TitleView(context);

      return titleView;
    }
```

</content>
</snippet>
