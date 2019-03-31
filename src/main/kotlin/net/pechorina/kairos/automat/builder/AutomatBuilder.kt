package net.pechorina.kairos.automat.builder

import net.pechorina.kairos.automat.Automat
import net.pechorina.kairos.automat.State
import net.pechorina.kairos.automat.SyncAutomat
import net.pechorina.kairos.automat.listeners.LoggingListener

class AutomatBuilder<S, E> {
    private var configurer: AutomatConfigurer<S, E> = AutomatConfigurer(this)

    private val states: MutableSet<State<S, E>> = mutableSetOf()
    private val stateFinder = { payload: S -> findState(payload) }

    fun withConfig(): AutomatConfigurer<S, E> {
        return configurer
    }

    fun build(): Automat<S, E> {

        states.addAll(
                configurer.getStates()
        )

        configurer.getChildParentPairs(stateFinder).forEach {
            val childState: State<S, E> = it.first
            val parentState: State<S, E> = it.second
            childState.parent = parentState
        }

        configurer.getTransitions(stateFinder).forEach {
            val sourceState: State<S, E> = it.source
            val existingTransitions = sourceState.transitions
            sourceState.transitions = existingTransitions + it
        }

        if (!hasInitialState()) throw RuntimeException("Initial state is not defined")

        val machine: Automat<S, E> = SyncAutomat(states)

        if (configurer.logging) machine.addListener(LoggingListener())

        return machine
    }

    private fun findState(payload: S): State<S, E> {
        return states.find { it.value == payload } ?: throw RuntimeException("State[$payload] not found")
    }

    private fun hasInitialState(): Boolean {
        return this.states.any { it.initialState }
    }
}