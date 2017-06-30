package com.resetPasswordDemo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "/test", collectionResourceRel = "test")
public interface JpaService extends JpaRepository<Person, Long> {
	
	@RestResource(exported = false)
	@Query("SELECT p FROM Person AS p WHERE p.email =:email AND p.password =:password")
	public Person login(@Param("email") String email , @Param("password")String password);
	
	@RestResource(exported = false)
	@Query("SELECT p FROM Person AS p WHERE p.email =:email")
	public Person getPersonByEmail(@Param("email") String email);


}
