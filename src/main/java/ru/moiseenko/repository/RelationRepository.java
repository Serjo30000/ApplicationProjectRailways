package ru.moiseenko.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.moiseenko.entity.Relation;

@Repository
public interface RelationRepository extends CrudRepository<Relation,Integer> {
    @Modifying
    @Query("UPDATE users_roles SET users_roles.user_id=:userId, users_roles.roles_id=:roleId WHERE users_roles.user_id=:userId")
    void updateRelation(@Param("userId")int userId, @Param("roleId")int roleId);
}
