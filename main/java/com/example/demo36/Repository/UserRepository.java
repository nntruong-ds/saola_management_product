package com.example.demo36.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.saolasoft.base.persistence.repository.VoidableRepository;

import java.util.List;
import java.util.Optional;
import com.example.demo36.Entity.User;
@Repository
public interface UserRepository extends VoidableRepository<User, Long> {

    Optional<User> findByUsernameAndVoidedFalse(String username);


    boolean existsByUsernameAndVoidedFalse(String username);

    // Override các method dùng generic ID (Serializable) vì Hibernate 6.3.1 không thể
    // so sánh 'Long' với 'Serializable' trong type check — fix bằng @Query cụ thể.
    @Override
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.voided = false")
    User findByIdAndVoidedFalse(@Param("id") Long id);

    @Override
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.id = :id AND u.voided = false")
    boolean existsByIdAndVoidedFalse(@Param("id") Long id);

    @Override
    @Query("SELECT u FROM User u WHERE u.voided = false")
    List<User> findAllByVoidedFalse();
}
