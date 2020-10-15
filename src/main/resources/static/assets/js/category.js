$(".datepicker").datepicker({
    monthNames: ['01','02','03','04','05','06','07','08','09','10','11','12'], // 월의 한글 형식.\
    monthNamesShort: [ '01','02','03','04','05','06','07','08','09','10','11','12' ],
    dateFormat : "yy-mm-dd"
});
$('.catearr').click(function(){
   $(this).parent('li').toggleClass('on');
});
$('.eqCateLi > a').click(function(){
   $(this).parent('li').toggleClass('on');
});
$('.catesub li a').click(function(e){
    e.preventDefault();
    $('.catesub li a').removeClass('on');
    $(this).addClass('on');
    var eq = $(this).text();
    var rs_max_quantity = $(this).attr("rs-max-quantity");
    var rs_max_time = $(this).attr("rs-max-time");
    $(this).parents('.flexwrap').find('#eqname').val(eq);
    $(this).parents('.flexwrap').find('#rs_max_quantity').val(rs_max_quantity);
    $(this).parents('.flexwrap').find('#rs_max_time').val(rs_max_time);
});

$('.rs_cate_id').click(function(e){
    e.preventDefault();
    var eq_v = $(this).attr('value');
    $(this).parents('.flexwrap').find('input[name=rs_cate_id]').val(eq_v);
});

$('#cateUpdate').on("click",function () {
    var formData = $('#cateForm').serialize();

    postCallAjax('/api/cateUpdate', formData, function(data){
        if(data.msg == 'success'){
            location.href=data.redirectUrl;
        } else {
            alert("ERROR");
        }
    });

})