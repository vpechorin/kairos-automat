package net.pechorina.kairos.automat.builder

import net.pechorina.kairos.automat.Action
import net.pechorina.kairos.automat.PayloadEvent
import net.pechorina.kairos.automat.State
import net.pechorina.kairos.automat.Transition

data class ExternalTransitionConfig<S, E>(
        internal val transitionConfigurer: TransitionConfigurer<S, E>,
        var source: S? = null,
        var target: S? = null,
        var event: E? = null,
        var action: Action<S, E>? = null) : TransitionConfig<S, E> {

    fun event(event: E): ExternalTransitionConfig<S, E> {
        this.event = event
        return this
    }

    fun source(state: S): ExternalTransitionConfig<S, E> {
        this.source = state
        return this
    }

    fun target(state: S): ExternalTransitionConfig<S, E> {
        this.target = state
        return this
    }

    fun action(action: Action<S, E>): ExternalTransitionConfig<S, E> {
        this.action = action
        return this
    }

    override fun asTransition(stateFinder: (payload: S) -> State<S, E>): Transition<S, E> {
        if (source == null) throw RuntimeException("Source is not defined for the transition config ${this}")
        if (target == null) throw RuntimeException("Target is not defined for the transition config ${this}")
        if (event == null) throw RuntimeException("Event is not defined for the transition config ${this}")

        val sourceState = stateFinder(source!!)
        val targetState = stateFinder(target!!)
        val transitionEvent = PayloadEvent(event!!)

        return Transition(sourceState, targetState, transitionEvent, action)
    }

    override fun and(): TransitionConfigurer<S, E> {
        return transitionConfigurer
    }
}