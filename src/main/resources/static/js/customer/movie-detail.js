document.addEventListener('DOMContentLoaded', () => {
    const dateTabs = document.getElementById('dateTabs');
    const showtimeContent = document.getElementById('showtimeContent');
    const btnPrev = document.getElementById('datePrev');
    const btnNext = document.getElementById('dateNext');
    const movieId = document.getElementById('movieId')?.value;
    if (!dateTabs || !movieId) return;

    const DAY_NAMES = ['CN', 'T.2', 'T.3', 'T.4', 'T.5', 'T.6', 'T.7'];
    const VISIBLE_COUNT = 5;

    let allDates = [];
    let currentPage = 0;
    let selectedDate = null;

    function formatDate(date) {
        const y = date.getFullYear();
        const m = String(date.getMonth() + 1).padStart(2, '0');
        const d = String(date.getDate()).padStart(2, '0');
        return `${y}-${m}-${d}`;
    }

    function buildAllDates() {
        const today = new Date();
        const lastDay = new Date(today.getFullYear(), today.getMonth() + 1, 0);
        allDates = [];

        for (let d = new Date(today); d <= lastDay; d.setDate(d.getDate() + 1)) {
            allDates.push({
                dateStr: formatDate(d),
                dayName: allDates.length === 0 ? 'Hôm nay' : DAY_NAMES[d.getDay()],
                dayNum: String(d.getDate()).padStart(2, '0') + '/' + String(d.getMonth() + 1).padStart(2, '0')
            });
        }

        selectedDate = allDates[0]?.dateStr;
    }

    function getTotalPages() {
        return Math.ceil(allDates.length / VISIBLE_COUNT);
    }

    function renderDateTabs() {
        const start = currentPage * VISIBLE_COUNT;
        const visible = allDates.slice(start, start + VISIBLE_COUNT);

        dateTabs.innerHTML = '';
        visible.forEach(item => {
            const tab = document.createElement('button');
            tab.className = 'date-tab' + (item.dateStr === selectedDate ? ' active' : '');
            tab.dataset.date = item.dateStr;
            tab.innerHTML = `<span class="day-name">${item.dayName}</span><span class="day-num">${item.dayNum}</span>`;
            tab.addEventListener('click', () => {
                selectedDate = item.dateStr;
                renderDateTabs();
                loadShowtimes(item.dateStr);
            });
            dateTabs.appendChild(tab);
        });

        updateNavButtons();
    }

    function updateNavButtons() {
        btnPrev.disabled = currentPage === 0;
        btnNext.disabled = currentPage >= getTotalPages() - 1;
    }

    btnPrev.addEventListener('click', () => {
        if (currentPage > 0) {
            currentPage--;
            renderDateTabs();
        }
    });

    btnNext.addEventListener('click', () => {
        if (currentPage < getTotalPages() - 1) {
            currentPage++;
            renderDateTabs();
        }
    });

    function loadShowtimes(date) {
        showtimeContent.innerHTML = '<div class="showtime-loading"><i class="fa-solid fa-spinner fa-spin"></i> Đang tải...</div>';

        fetch(`/api/showtimes?movieId=${movieId}&date=${date}`)
            .then(res => res.json())
            .then(data => renderShowtimes(data))
            .catch(() => {
                showtimeContent.innerHTML = '<div class="no-showtimes"><i class="fa-regular fa-calendar-xmark"></i><p>Không thể tải lịch chiếu</p></div>';
            });
    }

    function renderShowtimes(data) {
        const list = data.showtimes;
        if (!list || list.length === 0) {
            showtimeContent.innerHTML = '<div class="no-showtimes"><i class="fa-regular fa-calendar-xmark"></i><p>Không có suất chiếu nào trong ngày này</p></div>';
            return;
        }

        const isAuth = document.querySelector('.user-menu-wrapper') !== null;
        let html = '<div class="showtime-list">';

        list.forEach(s => {
            if (isAuth) {
                html += `<a href="/booking/seats?showtimeId=${s.id}" class="showtime-chip"><span class="chip-time">${s.startTime}</span></a>`;
            } else {
                html += `<button class="showtime-chip" onclick="openModal('loginView')"><span class="chip-time">${s.startTime}</span></button>`;
            }
        });

        html += '</div>';
        showtimeContent.innerHTML = html;
    }

    buildAllDates();
    renderDateTabs();
});
