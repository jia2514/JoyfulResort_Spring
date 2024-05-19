package com.joyfulresort.he.member.model;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joyfulresort.jia.roomorder.model.RoomOrder;
import com.joyfulresort.jia.roomorder.model.RoomOrderRepository;
import com.joyfulresort.ool.meetingroomorder.MeetingRoomOrder;
import com.joyfulresort.ool.meetingroomorder.MeetingRoomOrderRepository;
import com.joyfulresort.reserveorder.model.ResRepository;
import com.joyfulresort.reserveorder.model.ResVO;
import com.joyfulresort.so.activityorder.model.ActivityOrderRepository;
import com.joyfulresort.so.activityorder.model.ActivityOrderVO;


@Service("memberService")
public class MemberService {

	@Autowired
	MemberRepository repository;
	
	@Autowired
	private RoomOrderRepository reRoomOrder;
	
	@Autowired
	ActivityOrderRepository AOrepository;
	
	@Autowired
    private MeetingRoomOrderRepository MROrepository;
	
	@Autowired
	private ResRepository Resrepository;


	// 所有會員資料
	public List<MemberVO> getAll() {
		return repository.findAll();
	}

	// 使用PK查詢會員狀態並修改
	public MemberVO memberStateFindById(Integer id) {
		Optional<MemberVO> mem = repository.findById(id);
		MemberVO member = mem.get();
		if (member.getMemberState() == 2) { // 判斷用戶是否停權
			member.setMemberState(0); // 狀態2(停權) 設置為0復原
			MemberVO upMemberState = repository.save(member);
			return upMemberState;
		} else { // 狀態不為2(狀態正常) 設置為2停權
			member.setMemberState(2);
			MemberVO upMemberState = repository.save(member);
			return upMemberState;
		}
	}

	// 查詢會員資料
	public MemberVO getOneMember(Integer id) {
		Optional<MemberVO> mem = repository.findById(id);
//		MemberVO member = mem.get();
		return mem.orElse(null); //如果存在值回傳其值 否則回傳other的值
	}

	// 透過Account查詢會員資料
	public MemberVO getUserData(String account) {
		Integer userId = repository.findUserByAccount(account);
		if (userId == null) {
			return null;
		} else {
			Optional<MemberVO> user = repository.findById(userId);
			MemberVO userData = user.get();
			return userData;
		}
	}

	// 用戶修改資料
	public MemberVO upUserData(String memberId, String inputName, String inputEmail, String inputPhone,
			String inputAddrsee, String inputBirthday){

		
		int upData = repository.upData(inputName, inputEmail, inputPhone, inputAddrsee, Date.valueOf(inputBirthday),
				memberId);
//		System.out.println(upData);
		MemberVO newData = repository.findById(Integer.valueOf(memberId)).get();
		return newData;
	}
		
	
	//註冊
	public MemberVO newMember(String Name, String Account, String Password, String Email, String Phone, String Address,
			Integer Gender, Date Birthday) {
		MemberVO mem = new MemberVO();

		mem.setMemberName(Name);
		mem.setMemberAccount(Account);
		mem.setMemberPassword(Password);
		mem.setMemberEmail(Email);
		mem.setMemberPhone(Phone);
		mem.setMemberAddress(Address);
		mem.setMemberState(0);
		mem.setMemberGender(Gender);
		mem.setMemberBirthday(Birthday);

		MemberVO newMember = repository.save(mem);
		return newMember;
	}
	
	//檢查帳號是否存在
	public boolean checkAccount(String account) {
		return repository.existsBymemberAccount(account);
	}
	//檢查電話是否存在
	public boolean checkPhone(String phone) {
		return repository.existsBymemberPhone(phone);
	}
	//檢查信箱是否存在
	public boolean checkEmail(String email) {
		return repository.existsBymemberEmail(email);
	}

	//圖片上傳
	public void upDataByImg(String ID, byte[] buf) {
		repository.upImg(buf, ID);
		return;		
	}
	//透過驗證更新狀態
	public void memberStateUpData(Integer id) {
		Optional<MemberVO> mem = repository.findById(id);
		MemberVO member = mem.get();
		member.setMemberState(1);
		repository.save(member);
		return;
	}
	//用戶修改密碼
	public void changePassword(MemberVO mem) {
		repository.save(mem);
		
	}

	
//	--------------------------------------------------------------------------
	public MemberVO findByMemberPhone(String memberPhone) {
		return repository.findByMemberPhone(memberPhone);
	}
	
	//使用會員編號 查詢住宿訂單
	public List<RoomOrder> findMemberRoomOrder(Integer memberId) {
		return reRoomOrder.findRoomOrderByMemberId(memberId);
	}
	//使用會員編號 查詢活動訂單
	public List<ActivityOrderVO> findActivityOrderByMemberId(Integer memberID) {
		return AOrepository.findActivityOrderByMemberId(memberID);
	}
	//使用會員編號 查詢會議廳訂單
	public List<MeetingRoomOrder> findMeetingRoomOrderByMemberId(Integer memberID) {
		return MROrepository.findByMemberMemberId(memberID);
	}
	//使用會員編號 查詢餐廳訂單
	public List<ResVO> findmemberReserveOrderByMemberId(Integer memberID) {
		return Resrepository.findmemberReserveOrderByMemberId(memberID);
	}
	
	//用戶取消活動訂單
	public ActivityOrderVO activityCancelOrder(Integer orderID) {
		Optional<ActivityOrderVO> AOV = AOrepository.findById(orderID);
		ActivityOrderVO memberActivityOrderVO = AOV.get();
		memberActivityOrderVO.setOrderStatus((byte) 2);
		memberActivityOrderVO.setRefundStatus((byte) 1);
		AOrepository.save(memberActivityOrderVO);	
		return AOrepository.findById(orderID).get();
	}

	
	
}