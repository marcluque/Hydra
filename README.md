![hydra banner](http://hydra.datasecs.de/images/hydra_banner.png)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.datasecs/hydra/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.datasecs/hydra-all)
[![BCH compliance](https://bettercodehub.com/edge/badge/DataSecs/Hydra?branch=master)](https://datasecs.de)
[![Build Status](https://travis-ci.com/DataSecs/Hydra.svg?branch=master)](https://travis-ci.com/DataSecs/Hydra)
[![Coverage Status](https://coveralls.io/repos/github/DataSecs/Hydra/badge.svg?branch=master)](https://coveralls.io/github/DataSecs/Hydra?branch=master)
[![License](https://img.shields.io/badge/License-BSD%202--Clause-orange.svg)](https://opensource.org/licenses/BSD-2-Clause)

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

## Maven

### Local Maven dependency

```xml
<dependency>
    <groupId>de.datasecs</groupId>
    <artifactId>hydra-all</artifactId>
    <version>1.6.3</version>
</dependency>
```

### Maven central

```xml
<dependency>
    <groupId>de.datasecs</groupId>
    <artifactId>hydra-all</artifactId>
    <version>1.6.0</version>
</dependency>
```

If you would like to just have the client or server, use `hydra-client` or `hydra-server` instead of `hydra-all` as artifact id.

_And if you don't use maven you can download a [release](https://github.com/DataSecs/Hydra/releases) version and include it in your project the way you prefer._

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

# Javadoc

The javadoc is always up-to-date and can be found on [hydra.datasecs.de/javadoc](http://hydra.datasecs.de/javadoc)

# License

Licensed under the BSD 2-Clause License - see the [LICENSE](LICENSE) file for details.

### Credits
Special thanks go to [Aadi Bajpai](https://github.com/aadibajpai) for creating the awesome banner and logo!
