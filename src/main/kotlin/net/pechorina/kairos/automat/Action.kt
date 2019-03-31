package net.pechorina.kairos.automat

typealias Action<S, E> = (transition: Transition<S, E>, automat: Automat<S, E>) -> Unit
