package ru.practicum.request.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import lombok.experimental.FieldDefaults;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "participation_request")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    User requester;

    @Column(nullable = false)
    LocalDateTime created;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    EventRequestStatus status;
}
