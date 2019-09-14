package jpa;


import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Model
public class EntityManagerProducer {

    @PersistenceContext(unitName = "travel")
    private EntityManager entityManager;

    @Produces
    protected EntityManager createEntityManager() {
        return entityManager;
    }
}