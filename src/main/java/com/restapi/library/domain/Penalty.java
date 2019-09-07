package com.restapi.library.domain;

import com.restapi.library.dto.PenaltyDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class Penalty {

    public static final BigDecimal PENALTY_DAILY_RATE = BigDecimal.valueOf(1.0);

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private LocalDateTime creationTime;

    @NotNull
    private PenaltyCause penaltyCause;

    @NotNull
    private BigDecimal penaltyFee;

    @NotNull
    private Boolean paid;

    @ManyToOne
    @JoinColumn(
            name = "borrowingId",
            foreignKey = @ForeignKey(name = "fk_penalty_borrowing")
    )
    private Borrowing borrowing;

    public Penalty(PenaltyDto penaltyDto, Borrowing borrowing) {
        this(
                penaltyDto.getId(),
                penaltyDto.getCreationTime(),
                PenaltyCause.of(penaltyDto.getPenaltyCause()),
                penaltyDto.getPenaltyFee(),
                penaltyDto.getPaid(),
                borrowing
        );
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationTime, penaltyCause, penaltyFee, borrowing);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Penalty penalty = (Penalty) o;
        return id.equals(penalty.id) &&
                creationTime.equals(penalty.creationTime) &&
                penaltyCause == penalty.penaltyCause &&
                penaltyFee.equals(penalty.penaltyFee) &&
                borrowing.equals(penalty.borrowing);
    }

}
