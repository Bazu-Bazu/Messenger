package com.burkina.messenger.dto.request;

import lombok.Builder;

@Builder
public record MarkMessageAsReadRequest(
        Long messageId
) {}
