package com.getyourticket.cf.dto;

import com.getyourticket.cf.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    private UserRegisterDTO userId;
    private EventDTO eventId;
    private String seatNumber;
    private BigDecimal price;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public UserRegisterDTO getUserRegisterDTO() {
        return userId;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", userDTO=" + userId +
                ", eventDTO=" + eventId +
                ", seatNumber='" + seatNumber + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
