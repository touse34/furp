package com.furp.service.impl;

import com.furp.mapper.RoomMapper;
import com.furp.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomImpl implements RoomService {

    @Autowired
    RoomMapper roomMapper;

    @Override
    public String findLocationByRoomId(Integer roomId) {
        return roomMapper.selectLocationByRoomId(roomId);
    }
}
