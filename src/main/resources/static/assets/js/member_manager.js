$(".cc-btn").click(function(){
    $(this).parents(".modal").removeClass('on');
    $('body').css('overflow', 'auto');
});

$(".mem-set-btn").click(function(){
    $("#mem-set").addClass('on');
});
$(".mem-add-btn").click(function(){
    $("#mem-add").addClass('on');
});

$(".authority input:radio[name=authority-rd6]").click(function(){

    if($("input[name=authority-rd6]:checked").val() == "T1"){
        $(".noti-ck-box *").attr("disabled",false);

    }else if($("input[name=authority-rd6]:checked").val() == "T2"){
        $(".noti-ck-box *").attr("disabled",true);
    }
});
// 체크박스
$('#mem-ck').click(function(){
    var chk = $(this).is(':checked');//.attr('checked');
    if(chk) $('.mem-tb input').prop('checked',true);
    else $('.mem-tb input').prop('checked',false);
});

function userGrantUp(usr_no){
    // e.preventDefault();
    console.log(usr_no);
    $('input[name=usr_no]').val(usr_no);
}

function adminDrawUser(user_id){
    if(!confirm("회원탈퇴 후 복구 할 수 없습니다.")) {
        return;
    } else {
        postCallAjax('/api/withdrawProc', {user_id:user_id}, function (data) {
            if (data.msg == 'success') {
                location.href = data.redirectUrl;
            } else {
                alert("ERROR");
            }
        });
    }
}

//등급 변경
$('#grantChange').on("click",function () {
    var formData = $('#grantForm').serialize();

    postCallAjax('/api/grantChange', formData, function(data){
        if(data.msg == 'success'){
            location.href=data.redirectUrl;
        } else {
            alert("ERROR");
        }
    });
})

//사번등록
$('#empBtn').on("click",function () {
    var formData = $('#empForm').serialize();

    postCallAjax('/api/empSave', formData, function(data){
        if(data.msg == 'success'){
            location.href=data.redirectUrl;
        } else {
            alert("ERROR");
        }
    });
})

