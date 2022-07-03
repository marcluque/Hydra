![default_cropped-cropped](https://user-images.githubusercontent.com/33261455/177019658-1cec9876-022c-45b4-82f5-3a90aedcb4b1.svg)



[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.marcluque/hydra/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.marcluque/hydra-all)
[![BCH compliance](https://bettercodehub.com/edge/badge/marcluque/Hydra?branch=master)](https://marcluque.de)
[![Build Status](https://travis-ci.com/marcluque/Hydra.svg?branch=master)](https://travis-ci.com/marcluque/Hydra)
[![Coverage Status](https://coveralls.io/repos/github/marcluque/Hydra/badge.svg?branch=master)](https://coveralls.io/github/marcluque/Hydra?branch=master)
[![License](https://img.shields.io/badge/License-BSD%202--Clause-orange.svg)](https://opensource.org/licenses/BSD-2-Clause)

## Description

Hydra is built upon Netty. It is supposed to simplify the process of socket setup in Java. Netty allows high performances and good maintainability of programs built upon it.
And here comes Hydra in. Hydra uses the builder-pattern in order to make the process of socket setup even simpler. It comes with a handy packet system
that allows you to easily create your own packets and send them via the session Hydra creates for you. Furthermore, you have the ability to create packets and
listener which just need a simple annotation and will be invoked by Hydra when a packet is received.
Convince yourself by taking a look at the [client](https://github.com/marcluque/Hydra/tree/master/example/src/main/java/de/marcluque/hydra/example/client) and [server](https://github.com/marcluque/Hydra/tree/master/example/src/main/java/de/marcluque/hydra/example/server) examples.

## Wiki

In case you would like to have an in-depth introduction to Hydra, please take a look at the [wiki](https://github.com/marcluque/Hydra/wiki).
The wiki takes you step-by-step through the setup of a server and a client. Furthermore the wiki features example usages,
like a [simple chat application](https://github.com/marcluque/Hydra/wiki/Building-a-simple-chat-application) and a [key-value store](https://github.com/marcluque/Hydra/wiki/Building-a-small-key-value-store).

# Quantitative benefits over raw netty
### Raw netty code for server setup:
![original netty code](http://hydra.marcluque.de/images/original-netty-server-code_comparsion.png)
### Hydra code for server setup:
![hydra code](http://hydra.marcluque.de/images/hydra-code_comparsion.png)

# Installing

 * Install Maven 3
 * Clone/Download this repo
 * Install it with: ```mvn -U -Dmaven.test.skip=true clean compile install```

## Maven

### Local Maven dependency

```xml
<dependency>
    <groupId>com.marcluque</groupId>
    <artifactId>hydra-all</artifactId>
    <version>1.6.5</version>
</dependency>
```

### Maven central

```xml
<dependency>
    <groupId>com.marcluque</groupId>
    <artifactId>hydra-all</artifactId>
    <version>1.6.5</version>
</dependency>
```

If you would like to just have the client or server, use `hydra-client` or `hydra-server` instead of `hydra-all` as artifact id.

_And if you don't use maven you can download a [release](https://github.com/marcluque/Hydra/releases) version and include it in your project the way you prefer._

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
For detailed information on how to do that and examples see the [client example](https://github.com/marcluque/Hydra/tree/master/example/src/main/java/de/marcluque/hydra/example/client).

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
For detailed information on how to do that and examples see the [server example](https://github.com/marcluque/Hydra/tree/master/example/src/main/java/de/marcluque/hydra/example/server).

# Javadoc

The javadoc is always up-to-date and can be found on [hydra.marcluque.de/javadoc](http://hydra.marcluque.de/javadoc)

# License

Licensed under the BSD 2-Clause License - see the [LICENSE](LICENSE) file for details.

### Credits
Special thanks go to [Aadi Bajpai](https://github.com/aadibajpai) for creating the awesome banner and logo!
