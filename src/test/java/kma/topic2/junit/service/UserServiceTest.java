package kma.topic2.junit.service;

import kma.topic2.junit.exceptions.ConstraintViolationException;
import kma.topic2.junit.exceptions.LoginExistsException;
import kma.topic2.junit.model.NewUser;
import kma.topic2.junit.model.User;
import kma.topic2.junit.repository.UserRepository;
import kma.topic2.junit.validation.UserValidator;
//import org.junit.jupiter.api.Assertions;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @SpyBean
    @Autowired
    private UserValidator userValidator;

    @SpyBean
    @Autowired
    private UserRepository userRepository;

    @Test
    void validUserTest(){
        NewUser newUser = NewUser.builder().login("sorryfor").password("missed").fullName("deadline").build();
        userService.createNewUser(newUser);
        Assertions.assertThat(userRepository.isLoginExists("sorryfor")).isEqualTo(true);
        Assertions.assertThat(userRepository.getUserByLogin("sorryfor").getPassword()).isEqualTo(newUser.getPassword());
        Assertions.assertThat(userRepository.getUserByLogin("sorryfor").getFullName()).isEqualTo(newUser.getFullName());
    }

    @Test
    void loginExistsTest(){
        NewUser newUser = NewUser.builder().login("already").password("existin").fullName("person").build();
        userService.createNewUser(newUser);

        Mockito.verify(userValidator).validateNewUser(newUser);
        Mockito.verify(userRepository).saveNewUser(newUser);

        assertThatThrownBy(() -> userValidator.validateNewUser(newUser)).isInstanceOf(LoginExistsException.class);
    }


    @ParameterizedTest
    @CsvSource({
            "invlogin1,loooooooooooong,Alex_Kot",
            "invpass2,sh,Alex_Kotko",
            "invpass3,$%r^ng#,Alex_Kotkovian",

    })
    public void invalidPasswordTest(String login, String password, String fullName){
        NewUser newUser = NewUser.builder().login(login).password(password).fullName(fullName).build();
        assertThatThrownBy(() -> userService.createNewUser(newUser)).isInstanceOf(ConstraintViolationException.class);
    }
}