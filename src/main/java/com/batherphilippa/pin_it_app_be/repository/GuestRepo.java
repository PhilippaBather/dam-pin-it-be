package com.batherphilippa.pin_it_app_be.repository;

import com.batherphilippa.pin_it_app_be.model.Guest;
import com.batherphilippa.pin_it_app_be.model.Permissions;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GuestRepo  extends CrudRepository<Guest, Long> {

    List<Guest> findByEmail(String email);

    Optional<Guest> findByEmailAndProjectId(String email, long projectId);

    @Query(value =  "SELECT DISTINCT " +
                        "g.guest_id, g.email, g.notified, g.permissions, g.project_id, g.user_id as ownerId, " +
                        "p.title, p.deadline, " +
                        "u.first_name as ownerName, u.surname as ownerSurname, u.email as ownerEmail, u.user_id " +
                    "FROM guests as g " +
                    "INNER JOIN projects as p on g.project_id = p.project_id " +
                    "INNER JOIN users as u on u.user_id = g.user_id " +
                    "WHERE g.project_id = p.project_id " +
                    "AND g.user_id = u.user_id " +
                    "AND g.email = :email;", nativeQuery = true)
    Set<Guest> findSharedProjectsByGuestEmail(String email);

    @Query(value =  "SELECT " +
            "g.guest_id, g.email, g.notified, g.permissions, g.project_id, g.user_id, p.title, p.deadline, p.project_status " +
            "from guests as g " +
            "inner join projects as p on p.project_id = g.project_id " +
            "where g.user_id = :userId;" , nativeQuery = true)
    List<Guest> findOwnedProjectsWithGuests(long userId);
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM guests as g " +
                   "WHERE g.project_id = :projectId", nativeQuery = true)
    void deleteByProjectId(long projectId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM guests as g " +
                   "WHERE g.project_id = :projectId AND g.email = :email;", nativeQuery = true)
    void deleteByProjectIdAndGuestEmail(long projectId, String email);

    @Transactional
    @Modifying
    @Query(value = "UPDATE guests as g " +
                   "SET g.permissions = :permissions " +
                   "WHERE g.email = :email AND g.project_id = :id;", nativeQuery = true)
    void updateGuestPermissions(Permissions permissions, String email, long id);


}
