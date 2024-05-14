package com.joyfulresort.ool.meetingroomorder;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingRoomOrderRepository extends JpaRepository<MeetingRoomOrder, Integer> {
    List<MeetingRoomOrder> findByMemberMemberId(Integer memberId);
    List<MeetingRoomOrder> findByMeetingRoomMeetingRoomId(Integer meetingRoomId);


}
