package me.juhezi.module.router_compiler.processor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juhezi[juhezix@163.com] on 2017/8/21.
 */

public class ProxyInfo {
    private List<String> classNames = new ArrayList<>();

    public void add(String className) {
        classNames.add(className);
    }

    public void clear() {
        classNames.clear();
    }

    /**
     * 生成 Java 代码
     *
     * @return
     */
    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code. Do not modify!\n");
        builder.append("package ").append("com.example.juhezi.test").append(";\n\n");
        //import
        builder.append('\n');

        builder.append("public class ").append("Proxy");
        builder.append(" {\n");

        for (String className : classNames) {
            builder.append("//").append(className);
        }
        builder.append("}\n");
        return builder.toString();
    }

}
