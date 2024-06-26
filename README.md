[![Release](https://jitpack.io/v/fsalom/moshimoshi.svg)](https://jitpack.io/fsalom/moshimoshi)
[![minSDK](https://img.shields.io/badge/minSDK-28-blue)](https://img.shields.io/badge/minSDK-28-blue)

# MoshiMoshi

## ✍️ About
> Moshi moshi, or もしもし, is a common Japanese phrase that Japanese people use when picking up the phone. It's a casual greeting used for friends and family, like a “hello”

Welcome to the MoshiMoshi Library, the essential toolkit for integrating OAuth2 authentication into your Kotlin applications seamlessly. Built on top of the powerful Retrofit HTTP client, this library is designed to simplify the process of adding secure and efficient OAuth2 authentication flows to your mobile or server-side applications based on kotlin.

## 🤷‍♂️ Why use Moshi Moshi Library?
In the modern application development landscape, securing your application's data and ensuring only authenticated users can access certain resources is paramount. OAuth2 provides a robust framework for securing your application's access, and by using the **Moshi Moshi** Library, you're reducing the complexity of implementing these security measures. Spend less time worrying about authentication flows and more time focusing on building the features that matter to your users.

## 📦 Installation 

### Step 1. Add jitpack dependency
Open the file `settings.gradle` (it looks like that)
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // add jitpack here 👇🏽
        maven { url 'https://jitpack.io' }
       ...
    }
} 
...
```

Or open the file `settings.gradle.kts` (it looks like that)

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // add jitpack here 👇🏽
        maven("https://jitpack.io")
       ...
    }
} 
...
```

### Step 2. Sync the project

### Step 3. Add dependency
```groovy
dependencies {
    implementation 'com.github.fsalom:moshimoshi:{LAST-RELEASE}'
}
```

## 🛠️ Features
- **Easy Integration**: With minimal setup, you can integrate OAuth2 authentication flows into your Kotlin applications, making your development process smoother and faster.
- **Retrofit Compatibility**: Leveraging Retrofit, one of the most popular HTTP client libraries for Android and JVM, ensures that your network operations are efficient, maintainable, and scalable.
- **Flexible OAuth2 Support**: Whether you need to implement Authorization Code Grant, Implicit Grant, Resource Owner Password Credentials Grant, or Client Credentials Grant, our library has got you covered.
- **Secure Token Management**: Automatically handles token refresh and secure storage of access tokens, ensuring that your application communications are always secure.
- **Customizable**: Designed with flexibility in mind, allowing you to customize the authentication flow to meet your specific requirements without compromising on security.

## 🦾 Core elements
    
There are 4 main pieces in this system. Each one of them is responsable of their own area.

- **Authentication Card**: responsable of authentication flow.
- **Authenticator**:  responsable of managing tokens and refresh them.
- **Interceptor**: responsable of handling request and authenticate them.
- **TokenStore**: responsable of storing token information (by default UserDefaults)

## 📚 Examples
This repo includes an android example, which is attached to [App](https://github.com/fsalom/moshimoshi/tree/main/app/src/main/java/com/moshimoshi/app)

## 👨‍💻 Author
[Fernando Salom](https://github.com/fsalom)
