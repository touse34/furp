package com.furp.mapper;

import com.furp.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoomMapper {
    @Select("SELECT * from meeting_room")
    List<Room> selectAllRooms();

    @Select("SELECT location from meeting_room where id = #{roomId}")
    String selectLocationByRoomId(Integer roonId);



}
