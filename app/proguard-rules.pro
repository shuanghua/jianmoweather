-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.loc.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

-keepclasseswithmembers class ** {
    kotlinx.serialization.KSerializer serializer(...);
}
