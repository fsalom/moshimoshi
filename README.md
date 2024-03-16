[![Release](https://jitpack.io/v/fsalom/moshimoshi.svg)]
(https://jitpack.io/fsalom/moshimoshi)

# MoshiMoshi

## ✍️ About
> Moshi moshi, or もしもし, is a common Japanese phrase that Japanese people use when picking up the phone. It's a casual greeting used for friends and family, like a “hello”

Welcome to the MoshiMoshi Library, the essential toolkit for integrating OAuth2 authentication into your Kotlin applications seamlessly. Built on top of the powerful Retrofit HTTP client, this library is designed to simplify the process of adding secure and efficient OAuth2 authentication flows to your mobile or server-side applications based on kotlin.

## 🤷‍♂️ Why use Moshi Moshi Library?
In the modern application development landscape, securing your application's data and ensuring only authenticated users can access certain resources is paramount. OAuth2 provides a robust framework for securing your application's access, and by using the **Moshi Moshi** Library, you're reducing the complexity of implementing these security measures. Spend less time worrying about authentication flows and more time focusing on building the features that matter to your users.

## 📦 Installation 

### Prerequisites

- Git installed on your machine
- An existing Git repository for your project

### Step 1: Add the Library as a Submodule

Open your terminal and navigate to the root directory of your project. Run the following command to add the Kotlin OAuth2 Retrofit Library as a submodule:

```bash
git submodule add https://github.com/fsalom/moshimoshi.git path/to/submodule
```
Save to grepper

### Step 2: Initialize and Update the Submodule

After adding the submodule, you need to initialize it and fetch the contents. Run the following commands:

```bash
git submodule init
git submodule update
```

These commands will initialize your local configuration file and fetch all the data from Moshi Moshi Library project, placing it in the path you specified.

### Step 3: Commit the Submodule to Your Project

Now that the submodule is added and initialized, you need to commit these changes to your project. This step ensures that other contributors can initialize and update the submodule on their end after pulling your project. Run the following commands:

```bash
git add .
git commit -m "Add MoshiMoshi Library as a submodule."
git push
```

### Step 4: Cloning a Project with Submodules

If someone is cloning your project with the submodule for the first time, they should clone the repository as usual and then run the following commands to initialize and update the submodules:

```bash
git clone https://github.com/your-username/your-project.git
cd your-project
git submodule update --init --recursive
```

This ensures that MoshiMoshi Library submodule is correctly set up and ready to use in their local development environment.

### Step 5: Add dependency to your project

In the `setting.gradle` of your project you must add:

```bash
include(":PATH_TO_SUBMODULE:moshimoshi")

example:
include(":mySubmoduleFolder:moshimoshi")
```

### Step 6: Update submodule

Each time that library needs to be updated add:

```bash
git submodule update --remote --recursive
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
