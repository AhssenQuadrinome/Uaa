package com.ourbusway.uaa.event.output;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MailOutputEvent {
    private List<String> to;

    private List<String> cc;

    private List<String> bcc;

    private String subject;

    private String templateId;

    private Map<String, String> variables;
}
