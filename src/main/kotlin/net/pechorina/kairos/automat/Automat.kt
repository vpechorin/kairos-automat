package net.pechorina.kairos.automat

import net.pechorina.kairos.automat.listeners.AutomatListener

interface Automat<S, E> {
    fun currentState(): S
    fun finalStates(): Set<S>
    fun initialState(): S

    fun extendedState(): ExtendedState<S, E>

    fun sendEvent(event: E)

    fun listeners(): List<AutomatListener<S, E>>
    fun addListener(listener: AutomatListener<S, E>)

    fun start()
    fun stop()
    fun reset()
    fun isRunning(): Boolean

    fun id(): String
    fun name(): String?
}