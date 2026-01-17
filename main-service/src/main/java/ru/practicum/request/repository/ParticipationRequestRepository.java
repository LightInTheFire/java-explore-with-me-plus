package ru.practicum.request.repository;

import java.util.Collection;
import java.util.List;

import ru.practicum.request.model.EventRequestStatus;
import ru.practicum.request.model.ParticipationRequest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    boolean existsByEvent_IdAndRequester_Id(Long eventId, Long requesterId);

    List<ParticipationRequest> findAllByRequester_IdOrderByCreatedDesc(Long requesterId);

    List<ParticipationRequest> findAllByEvent_IdOrderByCreatedAsc(Long eventId);

    long countByEvent_IdAndStatus(Long eventId, EventRequestStatus status);

    List<ParticipationRequest> findAllByIdInAndEvent_Id(Collection<Long> ids, Long eventId);

    List<ParticipationRequest> findAllByEvent_IdAndStatus(Long eventId, EventRequestStatus status);
}
