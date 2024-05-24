//JS 動態路徑
var MyPoint = "/Ajax";
var host = window.location.host;
var path = window.location.pathname;
var webCtx = path.substring(0, path.indexOf('/', 1));
var endPointURL = "http://" + window.location.host + webCtx + MyPoint;

var nameState;
var accountState;
var passwordState;
var checkPasswordState;
var phoneState;
var emailState;


//取消註冊按鈕功能
$(document).ready(function () {
    $('#bittonLogin').attr('disabled', true)
})

//名稱欄位
$('#inputName').blur(function () {

    let inputName = $('#inputName').val()
    // console.log('名稱:' + inputName)

    let regexName = /^[\u4e00-\u9fa5\w]{1,10}$/g

    if (inputName == "") {
        // console.log('名稱不能為空')
        $('#nameError').html('名稱不能為空')
        $('#inputName').removeClass().addClass('form-control is-invalid')
        nameState = 1;
        $('#bittonLogin').attr('disabled', true)
    } else if (!regexName.test(inputName)) {
        // console.log('名稱不能為空')
        $('#nameError').html('名稱格式錯誤 名稱中英文加數字共10位組成 不能有空格')
        $('#inputName').removeClass().addClass('form-control is-invalid')
        nameState = 1;
        $('#bittonLogin').attr('disabled', true)
    } else {
        $('#inputName').removeClass().addClass('form-control is-valid')
        nameState = 0;
        if (0 === nameState && 0 === accountState && 0 === passwordState && 0 === checkPasswordState && 0 === phoneState && 0 === emailState) {
            $('#bittonLogin').attr('disabled', false)
        }

    }

})
//檢查帳號欄位
$('#inputAccount').blur(function () {

    let inputAccount = $('#inputAccount').val()
    // console.log('帳號:'+inputAccount)
    //正規表示法
    let regexAccount = /^\w{1,10}$/g


    if (inputAccount == "") {

        $('#AccountError').html('帳號不能為空')
        $('#inputAccount').removeClass().addClass('form-control is-invalid')
        accountState = 1;
        $('#bittonLogin').attr('disabled', true)
    } else if (!regexAccount.test(inputAccount)) { //正規表示法
        // console.log('帳號不能為空')
        $('#AccountError').html('帳號格式錯誤 帳號格式由共10個英文大小寫加數字組成 不能有空格')
        $('#inputAccount').removeClass().addClass('form-control is-invalid')
        accountState = 1;
        $('#bittonLogin').attr('disabled', true)
    } else {
        $('#inputAccount').removeClass().addClass('form-control')

        $.post({
            url: '/joyfulresort/member/Ajax',
            data: {
                "inputColumn": "Account",
                "inputAccount": inputAccount
            },
            datatype: 'json',
            success: function (data) {
                // console.log(data)
                // console.log(data.inputAccount)

                if (data.inputAccount) {
                    $('#AccountError').html('此帳號已被使用')
                    $('#inputAccount').removeClass().addClass('form-control is-invalid')
                    accountState = 1;
                    $('#bittonLogin').attr('disabled', true)
                } else {
                    $('#inputAccount').removeClass().addClass('form-control is-valid')
                    accountState = 0;
                    if (0 === nameState && 0 === accountState && 0 === passwordState && 0 === checkPasswordState && 0 === phoneState && 0 === emailState) {
                        $('#bittonLogin').attr('disabled', false)
                    }
                }
            }
        })

    }
})
//密碼欄位
$('#inputPassword').blur(function () {

    let inputPassword = $('#inputPassword').val()
    // console.log('密碼:'+inputPassword)

    let regexPassword = /^\w{1,10}$/g

    if (inputPassword == "") {
        // console.log('此欄位不能為空')
        $('#PasswordError').html('此欄位不能為空')
        $('#inputPassword').removeClass().addClass('form-control is-invalid')
        passwordState = 1;
        $('#bittonLogin').attr('disabled', true)
    } else
        if (!regexPassword.test(inputPassword)) {
            // console.log('密碼不能為空')
            $('#PasswordError').html('密碼格式錯誤 密碼由英文大小寫加數字組成共10位 不能有空格')
            $('#inputPassword').removeClass().addClass('form-control is-invalid')
            passwordState = 1;
            $('#bittonLogin').attr('disabled', true)
        } else {
            $('#inputPassword').removeClass().addClass('form-control is-valid')
            passwordState = 0;
            if (0 === nameState && 0 === accountState && 0 === passwordState && 0 === checkPasswordState && 0 === phoneState && 0 === emailState) {
                $('#bittonLogin').attr('disabled', false)
            }
        }
})

//確認密碼欄位
$('#checkPassword').blur(function () {

    let inputPassword = $('#inputPassword').val()
    let checkPassword = $('#checkPassword').val()
    // console.log('密碼:'+inputPassword)
    // console.log('確認密碼'+checkPassword)

    if (checkPassword == "") {
        // console.log('此欄位不能為空')
        $('#checkPasswordError').html('此欄位不能為空')
        $('#checkPassword').removeClass().addClass('form-control is-invalid')
        checkPasswordState = 1;
        $('#bittonLogin').attr('disabled', true)
    } else if (inputPassword != checkPassword) {
        $('#checkPassword').removeClass().addClass('form-control is-invalid')
        $('#checkPasswordError').html('輸入密碼不相同')
        checkPasswordState = 1;
        $('#bittonLogin').attr('disabled', true)
    } else {
        $('#checkPassword').removeClass().addClass('form-control is-valid')
        checkPasswordState = 0;
        if (0 === nameState && 0 === accountState && 0 === passwordState && 0 === checkPasswordState && 0 === phoneState && 0 === emailState) {
            $('#bittonLogin').attr('disabled', false)
        }
    }
})


//檢查電話欄位
$('#inputPhone').blur(function () {

    let inputPhone = $('#inputPhone').val()
    // console.log('電話:'+inputAccount)

    //正規表示法
    let regexPhone = /^09\d{8}$/g
    // console.log(!regexPhone.test(inputPhone))

    if (inputPhone == "") {
        // console.log('名稱不能為空')
        $('#PhoneError').html('電話不能為空')
        $('#inputPhone').removeClass().addClass('form-control is-invalid')
        phoneState = 1;
        $('#bittonLogin').attr('disabled', true)
    } else if (!regexPhone.test(inputPhone)) {
        // console.log("OK");
        $('#PhoneError').html('電話格式不對 請輸入由09開頭0~9組成的10位數 不能有空格')
        $('#inputPhone').removeClass().addClass('form-control is-invalid')
        phoneState = 1;
        $('#bittonLogin').attr('disabled', true)
    } else {
        $.post({
            url: '/joyfulresort/member/Ajax',
            data: {
                "inputColumn": "Phone",
                "inputPhone": inputPhone
            },
            datatype: 'json',
            success: function (data) {
                // console.log(data)
                // console.log(data.inputPhone)

                if (data.inputPhone) {
                    $('#PhoneError').html('此電話已被使用')
                    $('#inputPhone').removeClass().addClass('form-control is-invalid')
                    phoneState = 1;
                    $('#bittonLogin').attr('disabled', true)
                } else {
                    $('#inputPhone').removeClass().addClass('form-control is-valid')
                    phoneState = 0;
                    if (0 === nameState && 0 === accountState && 0 === passwordState && 0 === checkPasswordState && 0 === phoneState && 0 === emailState) {
                        $('#bittonLogin').attr('disabled', false)
                    }
                }
            }
        })
    }
})

//檢查信箱欄位
$('#inputEmail').blur(function () {

    let inputEmail = $('#inputEmail').val()
    // console.log('信箱:'+inputAccount)

    //正規表示法
    let regexEmail = /^([\w\.\-_]+)?\w+@[\w-_]+(\.\w+){1,}$/igm

    if (inputEmail == "") {
        // console.log('信箱不能為空')
        $('#EmailError').html('信箱不能為空')
        $('#inputEmail').removeClass().addClass('form-control is-invalid')
        emailState = 1;
        $('#bittonLogin').attr('disabled', true)
    } else if (!regexEmail.test(inputEmail)) {

        $('#EmailError').html('信箱格式錯誤')
        $('#inputEmail').removeClass().addClass('form-control is-invalid')
        emailState = 1;
        $('#bittonLogin').attr('disabled', true)
    } else {
        $('#inputEmail').removeClass().addClass('form-control')

        $.post({
            url: '/joyfulresort/member/Ajax',
            data: {
                "inputColumn": "Email",
                "inputEmail": inputEmail
            },
            datatype: 'json',
            success: function (data) {
                // console.log(data)
                // console.log(data.inputEmail)

                if (data.inputEmail) {
                    $('#EmailError').html('此信箱已被使用')
                    $('#inputEmail').removeClass().addClass('form-control is-invalid')
                    emailState = 1;
                    $('#bittonLogin').attr('disabled', true)
                } else {
                    $('#inputEmail').removeClass().addClass('form-control is-valid')
                    emailState = 0;
                    if (0 === nameState && 0 === accountState && 0 === passwordState && 0 === checkPasswordState && 0 === phoneState && 0 === emailState) {
                        $('#bittonLogin').attr('disabled', false)
                    }
                }
            }
        })

    }
})

//檢查地址欄位