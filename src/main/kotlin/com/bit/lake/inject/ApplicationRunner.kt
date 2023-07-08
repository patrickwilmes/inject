package com.bit.lake.inject

class Module() {
    private val singletons = mutableMapOf<Class<*>, Any>()

    internal constructor(singletons: Map<Class<*>, Any>) : this() {
        this.singletons.putAll(singletons)
    }

    fun <T : Any> singleOf(clazz: Class<*>, init: Module.() -> T) {
        singletons[clazz] = init()
    }

    inline fun <reified T : Any> singleOf(noinline init: Module.() -> T) {
        singleOf(T::class.java, init)
    }

    fun get(clazz: Class<*>): Any {
        return singletons[clazz]!!
    }

    inline fun <reified T : Any> get(): T {
        return get(T::class.java) as T
    }

    internal fun merge(module: Module): Module {
        val newSingletons = singletons + module.singletons
        return Module(newSingletons)
    }
}

fun module(init: Module.() -> Unit): Module {
    val module = Module()
    module.init()
    return module
}

fun start(vararg modules: Module): Module {
    return modules.reduce { acc, module -> acc.merge(module) }
}
