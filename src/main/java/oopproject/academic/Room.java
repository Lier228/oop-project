package oopproject.academic;

import java.io.Serializable;
import oopproject.enums.RoomType;

public class Room implements Serializable {
    private static final long serialVersionUID = 1L;

    private String roomNumber;
    private RoomType roomType;

    public Room() {
    }

    public Room(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Room(String roomNumber, RoomType roomType) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
}
