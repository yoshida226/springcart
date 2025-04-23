document.addEventListener('DOMContentLoaded', function () {
	console.log("test1");
    // 全ての画像ファイル入力に対してイベントを設定
    document.querySelectorAll('input[type="file"][name="imageContent"]').forEach(function (input) {
	console.log("test2");
        input.addEventListener('change', function (event) {
	console.log("test3");
	        const file = event.target.files[0];
	        if (!file || !file.type.startsWith('image/')) return;

			console.log("test4");
	        const reader = new FileReader();
	
			//読み込んだファイルでプレビューを変更
	        reader.onload = function (e) {
	          // inputの直前の img を見つけて更新（構造から逆算）
				console.log("test5");
	          const imgPreview = input.closest('.col-md-3').querySelector('img');
				console.log("test6");
	          imgPreview.src = e.target.result;
	        };
	
			//ファイルの読み込み
	        reader.readAsDataURL(file);
        });
    });
});