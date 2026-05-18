// ==========================================================================
//  CINEWAVE - Auth Modal Controller
// ==========================================================================

function openModal(viewId) {
    document.getElementById('authModal').classList.add('is-open');
    document.body.style.overflow = 'hidden';
    switchView(viewId);
}

function closeModal() {
    document.getElementById('authModal').classList.remove('is-open');
    document.body.style.overflow = '';
}

function handleOverlayClick(event) {
    if (event.target === document.getElementById('authModal')) {
        closeModal();
    }
}

function switchView(viewId) {
    document.querySelectorAll('.auth-view').forEach(function (view) {
        view.style.display = 'none';
    });
    document.getElementById(viewId).style.display = 'block';
}

function togglePassword(inputId, btn) {
    const input = document.getElementById(inputId);
    const icon = btn.querySelector('i');
    if (input.type === 'password') {
        input.type = 'text';
        icon.className = 'fa-regular fa-eye';
    } else {
        input.type = 'password';
        icon.className = 'fa-regular fa-eye-slash';
    }
}

// Tự động mở modal nếu server trả về lỗi hoặc vừa đăng ký xong
document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    if (urlParams.get('error') === 'true') {
        openModal('loginView');
    }
    if (urlParams.get('registered') === 'true') {
        openModal('loginView');
    }

    // Đóng modal bằng phím Escape
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
            closeModal();
        }
    });
});
