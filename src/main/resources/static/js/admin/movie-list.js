/**
 * Movie List Page — Toast messages & confirm dialogs
 */
document.addEventListener('DOMContentLoaded', () => {

    // Auto-show flash toast messages
    document.querySelectorAll('.toast-auto').forEach(el => {
        if (window.showToast) showToast(el.textContent, el.dataset.type);
        el.remove();
    });

    // Confirm dialog for forms with data-confirm
    document.querySelectorAll('form[data-confirm]').forEach(form => {
        form.addEventListener('submit', (e) => {
            if (!confirm(form.dataset.confirm)) {
                e.preventDefault();
            }
        });
    });
});
