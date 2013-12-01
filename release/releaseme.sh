#!/bin/sh
jarsigner -verbose -storepass rocrail -keystore androc-release-key.keystore andRoc.apk androc
#~/programs/android-sdk-linux_x86/tools/zipalign -v 4 andRoc.apk andRoc-rev$1.apk
~/adt-bundle-mac-x86_64-20131030/sdk/tools/zipalign -v 4 andRoc.apk andRoc-rev$1.apk
