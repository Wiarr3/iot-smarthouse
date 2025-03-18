package com.smartass.server.model.command;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @Setter
public class DeviceCommandDTO {
    private String deviceId;
    private String command;
    private String value;
}
