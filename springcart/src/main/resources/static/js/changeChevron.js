document.addEventListener('DOMContentLoaded', function () {
    const buttons = document.querySelectorAll('[data-bs-toggle="collapse"]');
    buttons.forEach(btn => {
        const targetId = btn.getAttribute('data-bs-target');
        const icon = btn.querySelector('i');
        const collapseEl = document.querySelector(targetId);
        collapseEl.addEventListener('show.bs.collapse', () => {
            icon.classList.remove('bi-chevron-down');
            icon.classList.add('bi-chevron-up');
        });
        collapseEl.addEventListener('hide.bs.collapse', () => {
            icon.classList.remove('bi-chevron-up');
            icon.classList.add('bi-chevron-down');
        });
    });
});