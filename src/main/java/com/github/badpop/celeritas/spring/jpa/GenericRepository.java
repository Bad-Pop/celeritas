package com.github.badpop.celeritas.spring.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Utility interface allowing a Jpa Repository to directly extend {@link JpaRepository}{@code <T, ID>} and
 * {@link CeleritasJpaSpecificationExecutor}{@code <T, ID>}.
 *
 * @param <T>  the type of the entity to handle
 * @param <ID> the type of the entity's identifier
 */
public interface GenericRepository<T, ID> extends JpaRepository<T, ID>, CeleritasJpaSpecificationExecutor<T, ID> {
}
