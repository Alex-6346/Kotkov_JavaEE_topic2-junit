package kma.topic2.junit.validation;

import kma.topic2.junit.exceptions.ConstraintViolationException;
import kma.topic2.junit.exceptions.LoginExistsException;
import kma.topic2.junit.model.NewUser;
import kma.topic2.junit.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserValidatorTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserValidator userValidator;


    @Test
    public void checkValidationTest(){
        NewUser user = NewUser.builder().login("anylogin").build();
        Mockito.when(userRepository.isLoginExists(ArgumentMatchers.anyString())).thenReturn(true);
        assertThatThrownBy(() -> userValidator.validateNewUser(user)).isInstanceOf(LoginExistsException.class);
    }

    @ParameterizedTest
    @CsvSource({
            "loooooooooooooooooooong",
            "sh",
            "12324q@"
    })
    public void invalidPasswordTest(String password){
        NewUser newUser = NewUser.builder().password(password).build();
        Mockito.when(userRepository.isLoginExists(ArgumentMatchers.anyString())).thenReturn(false);
        assertThatThrownBy(() -> userValidator.validateNewUser(newUser)).isInstanceOf(ConstraintViolationException.class);
    }


}