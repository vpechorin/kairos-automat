# Kairos Automat
==== 
[![Build Status](https://travis-ci.com/vpechorin/kairos-automat.svg?branch=master)](https://travis-ci.com/vpechorin/kairos-automat) [![codecov](https://codecov.io/gh/vpechorin/kairos-automat/branch/master/graph/badge.svg)](https://codecov.io/gh/vpechorin/kairos-automat)

Kairos Automat is a finite-state machine (FSM) library

Kairos Automat aims to provide following features:
 - Easy to use flat one level state machine for simple use cases
 - Hierarchical state machine structure to ease complex state configuration
 - Builder pattern for easy instantiation
 - State entry/exit actions
 - Extended state
 - Transition actions
 - State machine event listeners

## Getting Started

```
enum class States {
    S1, S2, S3
}

enum class Events {
    E1, E2
}

val configurer = AutomatBuilder<States, Events>()
        .withConfig()
        .enableLogging()

configurer
        .configureStates()
            .initial(States.S1)
            .states(States.values().toList())
            .end(States.S3)
            
configurer
    .configureTransitions()
        .withExternal()
            .event(Events.E1).source(States.S1).target(States.S2)
            .and()
        .withExternal()
            .event(Events.E2).source(States.S2).target(States.S3)

val machine = configurer.build()

machine.start()

a.sendEvent(Events.E1)
a.sendEvent(Events.E2)

machine.stop()

```


### Installing

#### Maven
```
<dependency>
  <groupId>net.pechorina.kairos.automat</groupId>
  <artifactId>kairos-automat</artifactId>
  <version>0.1.0</version>
</dependency>
```


#### Gradle
```
compile 'net.pechorina.kairos.automat:kairo-automat:0.1.0'
```

## Built With

* [gradle](https://gradle.org/) - Dependency Management
* [kotlin](https://kotlinlang.org/) - Programming language for JVM
* [kotlin-logging](https://github.com/MicroUtils/kotlin-logging) - A convenient and performant logging library wrapping slf4

## Contributing

Pull requests are welcome!

## Authors

* **Victor Pechorin** - *Initial work* - [GitHub](https://github.com/vpechorin)

## License

This project is licensed under the **Apache License Version 2.0** - see the [LICENSE](LICENSE) file for details

## Acknowledgments

* [Spring Statemachine](https://projects.spring.io/spring-statemachine/) for providing inspiration

