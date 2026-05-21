document.addEventListener('DOMContentLoaded', () => {
    const $ = (id) => document.getElementById(id);
    const $$ = (selector) => document.querySelectorAll(selector);

    $$('.toast-auto').forEach(el => {
        if (window.showToast) showToast(el.textContent, el.dataset.type);
        el.remove();
    });

    let currentDeleteForm = null;

    $$('form[data-confirm]').forEach(form => {
        form.addEventListener('submit', (e) => {
            e.preventDefault();
            currentDeleteForm = form;
            
            // Set dynamic title & description
            const desc = form.dataset.confirm;
            if (desc) $('deleteConfirmDesc').textContent = desc;

            const title = form.dataset.title || 'Xác nhận Thao tác';
            $('deleteConfirmTitle').textContent = title;

            // Set dynamic styles depending on action type (danger or success)
            const type = form.dataset.type || 'danger';
            const iconEl = $('confirmIcon');
            const iconSymbolEl = $('confirmIconSymbol');
            const submitBtnEl = $('confirmSubmitBtn');

            if (type === 'success') {
                // Success/Restore Mode (Green UI)
                iconEl.className = 'confirm-icon success';
                iconSymbolEl.className = 'fa-solid fa-circle-check';
                submitBtnEl.className = 'confirm-btn success';
            } else {
                // Danger/Hide Mode (Red UI)
                iconEl.className = 'confirm-icon';
                iconSymbolEl.className = 'fa-solid fa-triangle-exclamation';
                submitBtnEl.className = 'confirm-btn danger';
            }

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
