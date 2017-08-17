package me.juhezi.module.base.router.web

import me.juhezi.module.base.router.IRepository

/**
 * Test
 * Created by Juhezi on 2017/8/17.
 */
class WebRepository : IRepository<Any> {
    override fun get(key: String): Any? = "Hello"

    override fun put(key: String, value: Any) {}

    override fun size(): Int = 0
}