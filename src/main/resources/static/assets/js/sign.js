$(window).on("load",function(){
    $(".tableBody").mCustomScrollbar({
        autoHideScrollbar:true,
        theme:"dark"
    });
});
$('.numbercheck').on('click', function(){
    $('#memnumber').addClass('on');
})
$('.tableBody ul').click(function(){
    $('#memnumber').removeClass('on');
    var num = $(this).children('li:nth-child(1)').text()
    var name = $(this).children('li:nth-child(2)').text()
/*    $('#name').val(name);
    $('#id').val(num);*/
    $('#user_id').val(name);

})

$(".cc-btn").click(function(){
    $(this).parents(".modal").removeClass('on');
    $('body').css('overflow', 'auto');
});



var pwCheck = false;
$('#signUpSubmit').on("click",function () {
    var password = $('#password').val();
    var password_cf = $('#password_cf').val();
    var formData = $('#defaultJoinform').serialize();

    if(password != password_cf){
        alert('비밀번호를 확인해주세요.');
        return;
    }

    postCallAjax('/api/signupProc', formData, function(data){
        if(data.msg == 'success'){
            location.href=data.redirectUrl;
        } else if(data.msg == "idFail"){
            alert("이미 등록된 아이디 입니다.");
        } else {
            alert("ERROR");
        }
    });

})

var regExp = /^.{1,8}$/;
$(document).on('input','input[name=password],input[name=password_cf]',function () {
    var password = $('#password').val();
    var password_cf = $('#password_cf').val();
    pwCheck = false;

    if(!regExp.test(password)){
        $(".er_pw").text('최대 8글자까지 입력 가능합니다.');
    } else {
        if(password != password_cf){
            $(".er_pw").text('');
            $(".er_pwcf").text('비밀번호가 일치하지 않습니다.');
        } else {
            $(".er_pwcf").text('');
            pwCheck = true;
        }
    }
});