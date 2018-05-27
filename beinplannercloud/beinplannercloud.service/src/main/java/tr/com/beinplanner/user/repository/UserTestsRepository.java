package tr.com.beinplanner.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tr.com.beinplanner.user.dao.UserTests;
@Repository
public interface UserTestsRepository extends CrudRepository<UserTests, Long> {

	@Query(value="SELECT COUNT(*) " + 
			"				 FROM user_tests a" + 
			"				 WHERE TEST_ID=:testId ",nativeQuery=true)
	public long findUserTestsCountByTestId(@Param("testId") long testId);

	public List<UserTests> findByUserId( long userId);

}
