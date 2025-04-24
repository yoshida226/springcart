document.addEventListener('DOMContentLoaded', function () {
    // 全ての画像ファイル入力に対してイベントを設定
    document.querySelectorAll('input[type="file"][name="imageContent"]').forEach(function (input) {
        input.addEventListener('change', function (event) {
	        const file = event.target.files[0];
	        if (!file || !file.type.startsWith('image/')) return;

	        const reader = new FileReader();
	
			//読み込んだファイルでプレビューを変更
	        reader.onload = function (e) {
	          // inputの直前の img を見つけて更新（構造から逆算）
	          const imgPreview = input.closest('.col-md-3').querySelector('img');
	          imgPreview.src = e.target.result;
	        };
	
			//ファイルの読み込み
	        reader.readAsDataURL(file);
        });
    });
});