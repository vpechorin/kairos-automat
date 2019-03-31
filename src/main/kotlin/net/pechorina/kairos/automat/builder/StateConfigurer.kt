package net.pechorina.kairos.automat.builder

import net.pechorina.kairos.automat.Action

class StateConfigurer<S, E> {
    internal var states: MutableList<StateConfig<S, E>> = arrayListOf()

    private fun findState(state: S): StateConfig<S, E>? {
        return this.states.find { it.state == state }
    }

    fun initial(state: S): StateConfigurer<S, E> {
        val existingState = findState(state)
        if (existingState != null) {
            existingState.initialState = true
        } else {
            states.add(
                    StateConfig(state = state, initialState = true)
            )
        }
        return this
    }

    fun initial(state: S, exitAction: Action<S, E>): StateConfigurer<S, E> {
        val existingState = findState(state)
        existingState?.let {
            it.initialState = true
            it.exitAction = exitAction
        }

        if (existingState == null) {
            states.add(
                    StateConfig(state = state, initialState = true, exitAction = exitAction)
            )
        }

        return this
    }

    fun end(state: S): StateConfigurer<S, E> {
        val existingState = findState(state)
        existingState?.let {
            it.finalState = true
        }

        if (existingState == null) {
            states.add(StateConfig(state = state, finalState = true))
        }

        return this
    }

    fun end(state: S, entryAction: Action<S, E>): StateConfigurer<S, E> {
        val existingState = findState(state)
        existingState?.let {
            it.finalState = true
            it.entryAction = entryAction
        }

        if (existingState == null) {
            states.add(StateConfig(state = state, finalState = true, entryAction = entryAction))
        }

        return this
    }

    fun state(state: S): StateConfigurer<S, E> {
        val existingState = findState(state)
        if (existingState == null) {
            this.states.add(StateConfig(state))
        }
        return this
    }

    fun state(state: S, parent: S): StateConfigurer<S, E> {
        val existingState = findState(state)
        existingState?.let {
            it.parent = parent
        }
        if (existingState == null)
            this.states.add(StateConfig(state = state, parent = parent))
        return this
    }

    fun state(state: S, parent: S, entryAction: Action<S, E>, exitAction: Action<S, E>): StateConfigurer<S, E> {
        val existingState = findState(state)
        existingState?.let {
            it.parent = parent
            it.entryAction = entryAction
            it.exitAction = exitAction
        }

        if (existingState == null)
            this.states.add(
                    StateConfig(
                            state = state,
                            parent = parent,
                            entryAction = entryAction,
                            exitAction = exitAction)
            )
        return this
    }

    fun state(state: S, entryAction: Action<S, E>, exitAction: Action<S, E>): StateConfigurer<S, E> {
        val existingState = findState(state)
        existingState?.let {
            it.entryAction = entryAction
            it.exitAction = exitAction
        }
        if (existingState == null)
            this.states.add(
                    StateConfig(
                            state = state,
                            entryAction = entryAction,
                            exitAction = exitAction)
            )
        return this
    }

    fun stateEntry(state: S, entryAction: Action<S, E>): StateConfigurer<S, E> {
        val existingState = findState(state)
        existingState?.let {
            it.entryAction = entryAction
        }
        if (existingState == null)
            this.states.add(
                    StateConfig(
                            state = state,
                            entryAction = entryAction
                    )
            )
        return this
    }

    fun stateExit(state: S, exitAction: Action<S, E>): StateConfigurer<S, E> {
        val existingState = findState(state)
        existingState?.let {
            it.exitAction = exitAction
        }
        if (existingState == null)
            this.states.add(
                    StateConfig(
                            state = state,
                            exitAction = exitAction
                    )
            )
        return this
    }

    private fun stateExists(state: S): Boolean {
        return findState(state) != null
    }

    fun states(states: Collection<S>): StateConfigurer<S, E> {
        val statesToAdd = states.filterNot { stateExists(it) }
                .map { StateConfig<S, E>(state = it) }

        this.states.addAll(statesToAdd)
        return this
    }
}