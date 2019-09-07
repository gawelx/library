package com.restapi.library.dto;

import com.restapi.library.domain.Penalty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PenaltyDto {

    private Long id;
    private LocalDateTime creationTime;
    private String penaltyCause;
    private BigDecimal penaltyFee;
    private Boolean paid;
    private Long borrowingId;

    public PenaltyDto(Penalty penalty) {
        this(
                penalty.getId(),
                penalty.getCreationTime(),
                penalty.getPenaltyCause().toString(),
                penalty.getPenaltyFee(),
                penalty.getPaid(),
                penalty.getBorrowing().getId()
        );
    }

}
