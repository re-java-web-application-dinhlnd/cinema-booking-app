function formatCurrency(amount) {
    return new Intl.NumberFormat('vi-VN').format(amount) + 'đ';
}

function changeQty(btn, delta) {
    const card = btn.closest('.product-card');
    const valueEl = card.querySelector('.counter-value');
    let qty = parseInt(valueEl.textContent) + delta;
    if (qty < 0) qty = 0;
    if (qty > 10) {
        if (typeof showToast === 'function') {
            showToast('Tối đa 10 sản phẩm mỗi loại!', 'warning');
        }
        return;
    }
    valueEl.textContent = qty;
    updateOrderSummary();
}

function updateOrderSummary() {
    const cards = document.querySelectorAll('.product-card');
    const productInputs = document.getElementById('productInputs');
    const comboRow = document.getElementById('comboRow');
    const comboTotalText = document.getElementById('comboTotalText');
    const grandTotalText = document.getElementById('grandTotalText');

    let comboTotal = 0;
    let html = '';

    cards.forEach(card => {
        const qty = parseInt(card.querySelector('.counter-value').textContent);
        if (qty > 0) {
            const id = card.dataset.id;
            const price = parseFloat(card.dataset.price);
            comboTotal += price * qty;
            html += `<input type="hidden" name="productIds" value="${id}" />`;
            html += `<input type="hidden" name="quantities" value="${qty}" />`;
        }
    });

    productInputs.innerHTML = html;

    if (comboTotal > 0) {
        comboRow.style.display = 'flex';
        comboTotalText.textContent = formatCurrency(comboTotal);
    } else {
        comboRow.style.display = 'none';
    }

    const grandTotal = parseFloat(TICKET_TOTAL) + comboTotal;
    grandTotalText.textContent = formatCurrency(grandTotal);
}
