package testclasses;

/**
 * to test nested field access
 * ex: a = instance.nestedField.nestedField.value
 * ex: instance.nestedField.nestedField.value = "abc"
 */
public class NestedField {

    public static final NestedField instance = new NestedField(
            new NestedField(new NestedField(null, "c"), "b"), "a");

    public NestedField nestedField;
    public String value;

    public NestedField(final NestedField nestedField, final String value) {
        this.nestedField = nestedField;
        this.value = value;
    }

    public NestedField getNestedField() {
        return nestedField;
    }

    public String getValue() {
        return value;
    }
}
