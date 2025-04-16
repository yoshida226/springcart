document.addEventListener('DOMContentLoaded', function () {
	const dropdownItems = document.querySelectorAll('.js-dropdown-item');
	const dropdownInput = document.getElementById('dropdownInput');
//	const hiddenQuantity = document.getElementById('hiddenQuantity');

	dropdownItems.forEach(item => {
		item.addEventListener('click', function (e) {
			e.preventDefault();
			const selectedValue = this.getAttribute('data-value');

			// ボタン表示更新
			dropdownInput.value = selectedValue;

			// hiddenフィールド更新（Springのフォームにバインドされる）
//			hiddenQuantity.value = selectedValue;
		});
	});
})