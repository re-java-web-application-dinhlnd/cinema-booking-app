const $ = (id) => document.getElementById(id);
const $$ = (selector) => document.querySelectorAll(selector);

function switchTab(tabId) {
    $$('.nav-btn').forEach(btn => btn.classList.remove('active'));
    event.currentTarget.classList.add('active');

    $('tab-info').style.display = tabId === 'info' ? 'block' : 'none';
    $('tab-password').style.display = tabId === 'password' ? 'block' : 'none';
}

function showUpdateConfirm() {
    $('updateConfirm').classList.add('is-open');
}

function hideUpdateConfirm() {
    $('updateConfirm').classList.remove('is-open');
}

function submitUpdateForm() {
    $('updateProfileForm').submit();
}

function showPasswordConfirm() {
    $('passwordConfirm').classList.add('is-open');
}

function hidePasswordConfirm() {
    $('passwordConfirm').classList.remove('is-open');
}

function submitPasswordForm() {
    $('changePasswordForm').submit();
}
