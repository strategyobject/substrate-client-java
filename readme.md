# About
This project provides written in Java API for interaction with a Polkadot or Substrate based network.

## Key features
- ### Java 8 or higher.
The main idea is to make this library available and usable for as many projects as possible.

- ### Non-blocking API.

The API is designed to be non-blocking in order to not engage resources for waiting responses from a node, especially when working with web sockets.
Each method returns `CompletableFuture<>`.

- ### Declarative approach.
Some of our goals are:
- make API simpler for usage;
- let users organize their code similar to the one in the pallet they are interacting with;
- hide difficulties of low level and infrastructure code;
- facilitate the future maintenance.

The best approach to reach project’s goals is to use annotations and code generation techniques. Below are listed some annotations that this API shall provide:
- [x] Scale
    - [x] `@ScaleWriter`;
    - [x] `@ScaleReader`;
    - [x] `@Scale`;
    - [x] `@ScaleGeneric`;
    - [x] `@Ignore`;

- [x] Rpc
    - [x] `@RpcInterface`;
    - [x] `@RpcCall`;
    - [x] `@RpcSubscription`;
    - [x] `@RpcEncoder`;
    - [x] `@RpcDecoder`;

- [ ] Pallet
    - [ ] `@Pallet`;
    - [ ] `@Transaction`;
    - [ ] `@Query`;
    - [ ] `@EventHandler`.

These allow the generation of scale serializers, deserializers, RPC methods, code for interaction with pallet, etc.
More examples you can find below.

- ### Deferred parametrization of codecs
Annotations for codecs allow deferring parameters of a generic until it's used at an RPC method. E.g.:
 ```java
@RequiredArgsConstructor
@Getter
@ScaleWriter
public class Some<A> {
    private final int number;
    private final A some;
}

@RpcInterface(section = "test")
public interface TestSection {
  @RpcCall(method = "sendSome")
  CompletableFuture<Boolean> doNothing(@Scale Some<Parameter> value);
}
```
Annotation processors will generate scale writer for the class `Some` which expects another writer as a dependency.
When a processor faces a parameter like `Some<String> value`, it injects the `Strings`'s writer into the writer of `Some`.

- ### GC Manageable requests.
We take care of either lost responses or canceled futures by not holding handlers that are needed to match an RPC request with a response.

- ### Tests run with substrate node.
All API methods related to the substrate node will be tested for operability and compatibility.
Currently we use [test containers](https://www.testcontainers.org/) and docker image [parity/substrate:v3.0.0](https://hub.docker.com/layers/parity/substrate/v3.0.0/images/sha256-1aef07509d757c584320773c476dcb6077578bbf2f5e468ceb413dcf908897f1?context=explore).

## Our vision of the API
### How to generate scale codec for DTO (implemented)
```java
@RequiredArgsConstructor
@Getter
@ScaleWriter
public class SignedExtra<E extends Era> implements Extra, SignedExtension {
    @Ignore
    private final long specVersion;
    @Ignore
    private final long txVersion;
    @Ignore
    private final BlockHash genesis;
    @Ignore
    private final BlockHash eraBlock;
    private final E era;
    @Scale(ScaleType.CompactBigInteger.class)
    private final BigInteger nonce;
    @Scale(ScaleType.CompactBigInteger.class)
    private final BigInteger tip;

    @Override
    public AdditionalExtra getAdditionalExtra() {
        return new SignedAdditionalExtra(specVersion, txVersion, genesis, eraBlock);
    }
}
```

### How to generate RPC interface (implemented)
```java
@RpcInterface(section = "author")
public interface Author {
    @RpcCall(method = "hasKey")
    CompletableFuture<Boolean> hasKey(@Scale PublicKey publicKey, String keyType);

    @RpcCall(method = "insertKey")
    CompletableFuture<Void> insertKey(String keyType, String secretUri, @Scale PublicKey publicKey);

    @RpcCall(method = "submitExtrinsic")
    @Scale
    CompletableFuture<Hash> submitExtrinsic(@Scale Extrinsic<?, ?, ?, ?> extrinsic);

    @RpcSubscription(type = "extrinsicUpdate", subscribeMethod = "submitAndWatchExtrinsic", unsubscribeMethod = "unwatchExtrinsic")
    CompletableFuture<Supplier<CompletableFuture<Boolean>>> submitAndWatchExtrinsic(@Scale Extrinsic<?, ?, ?, ?> extrinsic,
                                                                                    BiConsumer<Exception, ExtrinsicStatus> callback);
}
```

### Create instance of API (TBD)
```java
Api api = Api.builder()
        .useWs()
        .withNodes("127.0.0.1:9944", "127.0.0.2:9944")
        .scanAnnotatedFrom("com.my_company.first", "com.my_company.second")
        .build();
```

### RPC: call method (implemented but not integrated into the API)
```java
CompletableFuture<RuntimeVersion> versionFuture = api.getRpc()
        .getState()
        .getRuntimeVersion();
```

### RPC: subscribe (implemented but not integrated into the API)
```java
CompletableFuture<?> unsubscribe = api.getRpc()
        .getChain()
        .subscribeNewHeads((ex, header) -> { print(header); });
```

### Pallet: transaction (TBD)
```java
api.pallete(MyPallet.class)
        .myExtrinsic(someValue)
        .signAndSend(KEY_PAIR);
```

## To be implemented
- [x] Transport - layer that interacts with a node. It provides async API for RPC requests.
- [ ] Scale
    - [x] Scale codec - implementation of the [SCALE](https://docs.substrate.io/v3/advanced/scale-codec/) for standard types.
    - [x] Scale code generation - approach to generate scale encoders/decoders for annotated classes.
- [ ] Signing:
    - [x] SR25519
    - [ ] ED25519
- [x] RPC code generation
    - [x] RPC interfaces with methods
    - [x] RPC encoders/decoders
    - [ ] Declare known RPC sections and methods.
- [ ] Handling metadata
- [ ] Pallet API
    - [ ] Transactions
    - [ ] Queries
    - [ ] Constants
    - [ ] Events
- [ ] Load balancing