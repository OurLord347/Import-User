package com.import_user.repository;

import com.import_user.entity.Position;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PositionRepository extends CrudRepository<Position, Long> {

    List<Position> findByName(String name);

    Position findById(long id);
}
