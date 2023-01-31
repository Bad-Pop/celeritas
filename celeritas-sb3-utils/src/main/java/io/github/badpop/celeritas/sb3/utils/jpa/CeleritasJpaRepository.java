package io.github.badpop.celeritas.sb3.utils.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Utility interface that extends the Spring data {@link JpaRepository} interface and the {@link CeleritasJpaSpecificationExecutor} interface.
 * Simply extend this interface from your own repositories to save time and take advantage of the optimizations provided by {@link CeleritasJpaSpecificationExecutor}.
 *
 * <p>Using this interface is absolutely not breakable and will make your repositories work as usual.
 * If you use it, just remember to add this configuration to your application:
 * <br>
 * {@code @EnableJpaRepositories(repositoryBaseClass = SimpleCeleritasJpaRepository.class)}
 *
 * @param <T>  the type of the entity to handle
 * @param <ID> the type of the entity's identifier
 */
public interface CeleritasJpaRepository<T, ID> extends JpaRepository<T, ID>, CeleritasJpaSpecificationExecutor<T, ID> {
}
