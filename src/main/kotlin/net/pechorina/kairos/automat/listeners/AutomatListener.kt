package net.pechorina.kairos.automat.listeners

import net.pechorina.kairos.automat.Automat
import net.pechorina.kairos.automat.Event
import net.pechorina.kairos.automat.State
import net.pechorina.kairos.automat.Transition

interface AutomatListener<S, E> {

    /**
     * Notified before the event will be processed
     */
    fun newEvent(event: Event<E>) {}

    /**
     * Notified when state is changed.
     *
     * @param from the source state
     * @param to   the target state
     */
    fun stateChanged(from: State<S, E>, to: State<S, E>) {}

    /**
     * Notified when state is entered.
     *
     * @param state the state
     */
    fun stateEntered(state: State<S, E>) {}

    /**
     * Notified when state is exited.
     *
     * @param state the state
     */
    fun stateExited(state: State<S, E>) {}

    /**
     * Notified when event was not accepted.
     *
     * @param event the event
     */
    fun eventNotAccepted(event: Event<E>) {}

    /**
     * Notified when transition happened.
     *
     * @param transition the transition
     */
    fun transition(transition: Transition<S, E>) {}

    /**
     * Notified when transition started.
     *
     * @param transition the transition
     */
    fun transitionStarted(transition: Transition<S, E>) {}

    /**
     * Notified when transition ended.
     *
     * @param transition the transition
     */
    fun transitionEnded(transition: Transition<S, E>) {}

    /**
     * Notified when statemachine starts
     *
     * @param stateMachine the statemachine
     */
    fun stateMachineStarted(stateMachine: Automat<S, E>) {}

    /**
     * Notified when statemachine stops
     *
     * @param stateMachine the statemachine
     */
    fun stateMachineStopped(stateMachine: Automat<S, E>) {}

    /**
     * Notified when statemachine enters error it can't recover from.
     *
     * @param stateMachine the state machine
     * @param exception    the exception
     */
    fun stateMachineError(stateMachine: Automat<S, E>, exception: Exception) {}

    /**
     * Notified when extended state variable is either added, modified or removed.
     *
     * @param key   the variable key
     * @param oldValue the old variable value
     * * @param newValue the new variable value
     */
    fun extendedStateChanged(key: String, oldValue: Any?, newValue: Any?) {}
}

