package com.ourbusway.uaa.event.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailConfirmedOutputEvent {
    private String organisationId;
}
