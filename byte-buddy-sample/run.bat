
set bb=byte-buddy-1.14.7.jar

rmdir .\META-INF /S /Q
rmdir .\agent-extracted-predefined-classes /S /Q

del Sample.class

javac -cp "%bb%;." Sample.java

REM java -agentlib:native-image-agent=config-output-dir=META-INF/native-image,experimental-class-define-support -Dorg.graalvm.nativeimage.imagecode=agent -cp "byte-buddy-1.14.9.jar;." Sample

java -agentlib:native-image-agent=config-output-dir=META-INF/native-image,experimental-class-define-support -cp "%bb%;." Sample

REM Don't know why but this folder needs to be in the root folder and not as subdir of META-INF
xcopy .\META-INF\native-image\agent-extracted-predefined-classes .\agent-extracted-predefined-classes /E/H/C/I

native-image -cp "%bb%;." Sample

pause