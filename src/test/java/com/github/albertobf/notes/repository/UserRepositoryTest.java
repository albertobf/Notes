package com.github.albertobf.notes.repository;

import com.github.albertobf.notes.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindByUsername_thenReturnUser() {
        //given
        User user = new User();
        user.setName("Alberto");
        user.setPassword("Password");
        user.setUsername("albertobf");
        entityManager.persistAndFlush(user);

        //when
        Optional<User> userFromDb = userRepository.findByUsername(user.getUsername());

        //then
        assertThat(userFromDb.get().getUsername()).isEqualTo(user.getUsername());
    }


}