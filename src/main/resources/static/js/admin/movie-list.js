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
