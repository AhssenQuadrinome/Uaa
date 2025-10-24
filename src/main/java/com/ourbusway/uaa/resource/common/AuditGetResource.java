package com.ourbusway.uaa.resource.common;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditGetResource {
    private String createdBy;
    private LocalDateTime createdAt;
    private String modifiedBy;
    private LocalDateTime modifiedAt;
}
