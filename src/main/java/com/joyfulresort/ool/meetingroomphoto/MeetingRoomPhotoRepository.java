package com.joyfulresort.ool.meetingroomphoto;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomPhotoRepository extends JpaRepository<MeetingRoomPhoto, Integer> {
    MeetingRoomPhoto findByMeetingRoomMeetingRoomId(Integer meetingRoomId);
}
