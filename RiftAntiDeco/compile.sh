rm -Rf bin test.jar
javac -cp src:lib/forms-1.3.0pre4.jar -d bin src/ch/correvon/rift/antiDeco/use/RiftAntiDecoMain.java
jar cfm test.jar MANIFEST.MF -C bin ch
java -jar test.jar
