package com.github.albertobf.notes.repository;

import com.github.albertobf.notes.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void whenFindByExistingRole_thenReturnRole() {
        //given
        Role role = new Role();
        role.setRole("ROLE_USER");
        entityManager.persistAndFlush(role);
        //when
        Role userRole = roleRepository.findByRole(role.getRole());
        //then
        assertThat(userRole.getRole()).isEqualTo(role.getRole());
    }

}