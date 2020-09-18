$('#pwChBtn').on("click",function () {
    var password = $('#password').val();
    var password_cf = $('#password_cf').val();
    var formData = $('#pwChform').serialize();

    if(password != password_cf){
        alert("비밀번호가 일치하지 않습니다.");
    }

    postCallAjax('/api/pwChProc', formData, function(data){
        if(data.msg == 'success'){
            location.href=data.redirectUrl;
        } else {
            alert("ERROR");
        }
    });

})