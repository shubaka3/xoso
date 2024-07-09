package hutech.com.demo.Validators;


import hutech.com.demo.Validators.Annotations.ValidUsername;
import hutech.com.demo.repository.IUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {

    @Autowired
    private IUserRepository userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (userService == null)
            return true;
        return userService.findByUsername(username).isEmpty();
    }
}
