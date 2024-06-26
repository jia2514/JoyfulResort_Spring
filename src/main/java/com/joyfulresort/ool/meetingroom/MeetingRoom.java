package com.joyfulresort.ool.meetingroom;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.joyfulresort.ool.meetingroomorder.MeetingRoomOrder;
import com.joyfulresort.ool.meetingroomphoto.MeetingRoomPhoto;

@Entity
@Table(name = "meetingroom")
public class MeetingRoom {
    @Column(name = "meetingroom_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer meetingRoomId;

    @Column(name = "meetingroom_name")
    @NotBlank(message = "*請輸入會議廳名稱")
    private String meetingRoomName;

    @Column(name = "meetingroom_ename")
    @NotBlank(message = "*請輸入會議廳英文")
    private String meetingRoomEname;

    @Column(name = "meetingroom_content")
    private String meetingRoomContent;

    @Column(name = "meetingroom_sale_state")
    @NotNull(message = "*請輸入會議廳狀態")
    private Boolean meetingRoomSaleState;

    @Column(name = "meetingroom_price")
    @NotNull(message = "*請輸入會議廳價格")
    @Min(value = 0, message = "數字不可小於0")
    private Integer meetingRoomPrice;

    @Column(name = "meetingroom_chair")
    @NotNull(message = "*請輸入座位數")
    @Min(value = 0, message = "數字不可小於0")
    private Integer meetingRoomChair;
    @Column(name = "meetingroom_square_meter")
    @NotNull(message = "*請輸入場地坪數")
    @Min(value = 0, message = "數字不可小於0")
    private Integer meetingRoomSquareMeter;
    @Column(name = "meetingroom_capacity")
    @NotBlank(message = "*請輸入容納人數")
    private String meetingRoomCapacity;
    @OneToMany(mappedBy = "meetingRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MeetingRoomPhoto> photos;

    @OneToMany(mappedBy = "meetingRoom", cascade = CascadeType.ALL)
    private List<MeetingRoomOrder> meetingRoomOrderList;

    public Integer getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Integer meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public String getMeetingRoomName() {
        return meetingRoomName;
    }

    public void setMeetingRoomName(String meetingRoomName) {
        this.meetingRoomName = meetingRoomName;
    }

    public String getMeetingRoomEname() {
        return meetingRoomEname;
    }

    public void setMeetingRoomEname(String meetingRoomEname) {
        this.meetingRoomEname = meetingRoomEname;
    }

    public String getMeetingRoomContent() {
        return meetingRoomContent;
    }

    public void setMeetingRoomContent(String meetingRoomContent) {
        this.meetingRoomContent = meetingRoomContent;
    }

    public Boolean getMeetingRoomSaleState() {
        return meetingRoomSaleState;
    }

    public void setMeetingRoomSaleState(Boolean meetingRoomSaleState) {
        this.meetingRoomSaleState = meetingRoomSaleState;
    }

    public Integer getMeetingRoomPrice() {
        return meetingRoomPrice;
    }

    public void setMeetingRoomPrice(Integer meetingRoomPrice) {
        this.meetingRoomPrice = meetingRoomPrice;
    }

    public Integer getMeetingRoomChair() {
        return meetingRoomChair;
    }

    public void setMeetingRoomChair(Integer meetingRoomChair) {
        this.meetingRoomChair = meetingRoomChair;
    }

    public Integer getMeetingRoomSquareMeter() {
        return meetingRoomSquareMeter;
    }

    public void setMeetingRoomSquareMeter(Integer meetingRoomSquareMeter) {
        this.meetingRoomSquareMeter = meetingRoomSquareMeter;
    }

    public String getMeetingRoomCapacity() {
        return meetingRoomCapacity;
    }

    public void setMeetingRoomCapacity(String meetingRoomCapacity) {
        this.meetingRoomCapacity = meetingRoomCapacity;
    }

    public List<MeetingRoomPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MeetingRoomPhoto> photos) {
        this.photos = photos;
    }

    public List<MeetingRoomOrder> getMeetingRoomOrderList() {
        return meetingRoomOrderList;
    }

    public void setMeetingRoomOrderList(List<MeetingRoomOrder> meetingRoomOrderList) {
        this.meetingRoomOrderList = meetingRoomOrderList;
    }
}
