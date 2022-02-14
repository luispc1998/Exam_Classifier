package domain.constraints;

/**
 * This represents a condition or circumstance that for of the particular problem instance.
 */
public interface Constraint {

    /**
     * Returns the string id for the type of {@code Constraint}.
     * @return The String id for the type of {@code Constraint}.
     */
    String getConstraintID();

}
