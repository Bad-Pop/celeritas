# Spring boot related features

Celeritas offers features that integrate with your spring boot applications.
Celeritas `SB2` versions support spring boot version `2.x` and `SB3` versions support spring boot version `3.x`.
But if you don't use Spring boot, no worries, you can always use Celeritas and select any version.

## The SimpleCeleritasJpaRepository repository base class

The main purpose of the SimpleCeleritasJpaRepository class is to allow you to solve the `HHH000104` firstResult maxResults warning using Spring Data JPA
Specification and Criteria API. Indeed, whenever you use pagination and SQL joins to fetch entities and their associations to prevent the N+1 queries problem,
you'll most-likely run into the Hibernate's `HHH000104` warning message.

:sos: _**Be aware that this alert is very bad for your application, and especially for its performance when your dataset grows.**_

The SimpleCeleritasJpaRepository class implements the solution to this problem that was proposed by the excellent Vlad Mihalcea
in [this article](https://vladmihalcea.com/fix-hibernate-hhh000104-entity-fetch-pagination-warning-message/).

### How to use SimpleCeleritasJpaRepository ?

To use SimpleCeleritasJpaRepository only a few steps are required. First, you need to add this configuration to your Spring Boot application :

```java
@EnableJpaRepositories(repositoryBaseClass = SimpleCeleritasJpaRepository.class)
```

Then make your repository extends CeleritasJpaSpecificationExecutor and override the findAll method to use an `@EntityGraph` :

```java

@Repository
public interface MyRepository extends JpaRepository<EntityClass, Long>, CeleritasJpaSpecificationExecutor<EntityClass, Long> {

  @Override
  @EntityGraph(attributePaths = "yourJoinAttribute")
  List<EntityClass> findAll(Specification<EntityClass> spec);
}

//Or more quickly using the Celeritas GenericRepository
@Repository
public interface MyRepository extends GenericRepository<EntityClass, Long> {

  @Override
  @EntityGraph(attributePaths = "yourJoinAttribute")
  List<EntityClass> findAll(Specification<EntityClass> spec);
}
```

Then you simply call the `findAllEntitiesIds()` method from your own repositories, which will return the IDs of the entities matching your search.
Once you have retrieved the ids of your entities, create a new JPA specification using the `idIn()` method.

```java
// Please note that the specification can be null here
Page<Long> myEntitiesIdsPage = myRepository.findAllEntitiesIds(specification,pageable);

List<EntityClass> result;
List<Long> ids = myEntitiesIdsPage.getContent();

if (CollectionUtils.isEmpty(ids)) {
  result = Collections.emptyList();
} else {
  // Retrieve entities using IN predicate
  Specification<EntityClass> idInSpecification = myRepository.idIn(ids, "myEntityIdAttributeName");
  result = myRepository.findAll(idInSpecification);
}

return PageableExecutionUtils
    .getPage(result, pageable, ()-> myEntitiesIdsPage.getTotalElements());
```

With this approach, you effectively eliminate the performance problem caused by paging in your application memory by relaying it to your database.

### Conclusion on the Hibernate `HHH000104` warning

The `HHH000104` warning is so bad. There is no SQL pagination because the pagination is done in the application memory. Imagine that your query retrieves 1 or 2
million elements from your database without pagination in the SQL query actually sent : you then load the 1 or 2 million elements into your application and
return only a small part to the caller... **This is counterproductive !**

There a couple of approaches to fix the pagination in memory. One of them is to execute two SQL queries. The first query retrieves just the entities’ ID
using pagination and the second query retrieves the entities using an IN predicate with the IDs from the first query. This is the solution offered by Celeritas
with SimpleCeleritasJpaRepository.
