package net.pechorina.kairos.automat.builder

class TransitionConfigurer<S, E> {

    internal var transitionConfigs: MutableList<TransitionConfig<S, E>> = arrayListOf()

    fun withExternal(): ExternalTransitionConfig<S, E> {
        val config = ExternalTransitionConfig(transitionConfigurer = this)
        this.transitionConfigs.add(config)
        return config
    }

    fun withTransition(vararg transitionConfig: TransitionConfig<S, E>): TransitionConfigurer<S, E> {
        transitionConfigs.addAll(transitionConfig)
        return this
    }

}