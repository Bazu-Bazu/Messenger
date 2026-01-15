package group_chat;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.58.0)",
    comments = "Source: group_chat.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class GroupChatServiceGrpc {

  private GroupChatServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "group_chat.GroupChatService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<group_chat.GroupChat.ValidateMemberRightsInGroupChatRequest,
      group_chat.GroupChat.ValidateMemberRightsInGroupChatRequestResponse> getValidateMemberRightsInGroupChatMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ValidateMemberRightsInGroupChat",
      requestType = group_chat.GroupChat.ValidateMemberRightsInGroupChatRequest.class,
      responseType = group_chat.GroupChat.ValidateMemberRightsInGroupChatRequestResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<group_chat.GroupChat.ValidateMemberRightsInGroupChatRequest,
      group_chat.GroupChat.ValidateMemberRightsInGroupChatRequestResponse> getValidateMemberRightsInGroupChatMethod() {
    io.grpc.MethodDescriptor<group_chat.GroupChat.ValidateMemberRightsInGroupChatRequest, group_chat.GroupChat.ValidateMemberRightsInGroupChatRequestResponse> getValidateMemberRightsInGroupChatMethod;
    if ((getValidateMemberRightsInGroupChatMethod = GroupChatServiceGrpc.getValidateMemberRightsInGroupChatMethod) == null) {
      synchronized (GroupChatServiceGrpc.class) {
        if ((getValidateMemberRightsInGroupChatMethod = GroupChatServiceGrpc.getValidateMemberRightsInGroupChatMethod) == null) {
          GroupChatServiceGrpc.getValidateMemberRightsInGroupChatMethod = getValidateMemberRightsInGroupChatMethod =
              io.grpc.MethodDescriptor.<group_chat.GroupChat.ValidateMemberRightsInGroupChatRequest, group_chat.GroupChat.ValidateMemberRightsInGroupChatRequestResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ValidateMemberRightsInGroupChat"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group_chat.GroupChat.ValidateMemberRightsInGroupChatRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group_chat.GroupChat.ValidateMemberRightsInGroupChatRequestResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GroupChatServiceMethodDescriptorSupplier("ValidateMemberRightsInGroupChat"))
              .build();
        }
      }
    }
    return getValidateMemberRightsInGroupChatMethod;
  }

  private static volatile io.grpc.MethodDescriptor<group_chat.GroupChat.GetAllGroupChatMembersRequest,
      group_chat.GroupChat.GetAllGroupChatMembersResponse> getGetAllGroupChatMembersMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAllGroupChatMembers",
      requestType = group_chat.GroupChat.GetAllGroupChatMembersRequest.class,
      responseType = group_chat.GroupChat.GetAllGroupChatMembersResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<group_chat.GroupChat.GetAllGroupChatMembersRequest,
      group_chat.GroupChat.GetAllGroupChatMembersResponse> getGetAllGroupChatMembersMethod() {
    io.grpc.MethodDescriptor<group_chat.GroupChat.GetAllGroupChatMembersRequest, group_chat.GroupChat.GetAllGroupChatMembersResponse> getGetAllGroupChatMembersMethod;
    if ((getGetAllGroupChatMembersMethod = GroupChatServiceGrpc.getGetAllGroupChatMembersMethod) == null) {
      synchronized (GroupChatServiceGrpc.class) {
        if ((getGetAllGroupChatMembersMethod = GroupChatServiceGrpc.getGetAllGroupChatMembersMethod) == null) {
          GroupChatServiceGrpc.getGetAllGroupChatMembersMethod = getGetAllGroupChatMembersMethod =
              io.grpc.MethodDescriptor.<group_chat.GroupChat.GetAllGroupChatMembersRequest, group_chat.GroupChat.GetAllGroupChatMembersResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetAllGroupChatMembers"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group_chat.GroupChat.GetAllGroupChatMembersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group_chat.GroupChat.GetAllGroupChatMembersResponse.getDefaultInstance()))
              .setSchemaDescriptor(new GroupChatServiceMethodDescriptorSupplier("GetAllGroupChatMembers"))
              .build();
        }
      }
    }
    return getGetAllGroupChatMembersMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static GroupChatServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GroupChatServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GroupChatServiceStub>() {
        @java.lang.Override
        public GroupChatServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GroupChatServiceStub(channel, callOptions);
        }
      };
    return GroupChatServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static GroupChatServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GroupChatServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GroupChatServiceBlockingStub>() {
        @java.lang.Override
        public GroupChatServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GroupChatServiceBlockingStub(channel, callOptions);
        }
      };
    return GroupChatServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static GroupChatServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<GroupChatServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<GroupChatServiceFutureStub>() {
        @java.lang.Override
        public GroupChatServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new GroupChatServiceFutureStub(channel, callOptions);
        }
      };
    return GroupChatServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void validateMemberRightsInGroupChat(group_chat.GroupChat.ValidateMemberRightsInGroupChatRequest request,
        io.grpc.stub.StreamObserver<group_chat.GroupChat.ValidateMemberRightsInGroupChatRequestResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getValidateMemberRightsInGroupChatMethod(), responseObserver);
    }

    /**
     */
    default void getAllGroupChatMembers(group_chat.GroupChat.GetAllGroupChatMembersRequest request,
        io.grpc.stub.StreamObserver<group_chat.GroupChat.GetAllGroupChatMembersResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetAllGroupChatMembersMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service GroupChatService.
   */
  public static abstract class GroupChatServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return GroupChatServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service GroupChatService.
   */
  public static final class GroupChatServiceStub
      extends io.grpc.stub.AbstractAsyncStub<GroupChatServiceStub> {
    private GroupChatServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GroupChatServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GroupChatServiceStub(channel, callOptions);
    }

    /**
     */
    public void validateMemberRightsInGroupChat(group_chat.GroupChat.ValidateMemberRightsInGroupChatRequest request,
        io.grpc.stub.StreamObserver<group_chat.GroupChat.ValidateMemberRightsInGroupChatRequestResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getValidateMemberRightsInGroupChatMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAllGroupChatMembers(group_chat.GroupChat.GetAllGroupChatMembersRequest request,
        io.grpc.stub.StreamObserver<group_chat.GroupChat.GetAllGroupChatMembersResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetAllGroupChatMembersMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service GroupChatService.
   */
  public static final class GroupChatServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<GroupChatServiceBlockingStub> {
    private GroupChatServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GroupChatServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GroupChatServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public group_chat.GroupChat.ValidateMemberRightsInGroupChatRequestResponse validateMemberRightsInGroupChat(group_chat.GroupChat.ValidateMemberRightsInGroupChatRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getValidateMemberRightsInGroupChatMethod(), getCallOptions(), request);
    }

    /**
     */
    public group_chat.GroupChat.GetAllGroupChatMembersResponse getAllGroupChatMembers(group_chat.GroupChat.GetAllGroupChatMembersRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetAllGroupChatMembersMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service GroupChatService.
   */
  public static final class GroupChatServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<GroupChatServiceFutureStub> {
    private GroupChatServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected GroupChatServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new GroupChatServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<group_chat.GroupChat.ValidateMemberRightsInGroupChatRequestResponse> validateMemberRightsInGroupChat(
        group_chat.GroupChat.ValidateMemberRightsInGroupChatRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getValidateMemberRightsInGroupChatMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<group_chat.GroupChat.GetAllGroupChatMembersResponse> getAllGroupChatMembers(
        group_chat.GroupChat.GetAllGroupChatMembersRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetAllGroupChatMembersMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_VALIDATE_MEMBER_RIGHTS_IN_GROUP_CHAT = 0;
  private static final int METHODID_GET_ALL_GROUP_CHAT_MEMBERS = 1;

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
        case METHODID_VALIDATE_MEMBER_RIGHTS_IN_GROUP_CHAT:
          serviceImpl.validateMemberRightsInGroupChat((group_chat.GroupChat.ValidateMemberRightsInGroupChatRequest) request,
              (io.grpc.stub.StreamObserver<group_chat.GroupChat.ValidateMemberRightsInGroupChatRequestResponse>) responseObserver);
          break;
        case METHODID_GET_ALL_GROUP_CHAT_MEMBERS:
          serviceImpl.getAllGroupChatMembers((group_chat.GroupChat.GetAllGroupChatMembersRequest) request,
              (io.grpc.stub.StreamObserver<group_chat.GroupChat.GetAllGroupChatMembersResponse>) responseObserver);
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
          getValidateMemberRightsInGroupChatMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              group_chat.GroupChat.ValidateMemberRightsInGroupChatRequest,
              group_chat.GroupChat.ValidateMemberRightsInGroupChatRequestResponse>(
                service, METHODID_VALIDATE_MEMBER_RIGHTS_IN_GROUP_CHAT)))
        .addMethod(
          getGetAllGroupChatMembersMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              group_chat.GroupChat.GetAllGroupChatMembersRequest,
              group_chat.GroupChat.GetAllGroupChatMembersResponse>(
                service, METHODID_GET_ALL_GROUP_CHAT_MEMBERS)))
        .build();
  }

  private static abstract class GroupChatServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    GroupChatServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return group_chat.GroupChat.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("GroupChatService");
    }
  }

  private static final class GroupChatServiceFileDescriptorSupplier
      extends GroupChatServiceBaseDescriptorSupplier {
    GroupChatServiceFileDescriptorSupplier() {}
  }

  private static final class GroupChatServiceMethodDescriptorSupplier
      extends GroupChatServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    GroupChatServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (GroupChatServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new GroupChatServiceFileDescriptorSupplier())
              .addMethod(getValidateMemberRightsInGroupChatMethod())
              .addMethod(getGetAllGroupChatMembersMethod())
              .build();
        }
      }
    }
    return result;
  }
}
