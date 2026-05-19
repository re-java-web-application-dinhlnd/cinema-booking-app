function switchTab(tabId) {
    // Update buttons
    document.querySelectorAll('.nav-btn').forEach(btn => btn.classList.remove('active'));
    event.currentTarget.classList.add('active');

    // Update panes
    document.getElementById('tab-info').style.display = tabId === 'info' ? 'block' : 'none';
    document.getElementById('tab-password').style.display = tabId === 'password' ? 'block' : 'none';
}

// Modal Cập Nhật Thông Tin
function showUpdateConfirm() {
    document.getElementById('updateConfirm').classList.add('is-open');
}

function hideUpdateConfirm() {
    document.getElementById('updateConfirm').classList.remove('is-open');
}

function submitUpdateForm() {
    document.getElementById('updateProfileForm').submit();
}

// Modal Đổi Mật Khẩu
function showPasswordConfirm() {
    document.getElementById('passwordConfirm').classList.add('is-open');
}

function hidePasswordConfirm() {
    document.getElementById('passwordConfirm').classList.remove('is-open');
}

function submitPasswordForm() {
    document.getElementById('changePasswordForm').submit();
}
