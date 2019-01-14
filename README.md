# SpringEffectLibrary
An Android custom ListView with pull to zoom-in and spring effect header.<br>
<br>
![](https://github.com/SGagan/SpringEffectLibrary/raw/master/extras/gifeditor_20190113_215408.gif)
<br/>

## Include SpringEffectLibrary to Your Project
SpringEffectLibrary is available in Jcenter, so getting it as simple as adding it as a dependency
<br>
With gradle:
```groovy
dependencies {
   implementation 'com.github.sgagan:SpringEffectLibrary:1.0.0'
}
```
## Usage in Layout Files
```xml
 <com.example.gagan.springeffectlibrary.SpringEffectListView
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       android:divider="@android:color/transparent"
       android:id="@+id/listView"/>
```
<br/>

```xml
<com.example.gagan.springeffectlibrary.AboveContainer
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@android:color/white">
    <ImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scaleType="centerCrop"
        android:background="@drawable/image"
        />
</com.example.gagan.springeffectlibrary.AboveContainer>
```
