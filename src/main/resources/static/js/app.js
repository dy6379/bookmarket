$(function () {
  //console.log(1);
  $('a.deleteConfirm').click(function () {
    if (!confirm('삭제하겠습니까?')) return false; // 취소시 삭제 안됨
  });

  if ($('#content').length) {
    ClassicEditor.create(document.querySelector('#content')).catch((error) => {
      console.error(error);
    });
  }

  if ($('#description').length) {
    ClassicEditor.create(document.querySelector('#description')).catch((error) => {
      console.error(error);
    });
  }
});

$( function() {
    $( "#publicdate" ).datepicker({
		dateFormat : 'yy-mm-dd',
		showOn: "button",
      buttonText: "날짜 선택",
      prevText: '이전 달   ',
	    nextText: '다음 달',
	    monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
	    monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
	    dayNames: ['일','월','화','수','목','금','토'],
	    dayNamesShort: ['일','월','화','수','목','금','토'],
	    dayNamesMin: ['일','월','화','수','목','금','토'],
	    yearSuffix: '년',
	    changeMonth: true,
        changeYear: true
	});
  } );

function readURL(input) {
  //0.파일(이미지)가 있을경우에 실행
  if (input.files && input.files[0]) {
    let reader = new FileReader(); //1.파일을 읽는 자바스크립트 reader객체 생성

    reader.readAsDataURL(input.files[0]); //2.reader로 입력객체의 첫번째 파일을 경로읽기 (files는 배열이므로[0])

    //3.이벤트 onload(파일을 성공적으로 다읽었을때) => 화면 페이지의 img태그에 reader로 읽은 경로 결과를 속성src에 입력
    // 가로 세로 100% 로 이미지 사이즈 표시
    reader.onload = function (e) {
      $('#imgPreview').attr('src', e.target.result).width(200).show();
    };
  }
}

$(function(){
	var radios = document.getElementsByName('bookCondition');

	radios.forEach(elem => {
	    if(elem.checked){
	        console.log(elem.value);
	    }
	});

});

