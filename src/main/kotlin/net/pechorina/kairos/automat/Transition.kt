package net.pechorina.kairos.automat

data class Transition<S, E>(val source: State<S, E>, val target: State<S, E>, val event: Event<E>, val action: Action<S, E>? = null) {
    fun transit(automat: Automat<S, E>): State<S, E> {

        source.onExit?.let {
            it(this, automat)
        }

        automat.listeners().forEach { it.stateExited(source) }

        target.onEntry?.let {
            it(this, automat)
        }

        automat.listeners().forEach { it.stateEntered(target) }
        automat.listeners().forEach { it.stateChanged(source, target) }

        action?.invoke(this, automat)

        automat.listeners().forEach { it.transition(this) }

        return target
    }
}