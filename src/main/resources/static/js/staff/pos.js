

function showCheckInConfirm() {
    const modal = $('checkInConfirm');
    if (modal) modal.classList.add('is-open');
}

function hideCheckInConfirm() {
    const modal = $('checkInConfirm');
    if (modal) modal.classList.remove('is-open');
}

function submitCheckIn() {
    const form = $('checkInForm');
    if (form) form.submit();
}
