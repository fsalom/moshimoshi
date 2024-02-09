# MoshiMoshi

## ✍️ About
> Moshi moshi, or もしもし, is a common Japanese phrase that Japanese people use when picking up the phone. It's a casual greeting used for friends and family, like a “hello”

Welcome to the MoshiMoshi Library, the essential toolkit for integrating OAuth2 authentication into your Kotlin applications seamlessly. Built on top of the powerful Retrofit HTTP client, this library is designed to simplify the process of adding secure and efficient OAuth2 authentication flows to your mobile or server-side applications.

## 📦 Installation 

## 🦾 Core elements
There are 4 main pieces in this system. Each one of them is responsable of their own area.

- **Authentication Card**: responsable of authentication flow.
- **Authenticator**:  responsable of managing tokens and refresh them.
- **Interceptor**: responsable of handling request and authenticate them.
- **TokenStore**: responsable of storing token information (by default UserDefaults)

### Network

### Endpoint

## 🚀 Usage non authorized API
As an example we will use https://coincap.io and their crypto list. This API is free to use and do not have any kind of authentication system

### Endpoint

### Making request

## 🔒 Usage authorized API

## 📚 Examples
This repo includes an android example, which is attached to [App](https://github.com/fsalom/moshimoshi/tree/main/app/src/main/java/com/moshimoshi/app)

## 👨‍💻 Author
[Fernando Salom](https://github.com/fsalom)
