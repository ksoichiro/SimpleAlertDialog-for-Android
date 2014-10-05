# Releasing

## Versioning

Change version in root project's `gradle.properties`:

Staging:

```
VERSION_NAME=1.1.3-SNAPSHOT
```

Release:

```
VERSION_NAME=1.1.3
```

## Releasing

```
./gradlew clean build connectedCheck uploadArchives
```

## Checking

After releasing to snapshots, check if snapshot is resolved correctly and works.

`simplealertdialog-samples/demos/build.gradle`:

```groovy
repositories {
    mavenCentral()
    maven {
        url uri('https://oss.sonatype.org/content/repositories/snapshots/')
    }
}

dependencies {
    compile 'com.android.support:support-v4:20.0.+'
    //compile project(':simplealertdialog')
    compile 'com.github.ksoichiro:simplealertdialog:1.1.3-SNAPSHOT'
}
```
