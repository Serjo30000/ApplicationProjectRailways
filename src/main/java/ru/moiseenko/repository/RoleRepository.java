package ru.moiseenko.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.moiseenko.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role,Integer> {

}
