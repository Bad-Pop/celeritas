package com.github.badpop.celeritas.spring.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.Set;

/**
 * An interface to extend the operation of {@link JpaSpecificationExecutor},
 * with which you will have access to different methods to optimise your interactions with your database
 *
 * @param <T>  the type of the entity to handle
 * @param <ID> the type of the entity's identifier
 */
public interface CeleritasJpaSpecificationExecutor<T, ID> extends JpaSpecificationExecutor<T> {

  /**
   * Retrieves in a paginated way the Ids of all entities corresponding to the requested search.
   * Use this method to optimise the performance of your search and ultimately retrieve entities using the
   * {@link JpaSpecificationExecutor#findAll(Specification)} method via a specification built
   * using {@link #idIn(Set, String)}
   *
   * @param specification allowing you to perform complex searches on your database. Can be null.
   * @param pageable      to paginate your query to limit the number of results returned.
   * @return a new page containing the ids of the entities matching your search
   * @throws IllegalStateException        if the underlying {@link EntityManager} has been closed
   * @throws QueryTimeoutException        if the query execution exceeds
   *                                      the query timeout value set and only the statement is
   *                                      rolled back
   * @throws TransactionRequiredException if a lock mode other than
   *                                      <code>NONE</code> has been set and there is no transaction
   *                                      or the persistence context has not been joined to the
   *                                      transaction
   * @throws PessimisticLockException     if pessimistic locking
   *                                      fails and the transaction is rolled back
   * @throws LockTimeoutException         if pessimistic locking
   *                                      fails and only the statement is rolled back
   * @throws PersistenceException         if the query execution exceeds
   *                                      the query timeout value set and the transaction
   *                                      is rolled back
   */
  Page<ID> findAllEntitiesIds(@Nullable Specification<T> specification, Pageable pageable);

  /**
   * Utility method allowing you to build a very simple Jpa specification for retrieving entities by their id
   *
   * @param entitiesIds           a set of all ids of entities to be retrieved. Can be null or empty.
   * @param entityIdAttributeName the name of the attribute of the entity class annotated @Id
   * @return a new specification for filtering on entity id
   */
  Specification<T> idIn(Set<ID> entitiesIds, String entityIdAttributeName);
}
