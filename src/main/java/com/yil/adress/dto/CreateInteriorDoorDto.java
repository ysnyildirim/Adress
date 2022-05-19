package com.yil.adress.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInteriorDoorDto {
    @NotBlank
    @Length(min = 1, max = 100)
    private String name;
    @NotNull
    private Long exteriorDoorId;
}
