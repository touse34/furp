package com.furp.mapper;

import com.furp.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoomMapper {
    @Select('select * from room');
    List<Room> selectAllRooms();



}
