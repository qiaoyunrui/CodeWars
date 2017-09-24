package me.juhezi.module.base.network

/**
 * Created by Juhezi[juhezix@163.com] on 2017/9/24.
 */
class ApiException(var status: String, message: String)
    : RuntimeException(message)