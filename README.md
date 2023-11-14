# Hibernate Sample For GraalVM Native-Compilation

Sample project to reproduce compilation issues with GraalVM in combination with Hibernate.

> :warning:  **This sample is just a reproducer - It's not working yet!**
 
## Setup
java version "21.0.1" 2023-10-17
Java(TM) SE Runtime Environment Oracle GraalVM 21.0.1+12.1 (build 21.0.1+12-jvmci-23.1-b19)
Java HotSpot(TM) 64-Bit Server VM Oracle GraalVM 21.0.1+12.1 (build 21.0.1+12-jvmci-23.1-b19, mixed mode, sharing)


## Deployment Steps
### 1. Generate Shadow (all-in-one) Jar
```
    .\gradlew shadowJar
```
### 2. Collect Metadata with the Tracing Agent
```
java -agentlib:native-image-agent=config-output-dir=META-INF/native-image,experimental-class-define-support -cp "HibernateSampleGraalVM-0.0.1-all.jar;." sample.hibernate.DbMain
```
This will generate metadata into ```META-INF/native-image``` folder. 

Through the additional parameter ```experimental-class-define-support``` an extra folder named ```agent-extracted-predefined-classes``` will be generated. It contains bytecode of loaded classes
which is loaded during the image build.

### 3. Native Compile Preparation
Copy the generated metadata into the resources folder to make them available for native compilation.

### 4. Start native compilation

``` 
.\gradlew nativeCompile
```

### 5. Execute Runnable DB.exe 

#### Output logs
```
12:09:19.472 [main] DEBUG org.hibernate.internal.SessionFactoryImpl -- HHH000031: Closing
12:09:19.472 [main] DEBUG org.hibernate.internal.SessionFactoryImpl -- Eating error closing SF on failed attempt to start it
Exception in thread "main" org.hibernate.MappingException: Could not instantiate persister org.hibernate.persister.entity.SingleTableEntityPersister
        at org.hibernate.persister.internal.PersisterFactoryImpl.createEntityPersister(PersisterFactoryImpl.java:103)
        at org.hibernate.persister.internal.PersisterFactoryImpl.createEntityPersister(PersisterFactoryImpl.java:75)
        at org.hibernate.metamodel.model.domain.internal.MappingMetamodelImpl.processBootEntities(MappingMetamodelImpl.java:278)
        at org.hibernate.metamodel.model.domain.internal.MappingMetamodelImpl.finishInitialization(MappingMetamodelImpl.java:211)
        at org.hibernate.metamodel.internal.RuntimeMetamodelsImpl.finishInitialization(RuntimeMetamodelsImpl.java:60)
        at org.hibernate.internal.SessionFactoryImpl.<init>(SessionFactoryImpl.java:308)
        at org.hibernate.boot.internal.SessionFactoryBuilderImpl.build(SessionFactoryBuilderImpl.java:415)
        at org.hibernate.cfg.Configuration.buildSessionFactory(Configuration.java:754)
        at org.hibernate.cfg.Configuration.buildSessionFactory(Configuration.java:773)
        at sample.hibernate.DbMain.start(DbMain.java:44)
        at sample.hibernate.DbMain.main(DbMain.java:59)
        at java.base@21.0.1/java.lang.invoke.LambdaForm$DMH/sa346b79c.invokeStaticInit(LambdaForm$DMH)
Caused by: java.lang.IllegalArgumentException: Could not create type
        at net.bytebuddy.TypeCache.findOrInsert(TypeCache.java:170)
        at net.bytebuddy.TypeCache.findOrInsert(TypeCache.java:190)
        at org.hibernate.bytecode.internal.bytebuddy.ByteBuddyState.load(ByteBuddyState.java:179)
        at org.hibernate.bytecode.internal.bytebuddy.ByteBuddyState.loadProxy(ByteBuddyState.java:103)
        at org.hibernate.proxy.pojo.bytebuddy.ByteBuddyProxyHelper.buildProxy(ByteBuddyProxyHelper.java:60)
        at org.hibernate.proxy.pojo.bytebuddy.ByteBuddyProxyFactory.postInstantiate(ByteBuddyProxyFactory.java:61)
        at org.hibernate.metamodel.internal.EntityRepresentationStrategyPojoStandard.createProxyFactory(EntityRepresentationStrategyPojoStandard.java:273)
        at org.hibernate.metamodel.internal.EntityRepresentationStrategyPojoStandard.<init>(EntityRepresentationStrategyPojoStandard.java:155)
        at org.hibernate.metamodel.internal.ManagedTypeRepresentationResolverStandard.resolveStrategy(ManagedTypeRepresentationResolverStandard.java:60)
        at org.hibernate.persister.entity.AbstractEntityPersister.<init>(AbstractEntityPersister.java:725)
        at org.hibernate.persister.entity.SingleTableEntityPersister.<init>(SingleTableEntityPersister.java:152)
        at java.base@21.0.1/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
        at java.base@21.0.1/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
        at org.hibernate.persister.internal.PersisterFactoryImpl.createEntityPersister(PersisterFactoryImpl.java:92)
        ... 11 more
Caused by: java.lang.IllegalStateException: java.lang.UnsupportedOperationException: Defining new classes at runtime is not supported
        at net.bytebuddy.dynamic.loading.ClassInjector$UsingLookup.injectRaw(ClassInjector.java:1640)
        at net.bytebuddy.dynamic.loading.ClassInjector$AbstractBase.inject(ClassInjector.java:118)
        at net.bytebuddy.dynamic.loading.ClassLoadingStrategy$UsingLookup.load(ClassLoadingStrategy.java:519)
        at net.bytebuddy.dynamic.TypeResolutionStrategy$Passive.initialize(TypeResolutionStrategy.java:101)
        at net.bytebuddy.dynamic.DynamicType$Default$Unloaded.load(DynamicType.java:6166)
        at org.hibernate.bytecode.internal.bytebuddy.ByteBuddyState.lambda$load$0(ByteBuddyState.java:183)
        at net.bytebuddy.TypeCache.findOrInsert(TypeCache.java:168)
        ... 24 more
Caused by: java.lang.UnsupportedOperationException: Defining new classes at runtime is not supported
        at org.graalvm.nativeimage.builder/com.oracle.svm.core.util.VMError.unimplemented(VMError.java:195)
        at java.base@21.0.1/java.lang.invoke.MethodHandles$Lookup.defineClass(MethodHandles.java:45)
        at java.base@21.0.1/java.lang.reflect.Method.invoke(Method.java:580)
        at net.bytebuddy.utility.Invoker$Dispatcher.invoke(Unknown Source)
        at net.bytebuddy.utility.dispatcher.JavaDispatcher$Dispatcher$ForNonStaticMethod.invoke(JavaDispatcher.java:1028)
        at net.bytebuddy.utility.dispatcher.JavaDispatcher$ProxiedInvocationHandler.invoke(JavaDispatcher.java:1158)
        at jdk.proxy4/jdk.proxy4.$Proxy54.defineClass(Unknown Source)
        at net.bytebuddy.dynamic.loading.ClassInjector$UsingLookup.injectRaw(ClassInjector.java:1638)
        ... 30 more
```

### Assumption

- The only class that is generated by ```experimental-class-define-support``` is ```net.bytebuddy.utility.Invoker$Dispatcher```
And exactly this class is listed in the stacktrace. I don't know if there is an error in the reachability of this extra folder during compilation?!
Anyhow, reverse-monitoring shows a successful access to the file. So the location should be fine.


- I was wondering if there is a issue with byte-buddy, due to its dynamic class generation tasks. But a separate example compiling only byte-buddy logic without hibernate went well. See folder ```byte-buddy-sample```


 
