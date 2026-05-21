document.addEventListener('DOMContentLoaded', () => {
    initBannerCarousel();
    initQuickFilter();
});

function initBannerCarousel() {
    const track = $('bannerTrack');
    const dotsContainer = $('bannerDots');
    const btnPrev = $('bannerPrev');
    const btnNext = $('bannerNext');
    const viewport = document.querySelector('.banner-viewport');
    if (!track || !dotsContainer || !viewport) return;

    const originalSlides = Array.from(track.querySelectorAll('.banner-slide'));
    const total = originalSlides.length;
    if (total === 0) return;

    /* Clone last slide → prepend, clone first slide → append
       Layout: [clone-last] [slide-0] [slide-1] [slide-2] [clone-first]
       Index:       0           1         2         3          4         */
    const cloneFirst = originalSlides[0].cloneNode(true);
    const cloneLast = originalSlides[total - 1].cloneNode(true);
    cloneFirst.classList.add('clone');
    cloneLast.classList.add('clone');
    track.appendChild(cloneFirst);
    track.insertBefore(cloneLast, track.firstChild);

    let current = 0;
    let autoTimer = null;
    let isTransitioning = false;
    const INTERVAL = 3000;

    for (let i = 0; i < total; i++) {
        const dot = document.createElement('button');
        dot.className = 'banner-dot' + (i === 0 ? ' active' : '');
        dot.addEventListener('click', () => goTo(i));
        dotsContainer.appendChild(dot);
    }

    const allSlides = track.querySelectorAll('.banner-slide');

    function getSlideWidth() {
        return allSlides[0].offsetWidth;
    }

    function setPosition(index, animate) {
        const realIndex = index + 1;
        if (animate) {
            track.style.transition = 'transform 0.5s cubic-bezier(0.25, 0.46, 0.45, 0.94)';
        } else {
            track.style.transition = 'none';
        }
        track.style.transform = 'translateX(-' + (realIndex * getSlideWidth()) + 'px)';
    }

    function updateActive() {
        allSlides.forEach(s => s.classList.remove('active'));
        const realIndex = current + 1;
        if (allSlides[realIndex]) {
            allSlides[realIndex].classList.add('active');
        }
    }

    function updateDots() {
        const dots = dotsContainer.querySelectorAll('.banner-dot');
        dots.forEach((d, i) => d.classList.toggle('active', i === current));
    }

    function goTo(index) {
        if (isTransitioning) return;
        isTransitioning = true;
        current = index;
        setPosition(current, true);
        updateDots();
        updateActive();
        resetTimer();
    }

    track.addEventListener('transitionend', () => {
        isTransitioning = false;
        if (current >= total) {
            current = 0;
            setPosition(current, false);
            updateActive();
            updateDots();
        } else if (current < 0) {
            current = total - 1;
            setPosition(current, false);
            updateActive();
            updateDots();
        }
    });

    function resetTimer() {
        clearInterval(autoTimer);
        autoTimer = setInterval(() => goTo(current + 1), INTERVAL);
    }

    if (btnPrev) btnPrev.addEventListener('click', () => goTo(current - 1));
    if (btnNext) btnNext.addEventListener('click', () => goTo(current + 1));

    window.addEventListener('resize', () => {
        setPosition(current, false);
    });

    setPosition(0, false);
    updateActive();
    resetTimer();
}

function initQuickFilter() {
    const movieSelect = $('filterMovie');
    const dateSelect = $('filterDate');
    const showtimeSelect = $('filterShowtime');
    const submitBtn = $('filterSubmit');
    if (!movieSelect) return;

    const DAY_NAMES = ['CN', 'T.2', 'T.3', 'T.4', 'T.5', 'T.6', 'T.7'];

    movieSelect.addEventListener('change', () => {
        const movieId = movieSelect.value;
        dateSelect.innerHTML = '<option value="">Chọn Ngày</option>';
        showtimeSelect.innerHTML = '<option value="">Chọn Suất</option>';
        showtimeSelect.disabled = true;
        submitBtn.disabled = true;

        if (!movieId) {
            dateSelect.disabled = true;
            return;
        }

        const today = new Date();
        for (let i = 0; i < 7; i++) {
            const d = new Date(today);
            d.setDate(d.getDate() + i);
            const y = d.getFullYear();
            const m = String(d.getMonth() + 1).padStart(2, '0');
            const dd = String(d.getDate()).padStart(2, '0');
            const val = y + '-' + m + '-' + dd;
            const label = i === 0 ? 'Hôm nay' : DAY_NAMES[d.getDay()] + ' ' + dd + '/' + m;
            const opt = document.createElement('option');
            opt.value = val;
            opt.textContent = label;
            dateSelect.appendChild(opt);
        }
        dateSelect.disabled = false;
    });

    dateSelect.addEventListener('change', () => {
        const movieId = movieSelect.value;
        const date = dateSelect.value;
        showtimeSelect.innerHTML = '<option value="">Đang tải...</option>';
        submitBtn.disabled = true;

        if (!date) {
            showtimeSelect.innerHTML = '<option value="">Chọn Suất</option>';
            showtimeSelect.disabled = true;
            return;
        }

        fetch('/api/showtimes?movieId=' + movieId + '&date=' + date)
            .then(res => res.json())
            .then(data => {
                showtimeSelect.innerHTML = '<option value="">Chọn Suất</option>';
                const list = data.showtimes || [];
                if (list.length === 0) {
                    showtimeSelect.innerHTML = '<option value="">Không có suất chiếu</option>';
                    showtimeSelect.disabled = true;
                    return;
                }
                list.forEach(s => {
                    if (!s.soldOut) {
                        const opt = document.createElement('option');
                        opt.value = s.id;
                        opt.textContent = s.startTime + ' (' + s.availableSeats + ' ghế trống)';
                        showtimeSelect.appendChild(opt);
                    }
                });
                showtimeSelect.disabled = false;
            })
            .catch(() => {
                showtimeSelect.innerHTML = '<option value="">Lỗi tải suất chiếu</option>';
                showtimeSelect.disabled = true;
            });
    });

    showtimeSelect.addEventListener('change', () => {
        submitBtn.disabled = !showtimeSelect.value;
    });

    submitBtn.addEventListener('click', () => {
        const showtimeId = showtimeSelect.value;
        if (showtimeId) {
            const isAuth = document.querySelector('.user-menu-wrapper') !== null;
            const targetUrl = '/booking/seats?showtimeId=' + showtimeId;
            if (isAuth) {
                window.location.href = targetUrl;
            } else {
                sessionStorage.setItem('redirectAfterLogin', targetUrl);
                openModal('loginView');
            }
        }
    });
}
