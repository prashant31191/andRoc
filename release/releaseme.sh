#!/bin/sh
jarsigner -verbose -storepass rocrail -keystore androc-release-key.keystore andRoc.apk androc
~/programs/android-sdk-linux_86/tools/zipalign -v 4 andRoc.apk andRoc-rev$1.apk
#~/android-sdk-mac_86/tools/zipalign -v 4 andRoc.apk andRoc-rev$1.apk