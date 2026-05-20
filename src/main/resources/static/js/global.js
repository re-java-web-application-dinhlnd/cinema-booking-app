// CINEWAVE - Global Utilities

const $ = (id) => document.getElementById(id);
const $$ = (selector) => document.querySelectorAll(selector);

// === TOAST ===
function showToast(message, type) {
    const container = $('toastContainer');
    if (!container) return;

    // Support both boolean (true = success) and string ('success'/'error')
    const isSuccess = type === true || type === 'success';

    const toast = document.createElement('div');
    toast.className = `cw-toast ${isSuccess ? 'success' : 'error'}`;

    const icon = document.createElement('i');
    icon.className = isSuccess ? 'fa-solid fa-circle-check' : 'fa-solid fa-circle-xmark';

    const text = document.createElement('span');
    text.textContent = message;

    const closeBtn = document.createElement('button');
    closeBtn.className = 'cw-toast-close';
    closeBtn.innerHTML = '<i class="fa-solid fa-xmark"></i>';
    closeBtn.addEventListener('click', () => removeToast(toast));

    toast.append(icon, text, closeBtn);
    container.appendChild(toast);

    setTimeout(() => removeToast(toast), 4000);
}

function removeToast(toast) {
    if (!toast.parentNode) return;
    toast.style.animation = 'toastFadeOut 0.3s ease forwards';
    setTimeout(() => toast.parentNode?.removeChild(toast), 300);
}

// === LOGOUT CONFIRM ===
function showLogoutConfirm() {
    $('logoutConfirm')?.classList.add('is-open');
    document.body.style.overflow = 'hidden';
}

function hideLogoutConfirm() {
    $('logoutConfirm')?.classList.remove('is-open');
    document.body.style.overflow = '';
}

function submitLogout() {
    $('logoutForm')?.submit();
}

// === INIT ===
document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);

    if (params.get('loggedOut') === 'true') {
        showToast('Bạn đã đăng xuất thành công.', true);
        history.replaceState(null, '', window.location.pathname);
    } else if (params.get('error') === 'true') {
        showToast('Tên đăng nhập hoặc mật khẩu không đúng.', false);
        history.replaceState(null, '', window.location.pathname);
    }

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') hideLogoutConfirm();
    });
});
