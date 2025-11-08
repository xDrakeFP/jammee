package federicopini.jammee.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<Phone, String> {
    private String region;
    private PhoneNumberUtil util = PhoneNumberUtil.getInstance();

    @Override
    public void initialize(Phone constraintAnnotation) {
        this.region = constraintAnnotation.region();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        try {
            Phonenumber.PhoneNumber number = util.parse(value, region);
            return util.isValidNumber(number);
        } catch (NumberParseException ex){
            return false;
        }
    }
}
