// CINEWAVE - Auth Modal Controller

function openModal(viewId) {
    $('authModal').classList.add('is-open');
    document.body.style.overflow = 'hidden';
    switchView(viewId);
}

function closeModal() {
    $('authModal').classList.remove('is-open');
    document.body.style.overflow = '';
    clearFormErrors();
}

function handleOverlayClick(event) {
    if (event.target === $('authModal')) closeModal();
}

function switchView(viewId) {
    $$('.auth-view').forEach((view) => (view.style.display = 'none'));
    $(viewId).style.display = 'block';
    clearFormErrors();
}

function togglePassword(inputId, btn) {
    const input = $(inputId);
    const icon = btn.querySelector('i');
    const isHidden = input.type === 'password';
    input.type = isHidden ? 'text' : 'password';
    icon.className = isHidden ? 'fa-regular fa-eye' : 'fa-regular fa-eye-slash';
}

function clearFormErrors() {
    $$('.form-error').forEach((el) => el.remove());
    $$('.form-input.is-error').forEach((el) => el.classList.remove('is-error'));
}

function showFieldError(fieldName, message) {
    const form = $('registerForm');
    const input = form.querySelector(`[name="${fieldName}"]`);
    if (!input) return;

    input.classList.add('is-error');

    const parent = input.closest('.input-password-wrapper') ?? input.parentElement;
    parent.parentElement.querySelector('.form-error')?.remove();

    const errorEl = document.createElement('span');
    errorEl.className = 'form-error';
    errorEl.textContent = message;
    parent.parentElement.appendChild(errorEl);
}

function handleLoginSubmit(event) {
    event.preventDefault();

    const form = event.target;
    const submitBtn = form.querySelector('.btn-auth-submit');
    const originalText = submitBtn.textContent;
    const username = form.querySelector('[name="username"]').value.trim();
    const password = form.querySelector('[name="password"]').value;
    const csrfToken = form.querySelector('input[name="_csrf"]').value;

    if (!username || !password) {
        showToast('Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.', false);
        return;
    }

    submitBtn.disabled = true;
    submitBtn.textContent = 'ĐANG XỬ LÝ...';

    const formData = new URLSearchParams({ username, password, _csrf: csrfToken });

    fetch('/process-login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'X-Requested-With': 'XMLHttpRequest',
        },
        body: formData.toString(),
    })
        .then((res) => res.json().then((body) => ({ status: res.status, body })))
        .then(({ body }) => {
            if (body.success) {
                showToast('Đăng nhập thành công! Đang tải...', true);
                setTimeout(() => window.location.reload(), 1200);
            } else {
                showToast(body.message ?? 'Sai tài khoản hoặc mật khẩu!', false);
            }
        })
        .catch(() => showToast('Lỗi kết nối, vui lòng thử lại sau.', false))
        .finally(() => {
            submitBtn.disabled = false;
            submitBtn.textContent = originalText;
        });
}

function handleRegisterSubmit(event) {
    event.preventDefault();
    clearFormErrors();

    const form = event.target;
    const submitBtn = form.querySelector('.btn-auth-submit');
    const originalText = submitBtn.textContent;
    const csrfToken = form.querySelector('input[name="_csrf"]').value;

    const data = {
        fullName: form.fullName.value.trim(),
        username: form.username.value.trim(),
        email: form.email.value.trim(),
        phoneNumber: form.phoneNumber.value.trim(),
        password: form.password.value,
        confirmPassword: form.confirmPassword.value,
    };

    submitBtn.disabled = true;
    submitBtn.textContent = 'ĐANG XỬ LÝ...';

    fetch('/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': csrfToken,
        },
        body: JSON.stringify(data),
    })
        .then((res) => res.json().then((body) => ({ ok: res.ok, body })))
        .then(({ ok, body }) => {
            if (ok && body.success) {
                showToast('Đăng ký thành công! Vui lòng đăng nhập.', true);
                form.reset();

                const checkbox = $('agreeTerms');
                const registerBtn = $('registerSubmitBtn');
                if (checkbox) checkbox.checked = false;
                if (registerBtn) registerBtn.disabled = true;

                setTimeout(() => switchView('loginView'), 1500);
            } else if (body.errors) {
                let hasFieldErrors = false;

                Object.entries(body.errors).forEach(([field, msg]) => {
                    if (field === 'globalError') {
                        showToast(msg, false);
                    } else {
                        showFieldError(field, msg);
                        hasFieldErrors = true;
                    }
                });

                if (hasFieldErrors) showToast('Vui lòng kiểm tra lại thông tin đăng ký.', false);
            } else if (body.message) {
                showToast(body.message, false);
            }
        })
        .catch(() => showToast('Lỗi kết nối, vui lòng thử lại sau.', false))
        .finally(() => {
            const checkbox = $('agreeTerms');
            submitBtn.disabled = !(checkbox?.checked);
            submitBtn.textContent = originalText;
        });
}

document.addEventListener('DOMContentLoaded', () => {
    $('loginForm')?.addEventListener('submit', handleLoginSubmit);
    $('registerForm')?.addEventListener('submit', handleRegisterSubmit);

    const agreeCheckbox = $('agreeTerms');
    const registerBtn = $('registerSubmitBtn');
    if (agreeCheckbox && registerBtn) {
        agreeCheckbox.addEventListener('change', function () {
            registerBtn.disabled = !this.checked;
        });
    }

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') closeModal();
    });
});
