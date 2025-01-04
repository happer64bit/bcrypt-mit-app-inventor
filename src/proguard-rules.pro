# Add any ProGuard configurations specific to this
# extension here.

-keep public class bcrypt.Bcrypt {
    public *;
 }
-keeppackagenames gnu.kawa**, gnu.expr**

-optimizationpasses 4
-allowaccessmodification
-mergeinterfacesaggressively

-repackageclasses 'bcrypt/repack'
-flattenpackagehierarchy
-dontpreverify
