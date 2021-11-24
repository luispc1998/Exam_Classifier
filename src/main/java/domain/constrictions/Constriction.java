package domain.constrictions;

/**
 * This represents a condition or circumstance that the exam scheduling must fulfill.
 */
public interface Constriction {

    /**
     * Returns the string id for the type of {@code Constriction}.
     * @return The String id for the type of {@code Constriction}.
     */
    String getConstrictionID();

}
