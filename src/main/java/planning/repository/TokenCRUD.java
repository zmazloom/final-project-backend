package planning.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import planning.model.Token;

import java.util.List;

@Repository
public interface TokenCRUD extends JpaRepository<Token, Long> {

    @Query("from Token t where t.userToken = :token")
    List<Token> getTokenByToken(String token);

}
