function extractYoutubeId(url) {
    if (!url) return null;
    const patterns = [
        /(?:youtube\.com\/watch\?v=)([^&]+)/,
        /(?:youtu\.be\/)([^?]+)/,
        /(?:youtube\.com\/embed\/)([^?]+)/
    ];
    for (const p of patterns) {
        const match = url.match(p);
        if (match) return match[1];
    }
    return null;
}

function openTrailerModal(trailerUrl) {
    const videoId = extractYoutubeId(trailerUrl);
    if (!videoId) return;

    const iframe = document.getElementById('trailerIframe');
    iframe.src = 'https://www.youtube.com/embed/' + videoId + '?autoplay=1&rel=0';

    document.getElementById('trailerModal').classList.add('active');
    document.body.style.overflow = 'hidden';
}

function closeTrailerModal(event) {
    if (event && event.target !== event.currentTarget) return;

    const iframe = document.getElementById('trailerIframe');
    iframe.src = '';

    document.getElementById('trailerModal').classList.remove('active');
    document.body.style.overflow = '';
}

document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') closeTrailerModal();
});
