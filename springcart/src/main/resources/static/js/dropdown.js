const dropdownItems = document.querySelectorAll('.js-dropdown-item');
const inputField = document.getElementById('dropdownInput');

document.addEventListener('DOMContentLoaded', function () {
	dropdownItems.forEach(item => {
		item.addEventListener('click', function (e) {
			e.preventDefault(); // リンクのデフォルト動作を無効化
			const selectedValue = this.getAttribute('data-value'); // data-value 属性から値を取得
			inputField.value = selectedValue; // 選択された値を input に設定
		});
	});
});