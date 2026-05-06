package oopproject.academic;

import oopproject.enums.RoomType;

public class Room {
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
