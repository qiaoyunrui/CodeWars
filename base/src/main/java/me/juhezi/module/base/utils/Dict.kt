package me.juhezi.module.base.utils

/**
 * 字典
 * {"name":"Juhezi","age":"20","Address":"China"}
 * Created by Juhezi[juhezix@163.com] on 2017/12/6.
 */
class Dict(closure: Dict.() -> Unit) {
    private val map = HashMap<String, Any>()

    init {
        closure()
    }

    fun size() = map.size

    fun isEmpty() = map.isEmpty()

    fun add(key: String, value: Any) = map.put(key, value)

    infix fun add(pair: Pair<String, Any>) = map.put(pair.first, pair.second)

    infix fun add(key: String) = DictHandler(this, key)

    operator fun get(key: String) = map[key]

    override fun toString(): String = map.toString()

    inner class DictHandler(private val dict: Dict, private val key: String) {
        infix fun to(value: Any) {
            dict add (key to value)
        }
    }

}

operator fun String.invoke(value: Any) = Pair(this, value)

infix fun String.and(value: Any) = { dict: Dict ->
    dict.add("Hello", "World")
}

private fun sample() {
    //create Dict
    val dict = Dict {
        //multi-type add data
        add("Hello", "World")
        add("Hello" to "World")
        add("Hello"(256))
        "Hello" and "world"(this)
        this add "Hello" to "World"
    }
    //add data
    dict add "Hello" to "World"
    //get data
    dict["Hello"]
}


