package messenger.personal.chat.service.controller.grpc;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import messenger.personal.chat.service.domain.repository.PersonalChatRepository;
import net.devh.boot.grpc.server.service.GrpcService;
import personal_chat.PersonalChat;
import personal_chat.PersonalChatServiceGrpc;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class PersonalChatGrpcService extends PersonalChatServiceGrpc.PersonalChatServiceImplBase {

    private final PersonalChatRepository personalChatRepository;

    @Override
    public void validateUserIsMemberOfPersonalChat(
            PersonalChat.ValidateUserIsMemberOfPersonalChatRequest request,
            StreamObserver<PersonalChat.ValidateUserIsMemberOfPersonalChatResponse> responseObserver
    ) {
        boolean memberExists = personalChatRepository.existsMemberByChatIdAndUserId(
                request.getChatId(),
                request.getUserId()
        );

        var response = PersonalChat.ValidateUserIsMemberOfPersonalChatResponse.newBuilder()
                .setUserId(request.getUserId())
                .setChatId(request.getChatId())
                .setIsMember(memberExists)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllPersonalChatMembers(
            PersonalChat.GetAllPersonalChatMembersRequest request,
            StreamObserver<PersonalChat.GetAllPersonalChatMembersResponse> responseObserver
    ) {
        List<Long> memberIds = personalChatRepository.findUserIdsByChatId(request.getChatId());

        var response = PersonalChat.GetAllPersonalChatMembersResponse.newBuilder()
                .addAllUserId(memberIds)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
