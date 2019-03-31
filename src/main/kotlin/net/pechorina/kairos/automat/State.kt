package net.pechorina.kairos.automat

class State<S, E>(
        val value: S,
        var initialState: Boolean = false,
        var finalState: Boolean = false,
        var onEntry: Action<S, E>? = null,
        var onExit: Action<S, E>? = null,
        var parent: State<S, E>? = null,
        var transitions: List<Transition<S, E>> = listOf()
) {

    fun findTransition(event: Event<E>): Transition<S, E>? {
        val t = transitions.find {
            it.event == event
        }

        val transitionNotFound = t == null
        val hasParent = parent != null

        return if (transitionNotFound && hasParent) {
            parent!!.findTransition(event)
        } else t
    }

    fun addTransition(transition: Transition<S, E>): State<S, E> {
        this.transitions = this.transitions + transition
        return this
    }

    override fun toString(): String {
        return "State($value)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as State<*, *>

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }
}
