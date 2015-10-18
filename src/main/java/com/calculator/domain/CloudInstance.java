package com.calculator.domain;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CloudInstance {

    private final String id;
    private final String customerId;
    private final String hostId;
}
