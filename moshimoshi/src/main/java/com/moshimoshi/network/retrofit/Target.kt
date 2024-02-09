package com.moshimoshi.network.retrofit

import okhttp3.Request
import retrofit2.Invocation

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
annotation class Authenticated

fun <T : Annotation> Request.getAnnotation(annotationClass: Class<T>): T? =
    this.tag(Invocation::class.java)?.method()?.getAnnotation(annotationClass)

fun Request.authenticated(): Boolean {
    return this.getAnnotation(Authenticated::class.java) != null
}