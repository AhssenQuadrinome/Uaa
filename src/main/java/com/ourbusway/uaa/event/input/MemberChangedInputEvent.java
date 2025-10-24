package com.ourbusway.uaa.event.input;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberChangedInputEvent {
    private String firstName;
    private String lastName;
    private String memberId;
    private String userId;
}
