![hydra banner](http://hydra.datasecs.de/images/hydra_banner.png)

## Description

Hydra is built upon Netty. It is supposed to simplify the process of socket setup in Java. Netty allows high performances and good maintainability of programs built upon it.
And here comes Hydra in. Hydra uses the builder-pattern in order to make the process of socket setup even simpler. Hydra comes with a handy packet system
that allows you to easily create your own packets and send them via the session Hydra creates for you. Furthermore you have the ability to create packets and
listener which just need a simple annotation and will be invoked by Hydra when a packet is received.
Convince yourself by taking a look at the [client](https://github.com/DataSecs/Hydra/tree/master/client/src/test/java/client) and [server](https://github.com/DataSecs/Hydra/tree/master/server/src/test/java/server) examples.

## Wiki

In case you would like to have an in-depth introduction to Hydra, please take a look at the [wiki](https://github.com/DataSecs/Hydra/wiki).
The wiki takes you step-by-step through the setup of a server and a client. Furthermore the wiki features example usages,
like a [simple chat application](https://github.com/DataSecs/Hydra/wiki/Building-a-simple-chat-application) and a [key-value store](https://github.com/DataSecs/Hydra/wiki/Building-a-small-key-value-store).

# Quantitative benefits over raw netty
### Raw netty code for server setup:
![original netty code](http://hydra.datasecs.de/images/original-netty-server-code_comparsion.png)
### Hydra code for server setup:
![hydra code](http://hydra.datasecs.de/images/hydra-code_comparsion.png)

# Installing

 * Install Maven 3
 * Clone/Download this repo
 * Install it with: mvn clean install

### Maven local dependency

```xml
<dependency>
    <groupId>de.datasecs</groupId>
    <artifactId>hydra</artifactId>
    <version>1.4.3-SNAPSHOT</version>
</dependency>
```

### Maven central

__It is planned to push this project into the maven central.__


_If you don't use maven you can download a release version and include it in your project._

# Examples

## Client

```java
HydraClient client = new Client.Builder("localhost", 8888, new SampleProtocol())
                .workerThreads(4)
                .option(ChannelOption.TCP_NODELAY, true)
                .build();
```

This is an easy to understand example of how to create a client socket.
In order to make the packet system work, you have to register your created packets and listeners.
For detailed information on how to do that and examples see the [client example](https://github.com/DataSecs/Hydra/tree/master/client/src/test/java/client).

## Server

```java
HydraServer server = new Server.Builder("localhost", 8888, new SampleProtocol())
                .bossThreads(2)
                .workerThreads(4)
                .option(ChannelOption.TCP_NODELAY, true)
                .build();
```

This here is an example of how to create a server socket.
In order to make the packet system work, you have to register your created packets and listeners.
For detailed information on how to do that and examples see the [server example](https://github.com/DataSecs/Hydra/tree/master/server/src/test/java/server).

# License

Licensed under the GNU General Public License, Version 3.0 - see the [LICENSE](LICENSE) file for details.

### Credits
Special thanks go to [TheClashster](https://github.com/TheClashster) for creating the awesome banner and logo!