package net.pechorina.kairos.automat.builder

import net.pechorina.kairos.automat.State
import net.pechorina.kairos.automat.Transition

interface TransitionConfig<S, E> {
    fun asTransition(stateFinder: (payload: S) -> State<S, E>): Transition<S, E>
    fun and(): TransitionConfigurer<S, E>
}

