$("textarea.autosize").on('keydown keyup', function () {
    $(this).height(1).height( $(this).prop('scrollHeight')-20 );
});
$(document).on('click','.remove-btn','.re-com-btn', function () {
    $(this).parent().parent().parent().remove();
});
$('.re-com-btn').click(function(){
        $(this).parent().parent().parent().next('.com-rcom').addClass('on');
        $(this).parent().parent().parent().children('.com-rcom').addClass('on');
        $('.com-rcom textarea').focus();
    })
$('.re-com-rebtn').click(function(){
    $(this).parent().parent().parent().removeClass('on');
    $(this).parent().parent().removeClass('on');
})

$(document).on('click', '.re-modify button', function(){
	var modiT = $(this).siblings('textarea').val();
	var reply = $(this).parent('.re-modify').prev('.com-r');
	reply.css('display', 'block');
	reply.text(modiT);
	reply.prev('.com-btn').find('.modi-btn').text('수정');
	$(this).parent('.re-modify').remove();
});


function chkInputValue(id, msg){
	if ( $.trim($(id).val()) == "") {
		alert(msg+" 입력해주세요.");
		$(id).focus();
		return false;
	}
	return true;
}

function fn_SafeFormSubmit(){
	if ( ! chkInputValue("#rememo1", "글 내용을")) return;

	if($('#rewriter1').val() == null || $('#rewriter1').val() == ""){
		alert("로그인 후 이용 가능합니다.");
		return;
	}

	var formData = $("#form1").serialize();
	$.ajax({
		url: "/boardReplySave",
		type:"post",
		data : formData,
		success: function(result){
			if (result!=="") {
				$("#rememo1").val("");
				$(".com-renderer").append(result);
				alert("저장되었습니다.");
			} else{
				alert("서버에 오류가 있어서 저장되지 않았습니다.");
			}
		}
	})
}

function fn_replyDelete(reno){
	if (!confirm("삭제하시겠습니까?")) {
		return;
	}
	$.ajax({
		url: "/boardReplyDelete",
		type:"post",
		data: {"reno": reno},
		success: function(result){
			if (result=="OK") {
				$("#replyItem"+reno).remove();
				alert("삭제되었습니다.");
			} else{
				alert("답변이 있어서 삭제할 수 없습니다.");
			}
		}
	})
}

function fn_replyReplySave(){
	if ( ! chkInputValue("#rememo3", "글 내용을")) return;

	var formData = $("#form3").serialize();
	$.ajax({
		url: "/boardReplySave",
		type:"post",
		data : formData,
		success: function(result){
			if (result!=="") {
				var parent = $("#reparent").val();
				$("#replyItem"+parent).after(result);
				alert("저장되었습니다.");
				$("#rememo3").val("");
			} else{
				alert("서버에 오류가 있어서 저장되지 않았습니다.");
			}
		}
	})
}
