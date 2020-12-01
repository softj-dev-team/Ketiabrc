$('.rs_cate_id').click(function(e){
    e.preventDefault();
    var eq_v = $(this).attr('value');
    var rs_max_time = $(this).attr('rs-max-time');

    $(this).parents('.flexwrap').find('input[name=rs_cate_id]').val(eq_v);
    $(this).parents('.flexwrap').find('input[name=rs_max_time]').val(rs_max_time);
});


$('#resUpdateBtn').on("click",function () {
    var formData = $('#resUpdateForm').serialize();

    postCallAjax('/api/resUpdate', formData, function(data){
        if(data.msg == 'success'){
            location.reload();
        } else if(data.msg == "LoginFail"){
            alert("로그인 후 수정 가능합니다.");
        } else if(data.msg == "idFail"){
            alert("예약자 정보가 일치하지 않습니다.");
        } else if(data.msg == "timeError"){
            alert("예약 시간을 선택해주세요.");
        } else if(data.msg == "reCountFail"){
            alert("예약이 초과되었습니다. 예약시간을 확인해주세요.");
        } else {
            alert("ERROR");
        }
    });
})


$('.catearr').click(function(){
   $(this).parent('li').toggleClass('on');
});
$('.eqCateLi > a').click(function(){
   $(this).parent('li').toggleClass('on');
});
$('.ver2 .catesub li a').click(function(e){
    e.preventDefault();
    $('.ver2 .catesub li a').removeClass('on');
    $(this).addClass('on');
    var eq = $(this).text();
    $(this).parents('.flexwrap').find('#eqname').val(eq);
});
$('.canclebtn').click(function(){
    $(this).parents('#eqpop').removeClass('on');
    $(this).parents('.eqpop_insert').removeClass('on');
    // location.reload();
    $('select').each(function(){
       $(this).find('option:first').attr('selected','true');
    });
    /*$('input').each(function(){
       $(this).val('');
    });*/
    $(':text:not([id=user_name]):not([id=hp])').val('');
})

$('.catesub a').click(function(){
    $('.catesub a').removeClass('on');
    $(this).addClass('on');
});

$('#resBtn').on("click",function () {
    var formData = $('#resUpdateForm').serialize();
    //시작 시간
    var rs_start_time = $('select[name=rs_start_time]').val().substring(0,2) * 60;
    //종료 시간
    var rs_end_time = $('select[name=rs_end_time]').val().substring(0,2) * 60;
    //시작 분 계산
    var rs_start_time2 = $('select[name=rs_start_time]').val().substring(3,5);
    var rs_end_time2 = $('select[name=rs_end_time]').val().substring(3,5);
    if(rs_start_time2 == '30'){
        rs_start_time += 30;
    }
    if(rs_end_time2 == '30'){
        rs_end_time += 30;
    }
    var rs_max_time = $('input[name=rs_max_time]').val() * 60;
    var result = rs_end_time - rs_start_time;

    if(rs_max_time < result){
        alert("예약 최대시간을 초과했습니다.");
    } else {
        postCallAjax('/api/resSave', formData, function(data){
            if(data.msg == 'success'){
                location.reload();
            } else if(data.msg == "LoginFail"){
                alert("로그인후 이용 가능합니다.");
            } else if(data.msg == "grantFail"){
                alert("예약 권한이 없습니다.");
            } else if(data.msg == "timeError"){
                alert("예약 시간을 선택해주세요.");
            } else if(data.msg == "reCountFail"){
                alert("예약이 초과되었습니다. 예약시간을 확인해주세요.");
            } else {
                alert("ERROR");
            }
        });
    }
})

$('body').on('click', function(e){
    var $tgPoint = $(e.target);
    var $popArea = $tgPoint.parents().hasClass('rcpop');
    if ( !$popArea ){
        $('.rcpop').remove();
    }
});

$(".btn-eq3").click(function(){
    $('#eqpop').addClass('on');
    $("#eqpop input[name=eqname]").val($('.btn-eq2').text());
    $("#eqpop input[name=rs_cate_id]").val(getParam("rs_cate_id"));
    $("#resBtn").show();
    $("#resUpdateBtn").hide();
});