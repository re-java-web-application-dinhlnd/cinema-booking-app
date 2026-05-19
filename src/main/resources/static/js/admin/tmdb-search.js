/**
 * TMDB Search & Browse Module
 * Handles TMDB movie search, category browsing, infinite scroll,
 * detail modal, trailer playback, and movie importing.
 */
document.addEventListener('DOMContentLoaded', () => {

    // === DOM ELEMENTS ===
    const searchInput   = $('tmdbSearchInput');
    const resultsDiv    = $('tmdbResults');
    const loadingDiv    = $('tmdbLoading');
    const loadMoreDiv   = $('tmdbLoadMore');
    const endMsgDiv     = $('tmdbEndMsg');
    const emptyDiv      = $('tmdbEmpty');
    const tabsDiv       = $('tmdbTabs');
    const sentinel      = $('tmdbSentinel');
    const detailModal   = $('tmdbDetailModal');
    const trailerModal  = $('trailerModal');

    const csrfToken  = document.querySelector('meta[name="_csrf"]').content;
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

    // === PAGINATION STATE ===
    let currentPage      = 1;
    let totalPages       = 1;
    let isLoading        = false;
    let isSearchMode     = false;
    let currentCategory  = 'now_playing';
    let currentQuery     = '';
    let currentTrailerKey = null;
    let debounceTimer;

    const today = new Date().toISOString().split('T')[0];
    let importedIds = new Set();

    // === INTERSECTION OBSERVER (infinite scroll) ===
    const observer = new IntersectionObserver(entries => {
        if (entries[0].isIntersecting && !isLoading && currentPage < totalPages) {
            loadNextPage();
        }
    }, { rootMargin: '200px' });

    observer.observe(sentinel);

    // === SEARCH ===
    searchInput.addEventListener('input', function () {
        clearTimeout(debounceTimer);
        const query = this.value.trim();

        if (query.length < 2) {
            if (query.length === 0 && isSearchMode) {
                isSearchMode = false;
                currentQuery = '';
                tabsDiv.classList.remove('hidden');
                const activeTab = tabsDiv.querySelector('.tmdb-tab.active');
                if (activeTab) switchCategory(activeTab.dataset.category, activeTab);
            }
            return;
        }

        isSearchMode = true;
        currentQuery = query;
        tabsDiv.classList.add('hidden');
        debounceTimer = setTimeout(() => {
            resetAndLoad(() => fetchSearch(query, 1));
        }, 500);
    });

    async function fetchSearch(query, page) {
        const res = await fetch(`/admin/movies/api/tmdb-search?q=${encodeURIComponent(query)}&page=${page}`);
        return await res.json();
    }

    // === BROWSE CATEGORIES (event delegation on tabs) ===
    function switchCategory(category, btn) {
        tabsDiv.querySelectorAll('.tmdb-tab').forEach(t => t.classList.remove('active'));
        btn.classList.add('active');
        currentCategory = category;
        isSearchMode = false;
        currentQuery = '';
        searchInput.value = '';
        resetAndLoad(() => fetchBrowse(category, 1));
    }

    tabsDiv.addEventListener('click', (e) => {
        const tab = e.target.closest('.tmdb-tab');
        if (tab) switchCategory(tab.dataset.category, tab);
    });

    async function fetchBrowse(category, page) {
        const res = await fetch(`/admin/movies/api/tmdb-browse?category=${category}&page=${page}`);
        return await res.json();
    }

    // === CORE LOAD LOGIC ===
    function resetAndLoad(fetchFn) {
        currentPage = 1;
        totalPages = 1;
        resultsDiv.innerHTML = '';
        emptyDiv.classList.add('hidden');
        endMsgDiv.classList.add('hidden');
        loadMoreDiv.classList.add('hidden');
        loadingDiv.classList.remove('hidden');

        fetchFn().then(data => {
            loadingDiv.classList.add('hidden');
            handleResponse(data, true);
        }).catch(() => {
            loadingDiv.classList.add('hidden');
            if (window.showToast) showToast('Lỗi kết nối TMDB API', 'error');
        });
    }

    function loadNextPage() {
        if (isLoading || currentPage >= totalPages) return;

        isLoading = true;
        const nextPage = currentPage + 1;
        loadMoreDiv.classList.remove('hidden');

        const fetchFn = isSearchMode
            ? fetchSearch(currentQuery, nextPage)
            : fetchBrowse(currentCategory, nextPage);

        fetchFn.then(data => {
            loadMoreDiv.classList.add('hidden');
            isLoading = false;
            handleResponse(data, false);
        }).catch(() => {
            loadMoreDiv.classList.add('hidden');
            isLoading = false;
        });
    }

    function handleResponse(data, isFirstPage) {
        if (!data) return;

        totalPages = data.total_pages || 1;
        currentPage = data.page || 1;

        const results = data.results || [];

        if (isFirstPage && results.length === 0) {
            emptyDiv.classList.remove('hidden');
            return;
        }

        appendResults(results);

        if (currentPage >= totalPages) {
            endMsgDiv.classList.remove('hidden');
        }
    }

    // === RENDER (append, not replace) ===
    function appendResults(results) {
        results.forEach(movie => {
            const posterUrl = movie.poster_path
                ? `https://image.tmdb.org/t/p/w500${movie.poster_path}` : null;
            const rating = movie.vote_average ? movie.vote_average.toFixed(1) : '—';

            let releaseDateText = 'Chưa rõ';
            if (movie.release_date) {
                const parts = movie.release_date.split('-');
                if (parts.length === 3) {
                    releaseDateText = `${parts[2]}/${parts[1]}/${parts[0]}`;
                }
            }

            let statusBadge = '';
            if (movie.release_date) {
                if (movie.release_date > today) {
                    statusBadge = '<span class="tmdb-status upcoming"><i class="fa-solid fa-clock"></i> Sắp chiếu</span>';
                } else {
                    statusBadge = '<span class="tmdb-status playing"><i class="fa-solid fa-play-circle"></i> Đang chiếu</span>';
                }
            }

            const alreadyImported = importedIds.has(movie.id);

            const card = document.createElement('div');
            card.className = 'tmdb-card';
            card.innerHTML = `
                <div class="tmdb-poster-wrap tmdb-poster-clickable" data-tmdb-id="${movie.id}">
                    ${posterUrl
                        ? `<img src="${posterUrl}" alt="${movie.title}" loading="lazy"/>`
                        : `<div class="tmdb-poster-empty"><i class="fa-solid fa-image"></i></div>`}
                    ${statusBadge}
                </div>
                <div class="tmdb-card-body">
                    <h3 title="${movie.title || movie.original_title}">${movie.title || movie.original_title}</h3>
                    <div class="tmdb-card-meta">
                        <span><i class="fa-regular fa-calendar"></i> ${releaseDateText}</span>
                        <span class="tmdb-rating"><i class="fa-solid fa-star"></i> ${rating}</span>
                    </div>
                    ${alreadyImported
                        ? `<button class="btn-import btn-imported" disabled>
                               <i class="fa-solid fa-check"></i> Đã có trong rạp
                           </button>`
                        : `<button class="btn-import" data-import-id="${movie.id}">
                               <i class="fa-solid fa-plus"></i> Thêm vào rạp
                           </button>`}
                </div>`;
            resultsDiv.appendChild(card);
        });
    }

    // === EVENT DELEGATION (no inline onclick) ===
    resultsDiv.addEventListener('click', (e) => {
        // Handle poster click → detail modal
        const posterWrap = e.target.closest('.tmdb-poster-clickable');
        if (posterWrap) {
            const tmdbId = parseInt(posterWrap.dataset.tmdbId);
            openDetailModal(tmdbId);
            return;
        }

        // Handle import button
        const importBtn = e.target.closest('.btn-import[data-import-id]');
        if (importBtn) {
            const tmdbId = parseInt(importBtn.dataset.importId);
            importMovie(tmdbId, importBtn);
        }
    });

    // === IMPORT ===
    async function importMovie(tmdbId, btn) {
        btn.disabled = true;
        btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Đang thêm...';
        try {
            const res = await fetch(`/admin/movies/import/${tmdbId}`, {
                method: 'POST',
                headers: {
                    [csrfHeader]: csrfToken,
                    'Content-Type': 'application/json'
                }
            });
            const data = await res.json();

            if (res.ok && data.status === 'success') {
                btn.innerHTML = '<i class="fa-solid fa-check"></i> Đã có trong rạp';
                btn.classList.add('btn-imported');
                importedIds.add(tmdbId);
            } else {
                btn.innerHTML = '<i class="fa-solid fa-xmark"></i> ' + (data.message || 'Lỗi');
                btn.classList.add('btn-import-error');
                setTimeout(() => {
                    if (data.message && data.message.includes('đã có')) {
                        btn.innerHTML = '<i class="fa-solid fa-check"></i> Đã có trong rạp';
                        btn.classList.remove('btn-import-error');
                        btn.classList.add('btn-imported');
                        importedIds.add(tmdbId);
                    } else {
                        btn.disabled = false;
                        btn.classList.remove('btn-import-error');
                        btn.innerHTML = '<i class="fa-solid fa-plus"></i> Thêm vào rạp';
                    }
                }, 2500);
            }
        } catch (err) {
            btn.disabled = false;
            btn.innerHTML = '<i class="fa-solid fa-plus"></i> Thêm vào rạp';
        }
    }

    // === DETAIL MODAL ===
    async function openDetailModal(tmdbId) {
        detailModal.classList.remove('hidden');
        $('modalTitle').textContent = 'Đang tải...';
        $('modalOverview').textContent = '';
        $('modalTagline').textContent = '';
        $('modalGenres').innerHTML = '';
        $('modalPoster').src = '';
        $('modalBackdrop').style.backgroundImage = '';

        try {
            const res = await fetch(`/admin/movies/api/tmdb-detail/${tmdbId}`);
            if (!res.ok) { closeDetailModal(); return; }
            const m = await res.json();

            // Backdrop
            if (m.backdrop_path) {
                $('modalBackdrop').style.backgroundImage =
                    `url(https://image.tmdb.org/t/p/w1280${m.backdrop_path})`;
            }

            // Poster
            if (m.poster_path) {
                $('modalPoster').src =
                    `https://image.tmdb.org/t/p/w500${m.poster_path}`;
            }

            // Title + tagline
            $('modalTitle').textContent = m.title || m.original_title;
            if (m.tagline) {
                $('modalTagline').textContent = `"${m.tagline}"`;
            }

            // Release date
            let dateText = 'Chưa rõ';
            if (m.release_date) {
                const p = m.release_date.split('-');
                if (p.length === 3) dateText = `${p[2]}/${p[1]}/${p[0]}`;
            }
            $('modalRelease').innerHTML =
                `<i class="fa-regular fa-calendar"></i> ${dateText}`;

            // Runtime
            $('modalRuntime').innerHTML =
                `<i class="fa-solid fa-clock"></i> ${m.runtime ? m.runtime + ' phút' : 'N/A'}`;

            // Rating
            $('modalRating').innerHTML =
                `<i class="fa-solid fa-star"></i> ${m.vote_average ? m.vote_average.toFixed(1) : '—'} (${m.vote_count || 0} votes)`;

            // Genres
            const genresDiv = $('modalGenres');
            genresDiv.innerHTML = '';
            if (m.genres && m.genres.length > 0) {
                m.genres.forEach(g => {
                    genresDiv.innerHTML += `<span class="tmdb-genre-tag">${g.name}</span>`;
                });
            }

            // Overview
            $('modalOverview').textContent =
                m.overview || 'Chưa có mô tả cho phim này.';

            // Import button
            const btn = $('modalImportBtn');
            if (importedIds.has(tmdbId)) {
                btn.disabled = true;
                btn.className = 'btn-import btn-imported';
                btn.innerHTML = '<i class="fa-solid fa-check"></i> Đã có trong rạp';
                btn.onclick = null;
            } else {
                btn.disabled = false;
                btn.className = 'btn-import';
                btn.innerHTML = '<i class="fa-solid fa-plus"></i> Thêm vào rạp';
                btn.onclick = () => importMovie(tmdbId, btn);
            }

            // Trailer — show/hide play overlay on modal poster
            const posterPlayBtn = $('modalPosterPlayBtn');
            posterPlayBtn.classList.add('hidden');
            currentTrailerKey = null;

            if (m.videos && m.videos.results && m.videos.results.length > 0) {
                const youtubeVideos = m.videos.results.filter(v => v.site === 'YouTube');
                const trailer = youtubeVideos.find(v => v.type === 'Trailer' && v.official) ||
                                youtubeVideos.find(v => v.type === 'Trailer') ||
                                youtubeVideos.find(v => v.type === 'Teaser') ||
                                youtubeVideos[0];
                if (trailer) {
                    currentTrailerKey = trailer.key;
                    posterPlayBtn.classList.remove('hidden');
                }
            }

        } catch (err) {
            closeDetailModal();
        }
    }

    // Play trailer from modal poster → open trailer modal
    $('modalPosterPlayBtn').addEventListener('click', () => {
        if (!currentTrailerKey) return;
        trailerModal.classList.remove('hidden');
        $('trailerModalPlayer').innerHTML =
            `<iframe src="https://www.youtube.com/embed/${currentTrailerKey}?autoplay=1&rel=0"
                frameborder="0" allowfullscreen
                allow="autoplay; encrypted-media"></iframe>`;
    });

    // Close detail modal
    function closeDetailModal() {
        detailModal.classList.add('hidden');
    }

    $('detailModalCloseBtn').addEventListener('click', closeDetailModal);

    // === TRAILER MODAL ===
    function closeTrailerModal() {
        trailerModal.classList.add('hidden');
        $('trailerModalPlayer').innerHTML = '';
    }

    $('trailerModalCloseBtn').addEventListener('click', closeTrailerModal);

    // === KEYBOARD SHORTCUTS ===
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            if (!trailerModal.classList.contains('hidden')) {
                closeTrailerModal();
            } else if (!detailModal.classList.contains('hidden')) {
                closeDetailModal();
            }
        }
    });

    // === MODAL OVERLAY CLICK HANDLERS ===
    detailModal.addEventListener('click', (e) => {
        if (e.target === detailModal) closeDetailModal();
    });

    trailerModal.addEventListener('click', (e) => {
        if (e.target === trailerModal) closeTrailerModal();
    });

    // === INIT ===
    async function init() {
        try {
            const res = await fetch('/admin/movies/api/imported-ids');
            const ids = await res.json();
            importedIds = new Set(ids);
        } catch (e) { /* Nếu lỗi thì coi như chưa import phim nào */ }

        resetAndLoad(() => fetchBrowse('now_playing', 1));
    }

    init();
});
