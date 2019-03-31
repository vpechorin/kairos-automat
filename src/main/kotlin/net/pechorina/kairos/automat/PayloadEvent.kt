package net.pechorina.kairos.automat

data class PayloadEvent<E>(val payload: E): Event<E>