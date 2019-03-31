package net.pechorina.kairos.automat.builder

import net.pechorina.kairos.automat.Action
import net.pechorina.kairos.automat.State

data class StateConfig<S, E>(
        val state: S? = null,
        var parent: S? = null,
        var initialState: Boolean = false,
        var finalState: Boolean = false,
        var entryAction: Action<S, E>? = null,
        var exitAction: Action<S, E>? = null) {

    fun asState(): State<S, E> {
        if (state == null) throw RuntimeException("State state is not defined")

        return State(
                value = state,
                onEntry = entryAction,
                onExit = exitAction,
                initialState = initialState,
                finalState = finalState
        )
    }

    fun hasParent(): Boolean {
        return parent != null
    }

    fun getChildParentPair(stateFinder: (state: S) -> State<S, E>): Pair<State<S, E>, State<S, E>> {
        if (parent == null) throw RuntimeException("No parent defined")
        if (state == null) throw RuntimeException("No state defined")

        val child: State<S, E> = stateFinder(state)
        val parent: State<S, E> = stateFinder(parent!!)
        return Pair(child, parent)
    }

}