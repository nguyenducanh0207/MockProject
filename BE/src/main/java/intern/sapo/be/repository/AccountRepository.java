package intern.sapo.be.repository;

import intern.sapo.be.entity.Account;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>, CrudRepository<Account, Integer> {
	Optional<Account> findAccountByUsername(String username);
	List<Account> findAllByIsDelete(boolean delete);
}
