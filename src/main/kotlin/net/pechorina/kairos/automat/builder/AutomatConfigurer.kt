package net.pechorina.kairos.automat.builder

import net.pechorina.kairos.automat.Automat
import net.pechorina.kairos.automat.State
import net.pechorina.kairos.automat.Transition
import java.util.UUID

class AutomatConfigurer<S, E>(val builder: AutomatBuilder<S, E>) {
    internal var logging: Boolean = false
    internal var id: String = UUID.randomUUID().toString()
    internal var name: String? = null
    private val stateConfigurer = StateConfigurer<S, E>()
    private val transitionConfigurer = TransitionConfigurer<S, E>()

    fun enableLogging(): AutomatConfigurer<S, E> {
        this.logging = true
        return this
    }

    fun withId(id: String): AutomatConfigurer<S, E> {
        this.id = id
        return this
    }

    fun withName(name: String): AutomatConfigurer<S, E> {
        this.name = name
        return this
    }

    fun configureStates(): StateConfigurer<S, E> {
        return stateConfigurer
    }

    fun configureTransitions(): TransitionConfigurer<S, E> {
        return transitionConfigurer
    }

    fun getStateConfigs(): Set<StateConfig<S, E>> {
        return stateConfigurer.states.toSet()
    }

    fun getStates(): Set<State<S, E>> {
        return getStateConfigs().map { it.asState() }.toSet()
    }

    fun getTransitions(stateFinder: (state: S) -> State<S, E>): Set<Transition<S, E>> {
        return transitionConfigurer.transitionConfigs
                .map { it.asTransition(stateFinder) }
                .toSet()
    }

    fun getChildParentPairs(stateFinder: (state: S) -> State<S, E>): Set<Pair<State<S, E>, State<S, E>>> {
        return getStateConfigs().filter { it.hasParent() }
                .map { it.getChildParentPair(stateFinder) }
                .toSet()
    }

    fun build(): Automat<S, E> {
        return this.builder.build()
    }

}