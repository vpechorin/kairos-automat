package net.pechorina.kairos.automat.listeners

import mu.KotlinLogging
import net.pechorina.kairos.automat.Automat
import net.pechorina.kairos.automat.Event
import net.pechorina.kairos.automat.State
import net.pechorina.kairos.automat.Transition

class LoggingListener<S, E> : AutomatListener<S, E> {

    override fun newEvent(event: Event<E>) {
        log.debug { "New event: $event" }
    }

    override fun stateChanged(from: State<S, E>, to: State<S, E>) {
        log.debug { "State changed: $from --> $to" }
    }

    override fun stateEntered(state: State<S, E>) {
        log.debug { "State entered: $state" }
    }

    override fun stateExited(state: State<S, E>) {
        log.debug { "State exited: $state" }
    }

    override fun eventNotAccepted(event: Event<E>) {
        log.debug { "Event not accepted: $event" }
    }

    override fun transition(transition: Transition<S, E>) {
        log.debug { "Transition: $transition" }
    }

    override fun transitionStarted(transition: Transition<S, E>) {
        log.debug { "Transition started: $transition" }
    }

    override fun transitionEnded(transition: Transition<S, E>) {
        log.debug { "Transition ended: $transition" }
    }

    override fun stateMachineStarted(stateMachine: Automat<S, E>) {
        log.debug { "State machine started: $stateMachine" }
    }

    override fun stateMachineStopped(stateMachine: Automat<S, E>) {
        log.debug { "State machine stopped: $stateMachine" }
    }

    override fun stateMachineError(stateMachine: Automat<S, E>, exception: Exception) {
        log.error("State machine error: $stateMachine", exception)
    }

    override fun extendedStateChanged(key: String, oldValue: Any?, newValue: Any?) {
        log.debug { "Extended State changed: key:'$key', value:[$oldValue --> $newValue] " }
    }

    companion object {
        val log = KotlinLogging.logger("net.pechorina.kairos.automat.Automat")
    }

}