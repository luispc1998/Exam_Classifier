package domain.constraints;

/**
 * This represents a condition or circumstance that the exam scheduling must fulfill.
 */
public interface Constraint {

    /**
     * Returns the string id for the type of {@code Constriction}.
     * @return The String id for the type of {@code Constriction}.
     */
    String getConstrictionID();

}
