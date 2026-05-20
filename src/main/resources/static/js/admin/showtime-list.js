/**
 * Showtime List Page — Toast messages & confirm dialogs
 */
document.addEventListener('DOMContentLoaded', () => {

    // Auto-show flash toast messages
    $$('.toast-auto').forEach(el => {
        if (window.showToast) showToast(el.textContent, el.dataset.type);
        el.remove();
    });

    // Confirm dialog for forms with data-confirm
    let currentHideForm = null;

    $$('form[data-confirm]').forEach(form => {
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            currentHideForm = form;
            const desc = form.dataset.confirm;
            if (desc) $('hideConfirmDesc').textContent = desc;
            $('hideConfirmModal').classList.add('show');
        });
    });

    window.hideHideConfirm = () => {
        $('hideConfirmModal').classList.remove('show');
        currentHideForm = null;
    };

    window.submitHideForm = () => {
        if (currentHideForm) currentHideForm.submit();
    };

    // Close on Escape
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') hideHideConfirm();
    });
});
