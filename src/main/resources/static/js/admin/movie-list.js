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
    let currentDeleteForm = null;

    document.querySelectorAll('form[data-confirm]').forEach(form => {
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            currentDeleteForm = form;
            const desc = form.dataset.confirm;
            if (desc) $('deleteConfirmDesc').textContent = desc;
            $('deleteConfirmModal').classList.add('show');
        });
    });

    window.hideDeleteConfirm = () => {
        $('deleteConfirmModal').classList.remove('show');
        currentDeleteForm = null;
    };

    window.submitDeleteForm = () => {
        if (currentDeleteForm) currentDeleteForm.submit();
    };
});
