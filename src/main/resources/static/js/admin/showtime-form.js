/**
 * Showtime Form — Date/Time split, auto-calculate endTime,
 * AJAX room schedule, confirm modal
 */
document.addEventListener('DOMContentLoaded', () => {
    const $$ = (selector) => document.querySelectorAll(selector);

    $$('.toast-auto').forEach(el => {
        if (window.showToast) showToast(el.textContent, el.dataset.type);
        el.remove();
    });

    const BUFFER_MINUTES = 15;

    const movieSelect = $('movieId');
    const roomSelect = $('roomId');
    const showDateInput = $('showDate');
    const showTimeInput = $('showTime');
    const startTimeHidden = $('startTime');
    const calcInfo = $('calcInfo');
    const calcDuration = $('calcDuration');
    const calcEndTime = $('calcEndTime');
    const roomSchedule = $('roomSchedule');
    const roomScheduleContent = $('roomScheduleContent');

    // Edit mode: get duration from hidden field
    const editDurationEl = $('editDuration');
    const editDuration = editDurationEl ? parseInt(editDurationEl.value) : null;

    // Set min date to today (prevent past dates)
    if (showDateInput) {
        const today = new Date();
        const yyyy = today.getFullYear();
        const mm = String(today.getMonth() + 1).padStart(2, '0');
        const dd = String(today.getDate()).padStart(2, '0');
        showDateInput.setAttribute('min', `${yyyy}-${mm}-${dd}`);
    }

    /**
     * Get selected movie duration
     */
    function getSelectedDuration() {
        if (editDuration) return editDuration;
        if (!movieSelect) return null;
        const selected = movieSelect.options[movieSelect.selectedIndex];
        if (!selected || !selected.value) return null;
        return parseInt(selected.getAttribute('data-duration'));
    }

    /**
     * Merge date + time vào hidden field startTime (yyyy-MM-dd'T'HH:mm)
     */
    function mergeDateTime() {
        if (!showDateInput || !showTimeInput || !startTimeHidden) return;
        const dateVal = showDateInput.value;
        const timeVal = showTimeInput.value;
        if (dateVal && timeVal) {
            startTimeHidden.value = `${dateVal}T${timeVal}`;
        } else {
            startTimeHidden.value = '';
        }
    }

    /**
     * Recalculate and display endTime
     */
    function recalculate() {
        mergeDateTime();

        const duration = getSelectedDuration();

        // Show calc info as soon as movie is selected
        if (duration) {
            if (calcInfo) calcInfo.classList.remove('hidden');
            if (calcDuration) calcDuration.textContent = duration + ' phút';
        } else {
            if (calcInfo) calcInfo.classList.add('hidden');
            return;
        }

        const dateVal = showDateInput ? showDateInput.value : null;
        const timeVal = showTimeInput ? showTimeInput.value : null;

        if (!dateVal || !timeVal) {
            if (calcEndTime) calcEndTime.textContent = '—';
            return;
        }

        // Calculate endTime
        const startDate = new Date(`${dateVal}T${timeVal}`);
        const endDate = new Date(startDate.getTime() + (duration + BUFFER_MINUTES) * 60000);

        const hh = String(endDate.getHours()).padStart(2, '0');
        const mmEnd = String(endDate.getMinutes()).padStart(2, '0');
        const ddEnd = String(endDate.getDate()).padStart(2, '0');
        const MMEnd = String(endDate.getMonth() + 1).padStart(2, '0');
        const yyyyEnd = endDate.getFullYear();

        if (calcEndTime) calcEndTime.textContent = `${hh}:${mmEnd} ${ddEnd}/${MMEnd}/${yyyyEnd}`;
    }

    /**
     * AJAX: Load room schedule for selected room + date
     */
    function loadRoomSchedule() {
        const roomId = roomSelect ? roomSelect.value : null;
        const dateVal = showDateInput ? showDateInput.value : null;

        if (!roomId || !dateVal || !roomSchedule) {
            if (roomSchedule) roomSchedule.classList.add('hidden');
            return;
        }

        const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;  // meta tags require querySelector

        const headers = {};
        if (csrfHeader && csrfToken) headers[csrfHeader] = csrfToken;

        fetch(`/admin/showtimes/api/room-schedule?roomId=${roomId}&date=${dateVal}`, { headers })
            .then(res => res.json())
            .then(data => {
                roomSchedule.classList.remove('hidden');
                if (data.length === 0) {
                    roomScheduleContent.innerHTML =
                        '<p class="room-schedule-empty"><i class="fa-regular fa-circle-check"></i> Chưa có suất chiếu nào — phòng trống cả ngày!</p>';
                } else {
                    roomScheduleContent.innerHTML = data.map(st =>
                        `<div class="room-schedule-item">
                            <span class="schedule-time">${st.startTime} → ${st.endTime}</span>
                            <span class="schedule-movie">${st.movieTitle}</span>
                        </div>`
                    ).join('');
                }
            })
            .catch(() => {
                if (roomSchedule) roomSchedule.classList.add('hidden');
            });
    }

    // Attach listeners
    if (movieSelect) movieSelect.addEventListener('change', recalculate);
    if (showDateInput) {
        showDateInput.addEventListener('input', () => { recalculate(); loadRoomSchedule(); });
    }
    if (showTimeInput) {
        showTimeInput.addEventListener('input', recalculate);
    }
    if (roomSelect) {
        roomSelect.addEventListener('change', loadRoomSchedule);
    }

    // Initial calc — for edit mode OR create re-render (after validation error)
    if (getSelectedDuration() && showDateInput && showDateInput.value) {
        recalculate();
        loadRoomSchedule();
    }

    // ===== Inline Field Errors =====
    function setFieldError(id, message) {
        const el = $(id);
        if (!el) return;
        el.textContent = message;
        const input = el.previousElementSibling;
        if (input) input.classList.add('input-error');
    }

    function clearFieldError(errorId) {
        const el = $(errorId);
        if (!el) return;
        el.textContent = '';
        const input = el.previousElementSibling;
        if (input) input.classList.remove('input-error');
    }

    function clearAllErrors() {
        $$('.field-error').forEach(el => el.textContent = '');
        $$('.input-error').forEach(el => el.classList.remove('input-error'));
    }

    // Auto-clear error khi user thao tác vào input
    const fieldMap = [
        ['movieId', 'err-movieId'],
        ['roomId', 'err-roomId'],
        ['showDate', 'err-showDate'],
        ['showTime', 'err-showTime'],
        ['ticketPrice', 'err-ticketPrice'],
    ];
    fieldMap.forEach(([inputId, errorId]) => {
        const el = $(inputId);
        if (!el) return;
        const event = el.tagName === 'SELECT' ? 'change' : 'focus';
        el.addEventListener(event, () => clearFieldError(errorId));
    });

    // ===== Confirm Modal =====
    window.showConfirm = () => {
        mergeDateTime();
        clearAllErrors();

        let hasError = false;

        if (movieSelect && !movieSelect.value) {
            setFieldError('err-movieId', 'Vui lòng chọn phim');
            hasError = true;
        }
        if (roomSelect && !roomSelect.value) {
            setFieldError('err-roomId', 'Vui lòng chọn phòng chiếu');
            hasError = true;
        }
        if (!showDateInput || !showDateInput.value) {
            setFieldError('err-showDate', 'Vui lòng chọn ngày chiếu');
            hasError = true;
        }
        if (!showTimeInput || !showTimeInput.value) {
            setFieldError('err-showTime', 'Vui lòng chọn giờ bắt đầu');
            hasError = true;
        }

        const ticketInput = $('ticketPrice');
        const ticketVal = ticketInput ? parseFloat(ticketInput.value) : NaN;
        if (!ticketInput || !ticketInput.value) {
            setFieldError('err-ticketPrice', 'Vui lòng nhập giá vé');
            hasError = true;
        } else if (isNaN(ticketVal) || ticketVal < 1000) {
            setFieldError('err-ticketPrice', 'Giá vé tối thiểu là 1.000đ');
            hasError = true;
        }

        if (hasError) return;

        $('confirmModal').classList.add('show');
    };

    window.hideConfirm = () => {
        $('confirmModal').classList.remove('show');
    };

    window.submitForm = () => {
        mergeDateTime();
        $('showtimeForm').submit();
    };

    // Close on Escape
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') hideConfirm();
    });
});
