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

})

var emailState = 0;
var phoneState = 0;
var nameState = 0;
var address = 0;

//名稱欄位檢查
$('#memberName').change(function () {
  $('#buttonUpData').attr('disabled', false)
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
    let img_html = `<img src="${reader.result}">`;
    $('#preview').html(img_html);
  })
  $('#buttonUpData').attr('disabled', false)
})

//地址欄位
$('#memberAddress').change(function () {
  $('#buttonUpData').attr('disabled', false)
})

//取得驗證碼
$('#getAuthCode').click(function () {
  // console.log('OK')

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
$('#inputPassword_1').change(function(){
  let password_1 = $('#inputPassword_1').val()
  
  if(password_1 == ""){
	$('#passwordHelpBlock_1').html('密碼欄位不能為空')
	pw_1 = 1;
  } else {
	$('#passwordHelpBlock_1').html('')
	pw_1 = 0;
  $('#newPassword').attr('disabled', false)
  }
})
//新密碼
$('#inputPassword_2').change(function(){
  let regexPassword = /^\w{1,10}$/g
  let password_2 = $('#inputPassword_2').val()

  if(!regexPassword.test(password_2)){
    $('#passwordHelpBlock_2').html('密碼格式錯誤')
    $('#newPassword').attr('disabled', true)
    pw_2 = 1;
  } else {
    $('#passwordHelpBlock_2').html('')
    pw_2 = 0;
    if(pw_2 == 0 && pw_3 == 0 && pw_1 == 0){
      $('#newPassword').attr('disabled', false)
    }
  }
})

//確認新密碼
$('#inputPassword_3').change(function(){
  let password_3 = $('#inputPassword_3').val()
  let password_2 = $('#inputPassword_2').val()
  if(password_2 != password_3){
    $('#passwordHelpBlock_3').html('密碼不一致')
    $('#newPassword').attr('disabled', true)
    pw_3 = 1;
  } else {
    $('#passwordHelpBlock_3').html('')
    pw_3 = 0;
    if(pw_2 == 0 && pw_3 == 0 && pw_1 == 0){
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