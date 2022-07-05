![default-cropped](https://user-images.githubusercontent.com/33261455/177036138-fef7d692-fd8a-470f-af8a-6808eed476d8.svg)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.marcluque/hydra/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.marcluque/hydra-all)
[![BCH compliance](https://bettercodehub.com/edge/badge/marcluque/Hydra?branch=master)](https://bettercodehub.com/)
[![Build Status](https://travis-ci.com/marcluque/Hydra.svg?branch=master)](https://travis-ci.com/marcluque/Hydra)  

[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-white.svg)](https://sonarcloud.io/summary/new_code?id=marcluque_Hydra)  
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=marcluque_Hydra&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=marcluque_Hydra)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=marcluque_Hydra&metric=coverage)](https://sonarcloud.io/summary/new_code?id=marcluque_Hydra)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=marcluque_Hydra&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=marcluque_Hydra)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=marcluque_Hydra&metric=bugs)](https://sonarcloud.io/summary/new_code?id=marcluque_Hydra)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=marcluque_Hydra&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=marcluque_Hydra)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=marcluque_Hydra&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=marcluque_Hydra)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=marcluque_Hydra&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=marcluque_Hydra)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=marcluque_Hydra&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=marcluque_Hydra)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=marcluque_Hydra&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=marcluque_Hydra)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=marcluque_Hydra&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=marcluque_Hydra)

[![License](https://img.shields.io/badge/License-BSD%202--Clause-orange.svg)](https://opensource.org/licenses/BSD-2-Clause)

## Description

Hydra is built upon Netty. It is supposed to simplify the process of socket setup in Java. Netty allows high performances and good maintainability of programs built upon it.
And here comes Hydra in. Hydra uses the builder-pattern in order to make the process of socket setup even simpler. It comes with a handy packet system
that allows you to easily create your own packets and send them via the session Hydra creates for you. Furthermore, you have the ability to create packets and
listener which just need a simple annotation and will be invoked by Hydra when a packet is received.
Convince yourself by taking a look at the [client](https://github.com/marcluque/Hydra/tree/master/example/src/main/java/de/marcluque/hydra/example/client) and [server](https://github.com/marcluque/Hydra/tree/master/example/src/main/java/de/marcluque/hydra/example/server) examples.

## Wiki

In case you would like to have an in-depth introduction to Hydra, please take a look at the [wiki](https://github.com/marcluque/Hydra/wiki).
The wiki takes you step-by-step through the setup of a server and a client. Furthermore, the wiki features example usages,
like a [simple chat application](https://github.com/marcluque/Hydra/wiki/Building-a-simple-chat-application) and a [key-value store](https://github.com/marcluque/Hydra/wiki/Building-a-small-key-value-store).

# Quantitative benefits over netty
### Netty code for server setup:
```java
EventLoopGroup bossGroup = new NioEventLoopGroup(1);
EventLoopGroup workerGroup = new NioEventLoopGroup();
try {
    ServerBootstrap b = new ServerBootstrap();
    b.group(bossGroup, workerGroup)
     .channel(NioServerSocketChannel.class)
     .childHandler(new ChannelInitializer<SocketChannel>() {
         @Override
         public void initChannel(SocketChannel ch) throws Exception {
             ch.pipeline().addLast(new ServerHandler());
         }
     })
     .option(ChannelOption.SO_BACKLOG, 128);
     .childOption(ChannelOption.SO_KEEPALIVE, true);
     
    ChannelFuture f = b.bind("localhost", 8888).sync();
    f.channel().closeFuture().sync();
} finally {
    workerGroup.shutdownGracefully();
    bossGroup.shutdownGracefully();
}
```
### Hydra code for server setup:
```java
HydraServer server = new Server.Builder("localhost", 8888, new SampleProtocol())
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .build();
```

Note that `ServerHandler` and `SampleProtocol` have approximately the same size/complexity.

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

This is an easy-to-understand example of how to create a client socket.
In order to make the packet system work, you have to register your created packets and listeners.
For detailed information on how to do that and examples see the [client example](https://github.com/marcluque/Hydra/tree/master/example/src/main/java/de/marcluque/hydra/example/client).

## Server

```java
HydraServer server = new Server.Builder("localhost", 8888, new SampleProtocol())
                .bossThreads(2)
                .workerThreads(4)
                .option(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .build();
```

This here is an example of how to create a server socket.
In order to make the packet system work, you have to register your created packets and listeners.
For detailed information on how to do that and examples see the [server example](https://github.com/marcluque/Hydra/tree/master/example/src/main/java/de/marcluque/hydra/example/server).

# Future features
- [ ] Log4J configuration
- [ ] SSL support

# Javadoc

The javadoc is always up-to-date and can be found on [marcluque.com/hydra/javadoc/](https://marcluque.com/hydra/javadoc/)

# License

Licensed under the BSD 2-Clause License - see the [LICENSE](LICENSE) file for details.
