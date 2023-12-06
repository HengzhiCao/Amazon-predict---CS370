package Controller;

public class InputValidationController {

    public InputValidationController() {

    }

    /**
     * Validates the price to ensure it is a non-negative value.
     *
     * @param price The price to be validated.
     * @return true if the price is valid, false otherwise.
     */
    public boolean validatePrice(Double price) {
        return price != null && price >= 0;
    }

    /**
     * Validates the product name.
     * @param name the name of the product
     * @return true if the product name is valid, false otherwise
     */
    public boolean validateProductName(String name) {
        // Check if the name is not null and not empty after removing leading and trailing white spaces
        return name != null && !name.trim().isEmpty();
    }

}
