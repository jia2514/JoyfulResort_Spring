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
//判斷登入狀態 改用Spring th:switch 判斷
// $(document).ready(function () { //ready事件
//   //判斷後端JAVA 有無寫入登入狀態cookie值 改寫登入/登出按鈕狀態
//   if (getCookie('LogInState') != null) {
//     $('#LoginButton').css('display', 'none')
//     $('#LogoutButton').css('display', 'block')
//   } else {
//     $('#LoginButton').css('display', 'blocl')
//     $('#LogoutButton').css('display', 'none')
//   }
// })

//登入
$('#Button_Login').click(function () {
  console.log($('#rememberMe').get(0).checked)
  let inputAccount = $('#userAccount').val()
  let inputPassword = $('#userPassword').val()

  //帳號
  if (inputAccount == "") {
    $('#errorMessage').html('不能有空欄位')
    $('#userAccount').removeClass().addClass('form-control is-invalid')
    $('#buttonUpData').attr('disabled', true)
    
  } else {
    $('#userAccount').removeClass().addClass('form-control')
  }
  //密碼
  if (inputPassword == "") {
    $('#errorMessage').html('不能有空欄位')
    $('#userPassword').removeClass().addClass('form-control is-invalid')
    $('#Button_Login').attr('disabled', true)
  } else {
    $('#userPassword').removeClass().addClass('form-control')
  }

  if (inputAccount !== "" && inputPassword !== "") {
    

    $.post({
      url: 'joyfulresort/Login',
      data: {
        'userAccount': inputAccount,
        'userPassword': inputPassword
      },
      datatype: 'json',
      success: function (data) {


        if (data.State) {
          $('#errorMessage').html('')
          window.location.reload()
        } else {
          $('#errorMessage').html('帳號或密碼錯誤')
        }
      }
    })
  }


})

$('#userAccount, #userPassword').change(function () {
  let inputAccount = $('#userAccount').val()
  let inputPassword = $('#userPassword').val()

  if (inputAccount !== "" && inputPassword !== "") {
    $('#AccountErrorMessage').html('')
    $('#PasswordErrorMessage').html('')
    $('#Button_Login').attr('disabled', false)
  }
})
//會員專區按鈕
$('#memberField').click(function (e) {
  e.preventDefault();
  console.log(getCookie('LogInState'))
  console.log(document.cookie)

  //判斷燈入狀態 有-->個人頁面 無-->提示登入
  if (getCookie('LogInState') != null) {
    window.location.href = '/joyfulresort/member/memberinfo';
  } else {
    alert('請先登入')
    $('#LoginButton').click();
  }
})

$(document).keydown(function (e) {
  // console.log(e.which)
  if (e.which === 13) {
    $('#Button_Login').click()
  }
})

//忘記密碼 取得驗證碼
$('#forgetPassword_getAuthCode').click(function () {
  // console.log('OK')

  $.get({
    url: '/redis/getAuthCode',
    success: function (data) {
      // console.log(data)
      let html_AuthCode = `<h5>驗證碼: ` + data + `</h5>`;

      $('#forgetPassword_AuthCode').html(html_AuthCode)

    }
  })
})
//點擊忘記密碼取得驗證碼
$('#but_forgetPassword').click(function(){
  $.get({
    url: '/redis/getAuthCode',
    success: function (data) {
      // console.log(data)
      let html_AuthCode = `<h5>驗證碼: ` + data + `</h5>`;

      $('#forgetPassword_AuthCode').html(html_AuthCode)

    }
  })
})

//忘記密碼 送出資料
$('#Button_forgetPassword').click(function () {
  let inputEmail = $('#forgetPassword_inputEmali').val()
  let authCode = $('#forgetPassword_inPutAuthCode').val()

  // console.log(inputEmail)
  // console.log(authCode)

 

  if (inputEmail === "" || authCode === "") {
    $('#Error_forgetPassword').html('不能有空欄位')
    $('#forgetPassword_inPutAuthCode').removeClass().addClass('form-control is-invalid')
    $('#forgetPassword_inputEmali').removeClass().addClass('form-control is-invalid')
    if(inputEmail != ""){
      $('#forgetPassword_inputEmali').removeClass().addClass('form-control')
    }
    if(authCode != ""){
      $('#forgetPassword_inPutAuthCode').removeClass().addClass('form-control')
    }
  } else {
    $('#forgetPassword_inputEmali').removeClass().addClass('form-control')
    $('#forgetPassword_inPutAuthCode').removeClass().addClass('form-control')
    $.post({
      url: '/joyfulresort/member/forgetPassword',
      data: {
        'inputEmail': inputEmail,
        'authCode': authCode
      },
      datatype: 'json',
      success: function (data) {
        // console.log(data.error)
        if (data.error == "true") {
          $('#Error_forgetPassword').html('')
          // console.log('ok')
          $('#back_Login').click()
          alert('已將新密碼寄出')
        } else {
          $('#forgetPassword_inputEmali').removeClass().addClass('form-control')
          $('#forgetPassword_inPutAuthCode').removeClass().addClass('form-control is-invalid')
          $('#Error_forgetPassword').html(data.error)
        }

      }
    })
  }
})