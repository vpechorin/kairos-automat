package net.pechorina.kairos.automat

import net.pechorina.kairos.automat.builder.AutomatBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicBoolean

enum class States {
    S1, S2, S3, S4, S5
}

enum class Events {
    E1, E2, E3, E4
}

class EnumAutomatTest {

    @Test
    fun testSimpleStateSwitch() {
        val configurer = AutomatBuilder<States, Events>()
                .withConfig()
                .enableLogging()

        configurer
                .configureStates()
                .initial(States.S1)
                .states(States.values().toList())
                .end(States.S5)

        configurer.configureTransitions()
                .withExternal()
                .event(Events.E1).source(States.S1).target(States.S2)
                .and()
                .withExternal()
                .event(Events.E2).source(States.S2).target(States.S3)
                .and()
                .withExternal()
                .event(Events.E3).source(States.S3).target(States.S4)
                .and()
                .withExternal()
                .event(Events.E4).source(States.S4).target(States.S5)

        val a = configurer.build()

        assertThat(a.isRunning()).isFalse()
        a.start()
        assertThat(a.isRunning()).isTrue()

        Events.values().forEach {
            assertThat(a.isRunning()).isTrue()
            a.sendEvent(it)
        }

        assertThat(a.isRunning()).isFalse()

        assertThat(a.currentState()).isEqualTo(States.S5)
    }

    @Test
    fun testTransitionAction() {
        val configurer = AutomatBuilder<String, String>()
                .withConfig()
                .enableLogging()

        configurer
                .configureStates()
                .initial("A")
                .end("B")

        val switch = AtomicBoolean(false)

        configurer.configureTransitions()
                .withExternal()
                .event("E1").source("A").target("B").action(
                        { _, a -> switch.set(true) }
                )

        val a = configurer.build()

        a.start()

        a.sendEvent("E1")

        assertThat(a.isRunning()).isFalse()
        assertThat(a.currentState()).isEqualTo("B")
        assertThat(switch.get()).isTrue()
    }

    @Test
    fun testEntryAction() {
        val configurer = AutomatBuilder<String, String>()
                .withConfig()
                .enableLogging()

        val switch = AtomicBoolean(false)

        configurer
                .configureStates()
                .initial("A")
                .end("B", { _, a -> switch.set(true) })

        configurer.configureTransitions()
                .withExternal()
                .event("E1").source("A").target("B")

        val a = configurer.build()

        a.start()

        a.sendEvent("E1")

        assertThat(a.isRunning()).isFalse()
        assertThat(a.currentState()).isEqualTo("B")
        assertThat(switch.get()).isTrue()
    }

    @Test
    fun testExitAction() {
        val configurer = AutomatBuilder<String, String>()
                .withConfig()
                .enableLogging()

        val switch = AtomicBoolean(false)

        configurer
                .configureStates()
                .initial("A", { _, a -> switch.set(true) })
                .end("B")

        configurer.configureTransitions()
                .withExternal()
                .event("E1").source("A").target("B")

        val a = configurer.build()

        a.start()

        a.sendEvent("E1")

        assertThat(a.isRunning()).isFalse()
        assertThat(a.currentState()).isEqualTo("B")
        assertThat(switch.get()).isTrue()
    }

    @Test
    fun testExtendedState() {
        val configurer = AutomatBuilder<String, String>()
                .withConfig()
                .enableLogging()

        configurer
                .configureStates()
                .initial("A")
                .stateEntry("B", { t, a -> a.extendedState().put("k", "b") })
                .end(
                        "C",
                        { t, a ->
                            a.extendedState().compute(
                                    "k",
                                    { k, oldValue -> "${oldValue}c" }
                            )
                        }
                )

        configurer.configureTransitions()
                .withExternal()
                .event("E1").source("A").target("B")
                .and()
                .withExternal()
                .event("E2").source("B").target("C")

        val a = configurer.build()

        a.start()

        a.sendEvent("E1")
        a.sendEvent("E2")

        assertThat(a.isRunning()).isFalse()
        assertThat(a.currentState()).isEqualTo("C")
        assertThat(a.extendedState().get("action")).isNull()
        assertThat(a.extendedState().get("k")).isEqualTo("bc")
    }

    @Test
    fun testParentState() {
        val configurer = AutomatBuilder<String, String>()
                .withConfig()
                .enableLogging()

        configurer
                .configureStates()
                .initial("A")
                .state("B")
                .state("B1", "B")
                .end("C")

        configurer.configureTransitions()
                .withExternal()
                .event("E1").source("A").target("B")
                .and()
                .withExternal()
                .event("E2").source("B").target("B1")
                .and()
                .withExternal()
                .event("E3").source("B").target("C")

        val a = configurer.build()

        a.start()

        a.sendEvent("E1")
        assertThat(a.currentState()).isEqualTo("B")
        a.sendEvent("E2")
        assertThat(a.currentState()).isEqualTo("B1")
        a.sendEvent("E3")

        assertThat(a.isRunning()).isFalse()
        assertThat(a.currentState()).isEqualTo("C")
    }
}