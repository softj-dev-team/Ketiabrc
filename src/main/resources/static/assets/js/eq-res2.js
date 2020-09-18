$(".datepicker").datepicker({
    monthNames: ['01','02','03','04','05','06','07','08','09','10','11','12'], // 월의 한글 형식.\
    monthNamesShort: [ '01','02','03','04','05','06','07','08','09','10','11','12' ],
    dateFormat : "yy-mm-dd"
});
$('.catearr').click(function(){
   $(this).parent('li').toggleClass('on');
});
$('.catesub li a').click(function(e){
    e.preventDefault();
    $('.catesub li a').removeClass('on');
    $(this).addClass('on');
    var eq = $(this).text();
    // $(this).parents('.flexwrap').find('#eqname').val(eq);
    $(this).parents('.flexwrap').find('#eqname').val(eq);
});

$('.rs_cate_id').click(function(e){
    e.preventDefault();
    var eq_v = $(this).attr('value');
    $(this).parents('.flexwrap').find('input[name=rs_cate_id]').val(eq_v);
});

$('#resBtn').on("click",function () {
    var formData = $('#resForm').serialize();

    postCallAjax('/api/resSave', formData, function(data){
        if(data.msg == 'success'){
            location.href=data.redirectUrl;
        } else if(data.msg == "LoginFail"){
            alert("로그인후 이용 가능합니다.");
        } else if(data.msg == "grantFail"){
            alert("예약 권한이 없습니다.");
        } else if(data.msg == "reCountFail"){
            alert("예약초과");
        } else {
            alert("ERROR");
        }
    });

})