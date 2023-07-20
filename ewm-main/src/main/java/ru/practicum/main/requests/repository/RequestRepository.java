package ru.practicum.main.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main.requests.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    List<ParticipationRequest> findParticipationRequestsByRequester_Id(int userId);

    Optional<ParticipationRequest> findParticipationRequestByIdAndRequester_Id(int requestId, int userId);

    List<ParticipationRequest> findParticipationRequestsByEventsWithRequests_IdAndEventsWithRequests_Initiator_Id(int eventId, int userId);
}
