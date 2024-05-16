//JS 動態路徑
var MyPoint = "/QueryMember";
var host = window.location.host;
var path = window.location.pathname;
var webCtx = path.substring(0, path.indexOf('/', 1));
var endPointURL = "http://" + window.location.host + webCtx + MyPoint;

//console.log(endPointURL);

//狀態修改按鈕
$(document).on('click', '#stateButton', function () {
	// console.log('OK');
	let tr = this.closest('tr');
	// console.log(tr);
	let th = tr.children
	// console.log(th)
	let memberID = th[0].innerText;
	//console.log("memberID="+memberID);
	let memberName = th[1].innerText

	var tr_class = "";
	if (memberID % 2 == 0) {
		tr_class = "even";
	} else {
		tr_class = "odd";
	}

	let chngeState = confirm('您確定要修改"'+memberName+'"的權限嗎?')
	if(chngeState){
		$.post({
		url: "/backend/member/Ajax",
		data: {
			"memberID": memberID
		},
		datatype: 'json',
		success: function (data) {
			//console.log(data)

			let html = `
		      <tr class=${tr_class}>
		      <th class="sorting_1">${data.memberId}</th>
		      <td>${data.memberName}</td>
		      <td>${data.memberAccount}</td>
		      <td>${data.memberEmail}</td>
		      <td>${data.memberPhone}</td>
		      <td>${data.memberAddress}</td>
		      <td>${(data.memberGender == 0) ? "男" : "女"}</td>
		      <td>${data.memberBirthday}</td>
		      <td>${(data.memberState == 2) ? "停權" : (data.memberState == 0) ? "未驗證" : "已驗證"}</td>
		      <td><button id="stateButton" class="btn btn-secondary rounded-pill m-2">${(data.memberState == 2) ? "復原" : "停權"}</button></td>  
		      </tr>      
		      `;
			$(tr.previousSibling).after(html)

			tr.remove()

		}
	})
	}
	


})