//取得指定Cookie JS無法直接取得指定cookie 的Key-value 需要自己寫function取得 
function getCookie(cookieName) {  // 自訂function
  const strCookie = document.cookie
  const cookieList = strCookie.split(';')

  for (let i = 0; i < cookieList.length; i++) {
    const arr = cookieList[i].split('=')
    if (cookieName === arr[0].trim()) {
      return arr[1]
    }
  }
}

//取消修改按鈕功能
$(document).ready(function () {
  // console.log(document.cookie)
  $('#buttonUpData').attr('disabled', true)
  $('#newPassword').attr('disabled', true)
  
  //活動報名後重導致會員專區中的 活動訂單
  let getRedirect = new URL(location.href).searchParams.get('Redirect');
  // console.log(getRedirect)
  
  if(getRedirect === "activity"){
    $('#nav-contact-tab2').click()
  }

})

var emailState = 0;
var phoneState = 0;
var nameState = 0;
var address = 0;

//名稱欄位檢查
$('#memberName').change(function () {

  let inputName = $('#memberName').val()
  let regexName = /^[\u4e00-\u9fa5\w]{1,10}$/g
  if(inputName == ""){

  }



  if (0 == emailState && 0 == phoneState) {
    $('#buttonUpData').attr('disabled', false)
  }
})

//信箱欄位
$('#memberEmail').change(function () {
  // console.log('OK')


  let inputEmail = $('#memberEmail').val()
  //正規表示法
  let regexEmail = /^([\w\.\-_]+)?\w+@[\w-_]+(\.\w+){1,}$/igm

  // console.log(inputEmail)

  if (inputEmail == "") {
    console.log('信箱欄位為空值')

    $('#emailError').html('*信箱欄位不能為空')
    emailState = 1;
    $('#buttonUpData').attr('disabled', true)

  } else if (!regexEmail.test(inputEmail)) {

    $('#emailError').html('*信箱格式錯誤')
    emailState = 1;
    $('#buttonUpData').attr('disabled', true)
  } else {
    $.post({
      url: '/joyfulresort/member/Ajax',
      data: {
        "inputColumn": "Email",
        "inputEmail": inputEmail
      },
      datatype: "json",
      success: function (data) {

        if (data.inputEmail) {
          $('#emailError').html('*此信箱已被使用')
          emailState = 1;
          $('#buttonUpData').attr('disabled', true)
        } else {
          $('#emailError').html('')
          emailState = 0;
          if (0 == emailState && 0 == phoneState) {
            $('#buttonUpData').attr('disabled', false)
          }


        }
      }
    })
  }
})

//電話欄位檢查
$('#memberPhone').change(function () {
  // console.log('OK')

  let inputPhone = $('#memberPhone').val()
  //正規表示法
  let regexPhone = /^09\d{8}$/g

  // console.log(inputEmail)

  if (inputPhone == "") {

    $('#phoneError').html('*電話欄位不能為空')
    phoneState = 1;
    $('#buttonUpData').attr('disabled', true)

  } else if (!regexPhone.test(inputPhone)) {

    $('#phoneError').html('*電話格式錯誤')
    phoneState = 1;
    $('#buttonUpData').attr('disabled', true)
  } else {
    $.post({
      url: '/joyfulresort/member/Ajax',
      data: {
        "inputColumn": "Phone",
        "inputPhone": inputPhone
      },
      datatype: "json",
      success: function (data) {

        if (data.inputPhone) {
          $('#phoneError').html('*此電話已被使用')
          phoneState = 1;
          $('#buttonUpData').attr('disabled', true)
        } else {
          $('#phoneError').html('')
          phoneState = 0;
          if (0 == emailState && 0 == phoneState) {
            $('#buttonUpData').attr('disabled', false)
          }

        }
      }
    })
  }
})



//圖片預覽
var reader = new FileReader();
$('#formFile').on('change', function () {
  reader.readAsDataURL(this.files[0]);
  reader.addEventListener('load', function () {
    let img_html = `<img class="img-thumbnail" src="${reader.result}">`;
    $('#preview').html(img_html);
  })
  if (0 == emailState && 0 == phoneState) {
    $('#buttonUpData').attr('disabled', false)
  }
})

//地址欄位
$('#memberAddress').change(function () {
  if (0 == emailState && 0 == phoneState) {
    $('#buttonUpData').attr('disabled', false)
  }
})

//生日欄位
$('#memberBirthday').change(function(){
  if (0 == emailState && 0 == phoneState) {
    $('#buttonUpData').attr('disabled', false)
  }
})

//性別欄位
$('#memberGender').change(function(){
  if (0 == emailState && 0 == phoneState) {
    $('#buttonUpData').attr('disabled', false)
  }
})
//驗證按鈕
$('#memberState_AuthCode').click(function(){
  $.get({
    url: '/redis/getAuthCode',
    success: function (data) {
      // console.log(data)
      let html_AuthCode = `<h1>` + data + `</h1>`;

      $('#returnAuthCode').html(html_AuthCode)

    }
  })
})

//更新圖形化驗證碼
function loadCode(){
  var url ="/member/getCode?ts=" + new Date().getTime();
  $('#memberCaptcha').attr("src",url)
}

//取得驗證碼
$('#getAuthCode').click(function () {

  // console.log('OK')
  loadCode();
  $.get({
    url: '/redis/getAuthCode',
    success: function (data) {
      // console.log(data)
      let html_AuthCode = `<h1>` + data + `</h1>`;

      $('#returnAuthCode').html(html_AuthCode)

    }
  })
})

//確認驗證碼
$('#checkAuthCode').click(function () {
  // console.log('OK')

  let inputAuthCode = $('#inputAuthCode').val()
  // console.log(inputAuthCode)
  let id = getCookie('MemberID')
  $.post({
    url: '/joyfulresort/member/checkAuthCode',
    data: {
      "inputAuthCode": inputAuthCode,
      "MemberID": id
    },
    datatype: 'json',
    success: function (data) {
      // console.log(data.checkAuthCode)

      switch (data.checkAuthCode) {
        case '404':
          $('#errorText').html('連結已逾時，請重新申請');
          break;
        case '200':
          $('#errorText').html('');
          $('#cloose_button').click()
          $('#div_button_checkAuthCode').html('<button type="button" class="btn btn-success">已驗證</button>')
          break;
        case '400':
          $('#errorText').html('驗證碼有誤，請重新輸入');
          break;
      }
    }
  })
})

var pw_1 = 1;
var pw_2 = 1;
var pw_3 = 1;
//用戶密碼
$('#inputPassword_1').change(function () {
  let password_1 = $('#inputPassword_1').val()

  if (password_1 == "") {
    $('#passwordHelpBlock_1').html('密碼欄位不能為空')
    pw_1 = 1;
  } else {
    $('#passwordHelpBlock_1').html('')
    pw_1 = 0;
    if (pw_2 == 0 && pw_3 == 0 && pw_1 == 0) {
      $('#newPassword').attr('disabled', false)
    }
  }
})
//新密碼
$('#inputPassword_2').change(function () {
  let regexPassword = /^\w{1,10}$/g
  let password_2 = $('#inputPassword_2').val()

  if (!regexPassword.test(password_2)) {
    $('#passwordHelpBlock_2').html('密碼格式錯誤')
    $('#newPassword').attr('disabled', true)
    pw_2 = 1;
  } else {
    $('#passwordHelpBlock_2').html('')
    pw_2 = 0;
    if (pw_2 == 0 && pw_3 == 0 && pw_1 == 0) {
      $('#newPassword').attr('disabled', false)
    }
  }
})

//確認新密碼
$('#inputPassword_3').change(function () {
  let password_3 = $('#inputPassword_3').val()
  let password_2 = $('#inputPassword_2').val()
  if (password_2 != password_3) {
    $('#passwordHelpBlock_3').html('密碼不一致')
    $('#newPassword').attr('disabled', true)
    pw_3 = 1;
  } else {
    $('#passwordHelpBlock_3').html('')
    pw_3 = 0;
    if (pw_2 == 0 && pw_3 == 0 && pw_1 == 0) {
      $('#newPassword').attr('disabled', false)
    }
  }

})



//修改密碼 確認送出按鈕
$('#newPassword').click(function () {
  // console.log('OK')

  //取值
  let MemberID = getCookie('MemberID')
  let password_1 = $('#inputPassword_1').val()
  let password_2 = $('#inputPassword_2').val()
  let password_3 = $('#inputPassword_3').val()

  // console.log("輸入密碼:"+password_1)
  // console.log("新密碼:"+password_2)
  // console.log("再次輸入密碼:"+password_3)



  if (password_1 == "" && password_2 == "" && password_3 == "") {
    $('#passwordHelpBlock_4').html('不能有空欄位')

  } else {
    $.post({
      url: '/joyfulresort/member/passwordRevise',
      data: {
        'MemberID': MemberID,
        'password_1': password_1,
        'password_2': password_2,
        'password_3': password_3
      },
      datatype: 'json',
      success: function (data) {
        //console.log(data);

        if (data) {
          //console.log("正確")
          alert('密碼已修改,請重新登入')
          $('#LogoutButton').click()
        } else {
          $('#passwordHelpBlock_4').html('密碼錯誤')
          //console.log('錯誤')
        }

      }
    })
  }
})

//活動訂單取消
var activityOrderID;
var activity_tr_children;
$(document).on('click', '#activityCancelOrder', function () {
  // console.log('KKO')
  //取得本列表格(tr)資訊
  let tr = null;
  tr = this.closest('tr');
  // console.log(tr);
  //取得tr內的子元素
  activity_tr_children = tr.children
  // console.log(tr_children)
  //取得訂單編號
  activityOrderID = activity_tr_children[0].innerText;
  // console.log('訂單編號='+activityOrderID)

  // let memberID = getCookie('MemberID')
  // console.log('memberID='+memberID)

  $('#but_checkActivityCancelOrder').click()
})

$('#makeSureActivityCancelOrder').click(function () {
  //  console.log('OKKO')

  $.post({
    url: '/joyfulresort/member/CancelOrder',
    data: {
      'CancelOrder': 'activity',
      'activityOrderID': activityOrderID
    },
    datatype: 'json',
    success: function (data) {
      if (data) {
        $(activity_tr_children[4]).html(`<span>訂單取消中</span>`)
        $(activity_tr_children[7]).html('<span style="color: #ff4800ad;">退款中</span>')
        $(activity_tr_children[8]).html('<button id="activityCancelOrder_disabled" type="button" class="btn btn-outline-secondary" disabled>取消訂單</button>')
      }
    }
  })

  $('#Close_checkActivityCancelOrder').click()

})

//活動分頁測試
$('#table_memberActivityOrder').tablepage($('#table_memberActivityOrder_page'), 3)

//會議廳訂單取消
var MeetingRoomOrderID;
var MeetingRoom_tr_children;
$(document).on('click', '#MeetingRoomOrder', function () {
  // console.log('KKO')
  //取得本列表格(tr)資訊
  let tr = null;
  tr = this.closest('tr');
  // console.log(tr);
  //取得tr內的子元素
  MeetingRoom_tr_children = tr.children
  // console.log(tr_children)
  //取得訂單編號
  MeetingRoomOrderID = MeetingRoom_tr_children[0].innerText;
  // console.log('訂單編號='+activityOrderID)

  // let memberID = getCookie('MemberID')
  // console.log('memberID='+memberID)

  $('#but_checkMeetingRoomOrder').click()
})

$('#makeSureMeetingRoomOrder').click(function () {
  //  console.log('OKKO')

  $.post({
    url: '/joyfulresort/member/CancelOrder',
    data: {
      'CancelOrder': 'meetingRoom',
      'MeetingRoomOrderID': MeetingRoomOrderID
    },
    datatype: 'json',
    success: function (data) {
      if (data) {
        // console.log('OKL')
        $(MeetingRoom_tr_children[5]).html('<span>取消</span>')
        $(MeetingRoom_tr_children[6]).html('<span style="color: #ff4800ad;">退款中</span>')
        $(MeetingRoom_tr_children[7]).html('<button id="MeetingRoomOrder_disabled" type="button" class="btn btn-outline-secondary" disabled>取消訂單</button>')
      }
    }
  })

  $('#Close_checkMeetingRoomOrder').click()

})

//會議廳分頁
$('#table_memberMeetingRoomOrder').tablepage($('#table_memberMeetingRoomOrder_page'), 3)


//餐廳訂單取消
var ReserveOrderID;
var ReserveOrder_tr_children;
$(document).on('click', '#ReserveOrder', function () {
  // console.log('KKO')
  //取得本列表格(tr)資訊
  let tr = null;
  tr = this.closest('tr');
  // console.log(tr);
  //取得tr內的子元素
  ReserveOrder_tr_children = tr.children
  // console.log(tr_children)
  //取得訂單編號
  ReserveOrderID = ReserveOrder_tr_children[0].innerText;
  // console.log('訂單編號='+activityOrderID)

  // let memberID = getCookie('MemberID')
  // console.log('memberID='+memberID)

  $('#but_checkReserveOrder').click()
})

$('#makeSureReserveOrder').click(function () {
  //  console.log('OKKO')

  $.post({
    url: '/joyfulresort/member/CancelOrder',
    data: {
      'CancelOrder': 'ReserveOrder',
      'ReserveOrderID': ReserveOrderID
    },
    datatype: 'json',
    success: function (data) {
      if (data) {
        console.log('OKKKKK')
        $(ReserveOrder_tr_children[6]).html('<span>取消</span>')
        $(ReserveOrder_tr_children[7]).html('<button type="button" class="btn btn-outline-secondary" disabled>取消訂單</button>')
      }
    }
  })

  $('#Close_checkReserveOrder').click()

})

//餐廳分頁
$('#table_memberReserveOrder').tablepage($('#table_memberReserveOrder_page'), 3)

//住房訂單取消
var RoomOrderID;
var RoomOrder_tr_children;
$(document).on('click', '#RoomOrder', function () {
  // console.log('KKO')
  //取得本列表格(tr)資訊
  let tr = null;
  tr = this.closest('tr');
  // console.log(tr);
  //取得tr內的子元素
  RoomOrder_tr_children = tr.children
  // console.log(tr_children)
  //取得訂單編號
  RoomOrderID = RoomOrder_tr_children[0].innerText;
  // console.log('訂單編號='+activityOrderID)

  // let memberID = getCookie('MemberID')
  // console.log('memberID='+memberID)

  $('#but_checkRoomOrder').click()
})

$('#makeSureRoomOrder').click(function () {
  //  console.log('OKKO')

  $.post({
    url: '/joyfulresort/member/CancelOrder',
    data: {
      'CancelOrder': 'RoomOrder',
      'RoomOrderID': RoomOrderID
    },
    datatype: 'json',
    success: function (data) {
      if (data) {
        console.log('OKKKKK')
        $(RoomOrder_tr_children[5]).html('<span>取消中</span>')
        $(RoomOrder_tr_children[6]).html('<span>退款中</span>')
        $(RoomOrder_tr_children[7]).html('<button id="RoomOrder_disabled" type="button" class="btn btn-outline-secondary" disabled>取消訂單</button>')
      }
    }
  })

  $('#Close_checkRoomOrder').click()

})

//住房分頁
$('#table_memberRoomOrder').tablepage($('#table_memberRoomOrder_page'), 3)