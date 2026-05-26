package com.keqi.gress.plugin.appstore.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagTypeDTO {

    private Long id;

    private String typeName;

    private String typeKey;

    private String description;

    private Boolean enabled;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
