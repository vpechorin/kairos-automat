package net.pechorina.kairos.automat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class StateUnitTest {

    @Test
    fun testNoNextState() {
        val state = State<String, String>(value = "A")
        val event = StringEvent("event")
        assertThat(state.findTransition(event)).isNull()
    }

    @Test
    fun testFindTransition() {
        val e1: Event<String> = StringEvent("e1")

        val state1 = State<String, String>(value = "A")
        val state2 = State<String, String>(value = "B")
        val transition = Transition(state1, state2, e1)
        state1.addTransition(transition)
        val t = state1.findTransition(e1)
        assertThat(t).isNotNull
        assertThat(t!!.target).isEqualTo(state2)
    }

    @Test
    fun testEquals() {
        val state1 = State<String, String>(value = "A")
        val state2 = State<String, String>(value = "A")
        assertThat(state1).isEqualTo(state2)
    }

    @Test
    fun toStringTest() {
        val state1 = State<String, String>(value = "A")
        assertThat(state1.toString()).isEqualTo("State(A)")
    }

    @Test
    fun hashCodeTest() {
        val state1 = State<String, String>(value = "A")
        val state2 = State<String, String>(value = "A")
        assertThat(state1.hashCode()).isEqualTo(state2.hashCode())
    }

}