package io.github.badpop.celeritas.sb2.utils.jpa.impl;

import io.github.badpop.celeritas.sb2.utils.jpa.CeleritasJpaSpecificationExecutor;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.Path;
import java.util.Set;

import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

/**
 * This class implements the {@link CeleritasJpaSpecificationExecutor} optimisations and extends the features of
 * {@link SimpleJpaRepository} so that all repositories using this class as a base can benefit from these optimisations.
 *
 * @param <T>  the type of the entity to handle
 * @param <ID> the type of the entity's identifier
 */
public class SimpleCeleritasJpaRepository<T, ID> extends SimpleJpaRepository<T, ID> implements CeleritasJpaSpecificationExecutor<T, ID> {

  private final JpaEntityInformation<T, ID> entityInformation;
  private final EntityManager entityManager;

  public SimpleCeleritasJpaRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
    super(entityInformation, entityManager);
    this.entityInformation = entityInformation;
    this.entityManager = entityManager;
  }

  @Override
  public Page<ID> findAllEntitiesIds(@Nullable Specification<T> specification, Pageable pageable) {
    final var criteriaBuilder = entityManager.getCriteriaBuilder();
    final var criteriaQuery = criteriaBuilder.createQuery(entityInformation.getIdType());
    final var root = criteriaQuery.from(getDomainClass());

    criteriaQuery.select((Path<ID>) root.get(entityInformation.getIdAttribute()));

    if (specification != null) {
      final var predicate = specification.toPredicate(root, criteriaQuery, criteriaBuilder);

      if (predicate != null) {
        criteriaQuery.where(predicate);
      }
    }

    final var sort = pageable.isPaged()
      ? pageable.getSort()
      : Sort.unsorted();

    if (sort.isSorted()) {
      criteriaQuery.orderBy(toOrders(sort, root, criteriaBuilder));
    }

    final var typedQuery = entityManager.createQuery(criteriaQuery);

    if (pageable.isPaged()) {
      typedQuery.setFirstResult((int) pageable.getOffset());
      typedQuery.setMaxResults(pageable.getPageSize());
    }

    return PageableExecutionUtils.getPage(
      typedQuery.getResultList(),
      pageable,
      () -> executeCountQuery(getCountQuery(specification, getDomainClass())));
  }

  @Override
  public Specification<T> idIn(Set<ID> entitiesIds, String entityIdAttributeName) {
    if (CollectionUtils.isEmpty(entitiesIds)) {
      return null;
    }

    return (root, query, criteriaBuilder) -> root.get(entityIdAttributeName).in(entitiesIds);
  }

  private static long executeCountQuery(@NonNull TypedQuery<Long> query) {
    final var totals = query.getResultList();
    var total = 0L;

    for (Long item : totals) {
      total += item == null ? 0 : item;
    }

    return total;
  }
}
