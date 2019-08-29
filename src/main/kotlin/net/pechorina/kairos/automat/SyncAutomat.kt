package net.pechorina.kairos.automat

import mu.KotlinLogging
import net.pechorina.kairos.automat.listeners.AutomatListener
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class SyncAutomat<S, E>(
        val states: Set<State<S, E>>,
        val id: String = UUID.randomUUID().toString(),
        val name: String? = null
) : Automat<S, E> {

    private val current: AtomicReference<State<S, E>> = AtomicReference()
    private val extendedState: ExtendedState<S, E>
    private val running: AtomicBoolean = AtomicBoolean(false)
    private val listeners: MutableList<AutomatListener<S, E>> = arrayListOf()
    private val initial: State<S, E>
    private val endStates: Set<State<S, E>>

    init {
        this.running.set(false)
        this.initial = states.first { it.initialState }
        this.endStates = states.filter { it.finalState }.toSet()
        this.current.set(initial)
        this.extendedState = ExtendedState(this)
    }

    override fun id(): String {
        return this.id
    }

    override fun name(): String? {
        return this.name
    }

    override fun start() {
        this.running.set(true)
        listeners.forEach { it.stateMachineStarted(this) }
    }

    override fun stop() {
        this.running.set(false)
        listeners.forEach { it.stateMachineStopped(this) }
    }

    override fun reset() {
        this.current.set(initial)
    }

    override fun isRunning(): Boolean {
        return this.running.get()
    }

    override fun currentState(): S {
        return this.current.get().value
    }

    override fun finalStates(): Set<S> {
        return this.endStates.map { it.value }.toSet()
    }

    override fun initialState(): S {
        return this.initial.value
    }

    override fun extendedState(): ExtendedState<S, E> {
        return this.extendedState
    }

    @Synchronized
    override fun sendEvent(event: E) {
        val payloadEvent = PayloadEvent(event)
        listeners().forEach { it.newEvent(payloadEvent) }

        if (!running.get()) {
            log.trace { "Machine is not running" }
            return
        }

        if (current.get().finalState) {
            log.trace { "Machine is in final state: ${currentState()}" }
            return
        }

        val transition = current.get().findTransition(payloadEvent)
        if (transition == null) {
            listeners.forEach { it.eventNotAccepted(payloadEvent) }
            return
        }

        listeners.forEach { it.transitionStarted(transition) }

        this.current.set(
                transition.transit(this)
        )

        listeners.forEach { it.transitionEnded(transition) }

        if (this.current.get().finalState) {
            log.trace { "Machine reached the final state: ${this.current.get()}}" }
            stop()
        }
    }

    override fun listeners(): List<AutomatListener<S, E>> {
        return listeners
    }

    override fun addListener(listener: AutomatListener<S, E>) {
        this.listeners.add(listener)
    }

    companion object {
        val log = KotlinLogging.logger {}
    }

}
