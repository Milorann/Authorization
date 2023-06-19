package kpo.authorization.repos;

import kpo.authorization.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SessionRepository extends JpaRepository<Session, Integer> {
    Session findBySessionToken(String token);
}
