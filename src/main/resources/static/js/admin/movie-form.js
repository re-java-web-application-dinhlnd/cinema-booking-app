/**
 * Movie Edit Form Javascript
 */
document.addEventListener('DOMContentLoaded', () => {
    // Expose functions globally since they are called from HTML onclick attributes
    window.showSaveConfirm = () => {
        const form = $('editMovieForm');
        if (form.checkValidity()) {
            $('saveConfirmModal').classList.add('show');
        } else {
            form.reportValidity();
        }
    };

    window.hideSaveConfirm = () => {
        $('saveConfirmModal').classList.remove('show');
    };

    window.submitEditForm = () => {
        $('editMovieForm').submit();
    };

    // Trailer logic
    function extractYoutubeId(url) {
        if (!url) return null;
        let match = url.match(/(?:youtube\.com\/(?:[^\/]+\/.+\/|(?:v|e(?:mbed)?)\/|.*[?&]v=)|youtu\.be\/)([^"&?\/\s]{11})/i);
        return (match && match[1]) ? match[1] : url; // return ID if matched, else assume it's already the ID
    }

    window.openTrailerModal = (url) => {
        const videoId = extractYoutubeId(url);
        if (!videoId) return;

        const trailerModal = $('trailerModal');
        trailerModal.classList.remove('hidden');
        $('trailerModalPlayer').innerHTML = 
            `<iframe src="https://www.youtube.com/embed/${videoId}?autoplay=1&rel=0" 
                frameborder="0" allowfullscreen 
                allow="autoplay; encrypted-media"></iframe>`;
    };

    window.closeTrailerModal = () => {
        $('trailerModal').classList.add('hidden');
        $('trailerModalPlayer').innerHTML = ''; // Stop video
    };

    // Close trailer modal on click outside
    const trailerModal = $('trailerModal');
    if (trailerModal) {
        trailerModal.addEventListener('click', (e) => {
            if (e.target === trailerModal) closeTrailerModal();
        });
    }

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            if (trailerModal && !trailerModal.classList.contains('hidden')) {
                closeTrailerModal();
            } else if (!$('saveConfirmModal').classList.contains('hidden')) {
                hideSaveConfirm();
            }
        }
    });
});
