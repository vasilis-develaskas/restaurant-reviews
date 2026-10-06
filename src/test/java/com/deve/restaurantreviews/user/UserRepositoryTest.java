package com.deve.restaurantreviews.user;

import com.deve.restaurantreviews.TestcontainersConfiguration;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void savesUserWithDefaultRoleAndNormalizeEmail() {

        //--Given
        User user = new User("Maria@Example.com", "Maria", "hash");

        //--When
        User saved = userRepository.saveAndFlush(user);
        entityManager.clear();
        User loaded = userRepository.findById(saved.getId()).orElseThrow();

        //--Then
        assertThat(loaded.getEmail()).isEqualTo("maria@example.com");
        assertThat(loaded.getUsername()).isEqualTo("Maria");
        assertThat(loaded.getRole()).isEqualTo(Role.USER);
        assertThat(loaded.isAdmin()).isFalse();
        assertThat(loaded.getVersion()).isZero();
        assertThat(loaded.getUpdatedAt()).isNotNull();
        assertThat(loaded.getCreatedAt()).isNotNull();
    }

    @Test
    void findsUserByEmail() {

        userRepository.saveAndFlush(new User("Maria@Example.com", "Maria", "hash"));

        assertThat(userRepository.findByEmail("maria@example.com")).isPresent();
        assertThat(userRepository.findByEmail("nikos@example.com")).isEmpty();

    }

    @Test
    void findsUserByUsernameIgnoreCase() {

        userRepository.saveAndFlush(new User("Maria@Example.com", "Maria", "hash"));

        assertThat(userRepository.findByUsernameIgnoreCase("MARIA")).isPresent();
        assertThat(userRepository.findByUsernameIgnoreCase("maria")).isPresent();
        assertThat(userRepository.findByUsernameIgnoreCase("nikos")).isEmpty();
    }

    @Test
    void rejectsUsernameThatDiffersOnlyInCase() {

        userRepository.saveAndFlush(new User("Maria@Example.com", "Maria", "hash"));

        User sameNameDifferentCase = new User("maria3@example.com", "maria", "hash");

        assertThatThrownBy(() -> userRepository.saveAndFlush(sameNameDifferentCase)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void rejectsDoublicateEmail() {

        userRepository.saveAndFlush(new User("Maria@Example.com", "Maria", "hash"));

        User sameEmail = new User("mARia@example.com", "maria2", "hash");

        assertThatThrownBy(() -> userRepository.saveAndFlush(sameEmail)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void rejectsUsernameContaingAtSign() {

        User user = new User("Maria@Example.com", "Maria@Test", "hash");

        assertThatThrownBy(() -> userRepository.saveAndFlush(user)).isInstanceOf(DataIntegrityViolationException.class);
    }
}
