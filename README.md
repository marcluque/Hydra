# Hydra

Hydra is a network framework built upon Netty.

## Description

Hydra is built upon Netty. It is supposed to simplify the process of socket setup in Java. Netty allows high performances and good maintainability.
And here comes Hydra in. Hydra uses the builder-pattern in order to make the process of socket setup even simpler. Hydra comes with a handy packet system
that allows you to easily create your own packets and send them via the session Hydra creates for you. Furthermore you have the ability to create packet
listener that just need simple annotation and will be invoked by Hydra when a packet is received.
Convince yourself by taking a look at the [client]() and [server]() examples.

## Installing

 * Install Maven 3
 * Clone/Download this repo
 * Install it with: mvn clean install

# Maven local dependency

```xml
<dependency>
    <groupId>de.datasec</groupId>
    <artifactId>hydra</artifactId>
    <version>1.1.2-SNAPSHOT</version>
</dependency>
```

# Maven central

__It's planned to push this project into the maven central.__


_If you don't use maven you can download a release version and include it in your project._

## Examples

# Client

```java
Session session = new HydraClient.Builder("localhost", 8888, new SampleProtocol())
                .workerThreads(4)
                .option(StandardSocketOptions.TCP_NODELAY, true)
                .build();
```

This is an easy to understand example of how to create a client socket.
In order to make the packet system work, you have to register your created packets and listeners.
For detailed information on how to do that and examples see the [client example]()

# Server

```java
Session session = new HydraServer.Builder("localhost", 8888, new SampleProtocol())
                .bossThreads(2)
                .workerThreads(4)
                .option(StandardSocketOptions.TCP_NODELAY, true)
                .build();
```

This here is an example of how to create a server socket.
In order to make the packet system work, you have to register your created packets and listeners.
For detailed information on how to do that and examples see the [server example]()

## License

Licensed under the GNU General Public License, Version 3.0 - see the [LICENSE](LICENSE) file for details