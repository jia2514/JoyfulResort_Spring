package com.joyfulresort.ool.meetingroomphoto;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.joyfulresort.ool.meetingroom.MeetingRoom;

@Entity
@Table(name = "meetingroom_photo")
public class MeetingRoomPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meetingroom_photo_id")
    private Integer meetingRoomPhotoId;

    @Column(name = "photo_path")
    private String photoPath;

    @Lob
    @Column(name = "meetingroom_image")
    private Blob meetingRoomImage;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meetingroom_id", referencedColumnName = "meetingroom_id")
    private MeetingRoom meetingRoom;

    public Integer getMeetingRoomPhotoId() {
        return meetingRoomPhotoId;
    }

    public void setMeetingRoomPhotoId(Integer meetingRoomPhotoId) {
        this.meetingRoomPhotoId = meetingRoomPhotoId;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Blob getMeetingRoomImage() {
        return meetingRoomImage;
    }

    public void setMeetingRoomImage(Blob meetingRoomImage) {
        this.meetingRoomImage = meetingRoomImage;
    }

    public MeetingRoom getMeetingRoom() {
        return meetingRoom;
    }

    public void setMeetingRoom(MeetingRoom meetingRoom) {
        this.meetingRoom = meetingRoom;
    }
}
