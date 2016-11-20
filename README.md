# Nanorep Widget
--

Nanorep native widget


## Download

Gradle:

```
compile 'com.nanorep:nanowidget:1.1.2'
```

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

## Using Nanorep's widget

```
// Create Accont 
Nanorep.AccountParams accountParams = new Nanorep.AccountParams();
accountParams.setAccount("nanorep");
accountParams.setKnowledgeBase("english");

// Create Nanorep object
Nanorep nanorep = NanorepBuilder.createNanorep(getApplicationContext(), accountParams);

// Create NRWidgetFragment
NRWidgetFragment nanoFragment = NRWidgetFragment.newInstance(null, null);
nanoFragment.setNanoRep(nanorep);

// Present NRWidget
getSupportFragmentManager().beginTransaction().add(R.id.root_layout, nanoFragment, "nanorep").commit();
```
