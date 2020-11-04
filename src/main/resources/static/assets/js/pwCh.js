$('#pwChBtn').on("click",function () {
    var password = $('#password').val();
    var password_cf = $('#password_cf').val();
    var formData = $('#pwChform').serialize();

    if(password == ""){
        alert("비밀번호를 입력해주세요.")
    } else if(password_cf == ""){
        alert("비밀번호를 입력해주세요.")
    } else if(password != password_cf){
        alert("비밀번호가 일치하지 않습니다.");
    } else {
        postCallAjax('/api/pwChProc', formData, function(data){
            if(data.msg == 'success'){
                location.href=data.redirectUrl;
            } else {
                alert("ERROR");
            }
        });
    }


})

$('#withdrawUser').on("click",function () {
    if(!confirm("회원탈퇴 후 복구 할 수 없습니다.")) {
        return;
    } else {
        postCallAjax('/api/withdrawProc', {}, function (data) {
            if (data.msg == 'success') {
                location.href = data.redirectUrl;
            } else {
                alert("ERROR");
            }
        });
    }
})