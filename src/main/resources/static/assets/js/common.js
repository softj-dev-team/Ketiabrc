//ajax
function postCallAjax(url, data, success){
	$.ajax({
		type:'post',
		url: url,
		data: data,
		success: success,
		error: function(e){
			console.log(e);
			alert('ERROR');
		}
	});
}

//선택 이미지 출력 및 파일명 노출
//부모의 형제 parent().siblings()
//형제 siblings()
$('.uploadBtn').on('change', function(object){
    var sel_file;
    var thisObject = $(this);
    var filename = thisObject.val().split('/').pop().split('\\').pop();
    var files = object.target.files;
    var filesArr =Array.prototype.slice.call(files);
    filesArr.forEach(function (f) {
        if(!f.type.match("image.*")){
            alert(getMessageAjax('msg_review_1-8-1'));
            return false;
        }
        sel_file = f;
        var reader = new FileReader();
        reader.onload = function (e) {
            var imgDisplayType ='copy';
            // if(imgDisplayType =="copy"){
            //     $('.uploadBtn').parent().siblings('img').attr("src",e.target.result);
            //     $('.uploadBtn').siblings('.fileName').val(filename);
            // }else{
                thisObject.parent().siblings('img').attr("src",e.target.result);
                thisObject.siblings('.fileName').val(filename);
            // }

        }
        reader.readAsDataURL(f);
    })
});

//file upload
var uploadFile = $('.uploadBtn');
uploadFile.on('change', function(){
    if(window.FileReader){
        var filename = $(this)[0].files[0].name;
    } else {
        var filename = $(this).val().split('/').pop().split('\\').pop();
    }
    $(this).siblings('.fileName').val(filename);
});

$("#cancle").click(function(){
    history.back();
});

//삭제버튼
function delBoard(brdno){
	var formData = {
			brdno: brdno,
	}
	postCallAjax('/api/deleteBoard', formData, function(data){
	    location.href=data.redirectUrl;
	});
}

/**
 * 이미지 파일 업로드
 */
function uploadSummernoteImageFile(file, editor) {
    data = new FormData();
    data.append("file", file);

    $.ajax({
        data : data,
        type : "POST",
        url : "/api/uploadSummernoteImageFile",
        contentType : false,
        processData : false,
        success : function(data) {
            console.log(data);
            //항상 업로드된 파일의 url이 있어야 한다.
            $(editor).summernote('insertImage', data.url);
        }
    });
}