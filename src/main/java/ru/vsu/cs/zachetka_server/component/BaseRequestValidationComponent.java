package ru.vsu.cs.zachetka_server.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import ru.vsu.cs.zachetka_server.model.dto.request.IValidated;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

@Configuration
public class BaseRequestValidationComponent {

    private final Validator validator;

    @Autowired
    public BaseRequestValidationComponent(Validator validator) {
        this.validator = validator;
    }

    public boolean isValid(IValidated request) {
        Set<ConstraintViolation<IValidated>> errors = validator.validate(request);
        return errors.isEmpty();
    }
}
