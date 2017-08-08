package me.juhezi.module.base.router

import android.content.Intent

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/8.
 */
class IntentRepository : IRepository<Intent> {

    private val mIntentMap = HashMap<String, Intent>()

    override fun get(key: String) = mIntentMap[key]

    override fun put(key: String, value: Intent) {
        mIntentMap.put(key, value)
    }

    override fun size() = mIntentMap.size
}