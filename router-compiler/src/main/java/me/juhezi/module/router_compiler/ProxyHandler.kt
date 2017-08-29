package me.juhezi.module.router_compiler

import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/29.
 */
const val SUFFIX = "\$Proxy"

class ProxyHandler(private var proxyElement: Element) {
    private val registerElement: MutableList<Element> = ArrayList()
    var typeElement: TypeElement = proxyElement.enclosedElements as TypeElement

    fun add(element: Element) = registerElement.add(element)

    fun remove(element: Element) = registerElement.remove(element)

    fun addAll(collection: Collection<Element>) = registerElement.addAll(collection)

    fun clear() = registerElement.clear()

    /**
     * 生成代理类的内容
     */
    fun generate(): String {
        return """
package ${getPackageName()};

public class ${getSimpleGenerateClassName()} implements IRouterProxy {

    @Override
    public void proxy() {
        Repository.register(me.juhezi.module.base.view.activity.DefaultActivity.class);
    }

}
        """
    }

    fun getGenerateClassName(): String = typeElement.qualifiedName.toString() + SUFFIX

    private fun getSimpleGenerateClassName() = typeElement.simpleName.toString() + SUFFIX

    private fun getPackageName(): String {
        val name = typeElement.qualifiedName.toString()
        return name.substring(0, name.lastIndexOf('.'))
    }

}