package net.pechorina.kairos.automat

import java.util.concurrent.ConcurrentHashMap

class ExtendedState<S, E>(val automat: Automat<S, E>) {
    private val store: MutableMap<String, Any> = ConcurrentHashMap()

    fun put(key: String, newValue: Any): Any? {
        val oldValue = store.put(key, newValue)
        automat.listeners().forEach { it.extendedStateChanged(key, oldValue, newValue) }
        return oldValue
    }

    fun get(key: String): Any? {
        return store[key]
    }

    fun putAll(pairs: Collection<Pair<String, Any>>) {
        pairs.forEach {
            put(it.first, it.second)
        }
    }

    fun contains(key: String): Boolean {
        return store.contains(key)
    }

    fun compute(key: String, remappingFunction: (String, Any?) -> Any?): Any? {
        val oldValue = store[key]
        val newValue = store.compute(key, remappingFunction)
        automat.listeners().forEach { it.extendedStateChanged(key, oldValue, newValue) }
        return newValue
    }

    fun remove(key: String): Any? {
        val prevValue = store.remove(key)
        automat.listeners().forEach { it.extendedStateChanged(key, prevValue, null) }
        return prevValue
    }
}