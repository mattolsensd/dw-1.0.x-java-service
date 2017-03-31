package molsen.dw.util;

import com.porch.commons.response.ValidationError;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/*
 * ValidationError does not override equals method
 * Testing equality of a list of them is a huge pain without it
 */

public class ValidationErrorTestWrapper {

    private final ValidationError validationError;

    ValidationErrorTestWrapper(ValidationError ve) {
        this.validationError = ve;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationErrorTestWrapper)) return false;
        ValidationErrorTestWrapper that = (ValidationErrorTestWrapper) o;
        return Objects.equals(validationError.getField(), that.validationError.getField()) &&
                Objects.equals(validationError.getMessage(), that.validationError.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(validationError.getField(), validationError.getMessage());
    }

    @Override
    public String toString() {
        return "ValidationError{" +
                "field='" + validationError.getField() + '\'' +
                ", message='" + validationError.getMessage() + '\'' +
                '}';
    }


    public static void assertEqual(List<ValidationError> expected, List<ValidationError> actual) {
        assertEquals(
                expected.stream().map(ValidationErrorTestWrapper::new).collect(Collectors.toList()),
                actual.stream().map(ValidationErrorTestWrapper::new).collect(Collectors.toList())
        );
    }

}
