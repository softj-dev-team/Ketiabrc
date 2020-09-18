$('.rs_cate_id').click(function(e){
    e.preventDefault();
    var eq_v = $(this).attr('value');
    $(this).parents('.flexwrap').find('input[name=rs_cate_id]').val(eq_v);
});


$('#resUpdateBtn').on("click",function () {
    var formData = $('#resUpdateForm').serialize();

    postCallAjax('/api/resUpate', formData, function(data){
        if(data.msg == 'success'){
            location.reload();
        } else if(data.msg == "LoginFail"){
            alert("로그인 후 수정 가능합니다.");
        } else if(data.msg == "idFail"){
            alert("예약자 정보가 일치하지 않습니다.");
        } else {
            alert("ERROR");
        }
    });
})
