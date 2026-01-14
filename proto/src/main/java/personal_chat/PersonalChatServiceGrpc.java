package personal_chat;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.58.0)",
    comments = "Source: personal_chat.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class PersonalChatServiceGrpc {

  private PersonalChatServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "personal_chat.PersonalChatService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatRequest,
      personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatResponse> getValidateUserIsMemberOfPersonalChatMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateUserIsMemberOfPersonalChat",
      requestType = personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatRequest.class,
      responseType = personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatRequest,
      personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatResponse> getValidateUserIsMemberOfPersonalChatMethod() {
    io.grpc.MethodDescriptor<personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatRequest, personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatResponse> getValidateUserIsMemberOfPersonalChatMethod;
    if ((getValidateUserIsMemberOfPersonalChatMethod = PersonalChatServiceGrpc.getValidateUserIsMemberOfPersonalChatMethod) == null) {
      synchronized (PersonalChatServiceGrpc.class) {
        if ((getValidateUserIsMemberOfPersonalChatMethod = PersonalChatServiceGrpc.getValidateUserIsMemberOfPersonalChatMethod) == null) {
          PersonalChatServiceGrpc.getValidateUserIsMemberOfPersonalChatMethod = getValidateUserIsMemberOfPersonalChatMethod =
              io.grpc.MethodDescriptor.<personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatRequest, personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateUserIsMemberOfPersonalChat"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatResponse.getDefaultInstance()))
              .setSchemaDescriptor(new PersonalChatServiceMethodDescriptorSupplier("ValidateUserIsMemberOfPersonalChat"))
              .build();
        }
      }
    }
    return getValidateUserIsMemberOfPersonalChatMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PersonalChatServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PersonalChatServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PersonalChatServiceStub>() {
        @java.lang.Override
        public PersonalChatServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PersonalChatServiceStub(channel, callOptions);
        }
      };
    return PersonalChatServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PersonalChatServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PersonalChatServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PersonalChatServiceBlockingStub>() {
        @java.lang.Override
        public PersonalChatServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PersonalChatServiceBlockingStub(channel, callOptions);
        }
      };
    return PersonalChatServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PersonalChatServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<PersonalChatServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<PersonalChatServiceFutureStub>() {
        @java.lang.Override
        public PersonalChatServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new PersonalChatServiceFutureStub(channel, callOptions);
        }
      };
    return PersonalChatServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void validateUserIsMemberOfPersonalChat(personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatRequest request,
        io.grpc.stub.StreamObserver<personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateUserIsMemberOfPersonalChatMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service PersonalChatService.
   */
  public static abstract class PersonalChatServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return PersonalChatServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service PersonalChatService.
   */
  public static final class PersonalChatServiceStub
      extends io.grpc.stub.AbstractAsyncStub<PersonalChatServiceStub> {
    private PersonalChatServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PersonalChatServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PersonalChatServiceStub(channel, callOptions);
    }

    /**
     */
    public void validateUserIsMemberOfPersonalChat(personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatRequest request,
        io.grpc.stub.StreamObserver<personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateUserIsMemberOfPersonalChatMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service PersonalChatService.
   */
  public static final class PersonalChatServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<PersonalChatServiceBlockingStub> {
    private PersonalChatServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PersonalChatServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PersonalChatServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatResponse validateUserIsMemberOfPersonalChat(personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateUserIsMemberOfPersonalChatMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service PersonalChatService.
   */
  public static final class PersonalChatServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<PersonalChatServiceFutureStub> {
    private PersonalChatServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PersonalChatServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new PersonalChatServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatResponse> validateUserIsMemberOfPersonalChat(
        personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateUserIsMemberOfPersonalChatMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_VALIDATE_USER_IS_MEMBER_OF_PERSONAL_CHAT = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_VALIDATE_USER_IS_MEMBER_OF_PERSONAL_CHAT:
          serviceImpl.validateUserIsMemberOfPersonalChat((personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatRequest) request,
              (io.grpc.stub.StreamObserver<personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getValidateUserIsMemberOfPersonalChatMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatRequest,
              personal_chat.PersonalChat.ValidateUserIsMemberOfPersonalChatResponse>(
                service, METHODID_VALIDATE_USER_IS_MEMBER_OF_PERSONAL_CHAT)))
        .build();
  }

  private static abstract class PersonalChatServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PersonalChatServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return personal_chat.PersonalChat.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("PersonalChatService");
    }
  }

  private static final class PersonalChatServiceFileDescriptorSupplier
      extends PersonalChatServiceBaseDescriptorSupplier {
    PersonalChatServiceFileDescriptorSupplier() {}
  }

  private static final class PersonalChatServiceMethodDescriptorSupplier
      extends PersonalChatServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    PersonalChatServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PersonalChatServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PersonalChatServiceFileDescriptorSupplier())
              .addMethod(getValidateUserIsMemberOfPersonalChatMethod())
              .build();
        }
      }
    }
    return result;
  }
}
