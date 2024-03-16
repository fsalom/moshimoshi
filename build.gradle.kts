plugins {
    id("maven-publish")
}

buildscript {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io" )
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
    }
}