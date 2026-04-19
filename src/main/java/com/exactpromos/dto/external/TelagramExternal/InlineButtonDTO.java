package com.exactpromos.dto.external.TelagramExternal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InlineButtonDTO {

    private String text;

    private String url;
}
