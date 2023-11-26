package hu.torma.deliveryapplication.repository;

import hu.torma.deliveryapplication.entity.SecureUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SecureUserRepository extends JpaRepository<SecureUser, String> {
    @Query(value = """
SELECT CASE WHEN COUNT(u.username) > 0 THEN 1 ELSE 0 END as boOl
FROM SecureUser u
Where (u.username = ?1 AND u.password = ?2) 
""")
    Boolean loginWithUsernameAndPassword(String username, String password);

    @Query(value = """
SELECT CASE WHEN COUNT(u.username) > 0 THEN 1 ELSE 0 END as boOl
FROM SecureUser u
""")
    Boolean anyUserExists();

}