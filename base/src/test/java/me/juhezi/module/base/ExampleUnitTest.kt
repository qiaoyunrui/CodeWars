package me.juhezi.module.base

import me.juhezi.module.base.extensions.MD5Code
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    @Throws(Exception::class)
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    @Test
    fun checkStringMD5() {
//        assertEquals("juhezi".MD5Code(), "Juhezi".MD5Code())
        println("HelloWorld".MD5Code())
    }

}