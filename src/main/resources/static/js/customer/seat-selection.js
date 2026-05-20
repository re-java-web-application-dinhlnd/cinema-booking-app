document.addEventListener('DOMContentLoaded', () => {
    const seatGrid = document.getElementById('seatGrid');
    const summaryEl = document.getElementById('bookingSummary');
    const selectedSeatsText = document.getElementById('selectedSeatsText');
    const selectedCount = document.getElementById('selectedCount');
    const totalPriceEl = document.getElementById('totalPrice');
    const seatIdsInput = document.getElementById('seatIdsInput');
    const btnContinue = document.getElementById('btnContinue');

    if (!seatGrid || !SEAT_DATA || SEAT_DATA.length === 0) return;

    const selectedSeats = new Map();

    function groupByRow() {
        const rows = new Map();
        SEAT_DATA.forEach(seat => {
            const row = seat.seatName.replace(/[0-9]+$/, '');
            if (!rows.has(row)) rows.set(row, []);
            rows.get(row).push(seat);
        });
        rows.forEach(seats => seats.sort((a, b) => {
            const numA = parseInt(a.seatName.replace(/^[A-Z]+/, ''));
            const numB = parseInt(b.seatName.replace(/^[A-Z]+/, ''));
            return numA - numB;
        }));
        return rows;
    }

    function renderSeatMap() {
        const rows = groupByRow();
        seatGrid.innerHTML = '';

        rows.forEach((seats, rowLabel) => {
            const rowDiv = document.createElement('div');
            rowDiv.className = 'seat-row';

            const label = document.createElement('span');
            label.className = 'row-label';
            label.textContent = rowLabel;
            rowDiv.appendChild(label);

            const seatsDiv = document.createElement('div');
            seatsDiv.className = 'seat-cells';

            seats.forEach(seat => {
                const btn = document.createElement('button');
                btn.className = 'seat-btn';
                btn.dataset.id = seat.seatId;
                btn.dataset.name = seat.seatName;
                btn.dataset.type = seat.seatType;
                btn.textContent = seat.seatName.replace(/^[A-Z]+/, '');

                if (seat.booked) {
                    btn.classList.add('booked');
                    btn.disabled = true;
                } else {
                    btn.classList.add(seat.seatType.toLowerCase());
                    btn.addEventListener('click', () => toggleSeat(btn, seat));
                }

                seatsDiv.appendChild(btn);
            });

            const labelEnd = document.createElement('span');
            labelEnd.className = 'row-label';
            labelEnd.textContent = rowLabel;

            rowDiv.appendChild(seatsDiv);
            rowDiv.appendChild(labelEnd);
            seatGrid.appendChild(rowDiv);
        });
    }

    function toggleSeat(btn, seat) {
        if (selectedSeats.has(seat.seatId)) {
            selectedSeats.delete(seat.seatId);
            btn.classList.remove('selected');
        } else {
            if (selectedSeats.size >= MAX_SEATS) {
                if (typeof showToast === 'function') {
                    showToast('Tối đa ' + MAX_SEATS + ' ghế mỗi lần đặt!', 'warning');
                }
                return;
            }
            selectedSeats.set(seat.seatId, seat);
            btn.classList.add('selected');
        }
        updateSummary();
    }

    function calculatePrice(seatType) {
        if (seatType === 'VIP') return TICKET_PRICE * VIP_MULTIPLIER;
        if (seatType === 'SWEETBOX') return TICKET_PRICE * SWEETBOX_MULTIPLIER;
        return TICKET_PRICE;
    }

    function formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN').format(amount) + 'đ';
    }

    function updateSummary() {
        const count = selectedSeats.size;

        if (count === 0) {
            summaryEl.style.display = 'none';
            btnContinue.disabled = true;
            return;
        }

        summaryEl.style.display = 'block';
        btnContinue.disabled = false;

        const names = [];
        let total = 0;
        const ids = [];

        selectedSeats.forEach((seat, id) => {
            names.push(seat.seatName);
            total += calculatePrice(seat.seatType);
            ids.push(id);
        });

        names.sort();
        selectedSeatsText.textContent = names.join(', ');
        selectedCount.textContent = count;
        totalPriceEl.textContent = formatCurrency(total);
        seatIdsInput.value = ids.join(',');
    }

    renderSeatMap();
});
