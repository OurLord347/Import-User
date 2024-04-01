package com.import_user.services;

import com.import_user.entity.Company;
import com.import_user.entity.Position;
import com.import_user.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionService {
    @Autowired
    private PositionRepository positionRepository;

    public Position getByName(String name){
        Position position = null;
        List<Position> positionList = positionRepository.findByName(name);
        if (positionList.size() == 0){
            Position positionEntity = new Position();
            positionEntity.setName(name);
            position = positionRepository.save(positionEntity);
        } else {
            position = positionList.get(0);
        }

        return position;
    }
}
