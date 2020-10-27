//summernote
$(document).ready(function (){
	$('#summernote').summernote({
		toolbar:[
			['style', ['style']],
			['font', ['bold', 'underline', 'clear']],
			['color', ['color']],
			['fontsize', ['fontsize']],
			['font', ['strikethrough', 'superscript', 'subscript']],
			['para', ['ul', 'ol', 'paragraph']],
			['table', ['table']],
			['insert', ['link', 'picture', 'video']],
			['view', ['fullscreen', 'codeview', 'help']]
		],
		tabsize: 2,
		height: 250,
		lang: 'ko-KR', // default: 'en-US'
		callbacks: {
			onImageUpload : function(files) {
				uploadSummernoteImageFile(files[0],this);
			}
		}
	});
});
// 201021  추가
$(function(){
	$('#all-chk').click(function(){
		var chk = $(this).is(':checked');//.attr('checked');
		if(chk) $('.file-up-con input').prop('checked',true);
		else $('.file-up-con input').prop('checked',false);
	});
});
$('.ch-remove').click(function(){
	var formData = $('#boardForm').serialize();

	console.log(formData);

	if(!formData.includes("chk")){
		alert("항목을 선택해주세요.");
	} else if(confirm("삭제하시겠습니까?")){
		postCallAjax('/api/ListDelete', formData, function (data) {
			console.log(data);
			if(data.msg == 'success'){
				$(".file-up-con input[type=checkbox]:checked").each(function(){
					var tr_value =$(this).val();
					var tr=$("li[file-value='"+tr_value+"']");
					tr.remove();
				});
			} else if(data.msg == "pkName"){
				alert("ERROR");
			} else if(data.msg == "table"){
				alert("ERROR");
			} else {
				alert("ERROR");
			}
		})
	}else{
		return false;
	}
});

//게시판 글쓰기
function chkInputValue(id, msg){
	if ( $.trim($(id).val()) == "") {
		alert(msg+" "+ "입력해주세요.");
		$(id).focus();
		return false;
	}
	return true;
}

function fn_formSubmit(login){
	if ( ! chkInputValue("#brdwriter", "작성자를")) return;
	if ( ! chkInputValue("#brdtitle", "글 제목을")) return;
	if ( ! chkInputValue(".brdmemo", "글 내용을")) return;
	// if(!login){
	//     if ( ! chkInputValue("#password", "비밀번호를")) return;
	// }

	$("#boardForm").submit();
}