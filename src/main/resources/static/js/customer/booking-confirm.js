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

    if (qty > 0) {
        card.classList.add('has-qty');
    } else {
        card.classList.remove('has-qty');
    }

    updateOrderSummary();
}

function updateOrderSummary() {
    const $ = (id) => document.getElementById(id);
    const cards = document.querySelectorAll('.product-card');
    const productInputs = $('productInputs');
    const comboSection = $('comboSummarySection');
    const comboItemsList = $('comboItemsList');
    const comboTotalText = $('comboTotalText');
    const grandTotalText = $('grandTotalText');

    let comboTotal = 0;
    let inputsHtml = '';
    let itemsHtml = '';

    cards.forEach(card => {
        const qty = parseInt(card.querySelector('.counter-value').textContent);
        if (qty > 0) {
            const id = card.dataset.id;
            const price = parseFloat(card.dataset.price);
            const name = card.querySelector('.product-name').textContent;
            const lineTotal = price * qty;
            comboTotal += lineTotal;

            inputsHtml += '<input type="hidden" name="productIds" value="' + id + '" />';
            inputsHtml += '<input type="hidden" name="quantities" value="' + qty + '" />';

            itemsHtml += '<div class="summary-row combo-item-row">';
            itemsHtml += '<span>' + name + ' <small>x' + qty + '</small></span>';
            itemsHtml += '<span class="fw-600">' + formatCurrency(lineTotal) + '</span>';
            itemsHtml += '</div>';
        }
    });

    productInputs.innerHTML = inputsHtml;
    comboItemsList.innerHTML = itemsHtml;

    if (comboTotal > 0) {
        comboSection.style.display = 'block';
        comboTotalText.textContent = formatCurrency(comboTotal);
    } else {
        comboSection.style.display = 'none';
    }

    const grandTotal = parseFloat(TICKET_TOTAL) + comboTotal;
    grandTotalText.textContent = formatCurrency(grandTotal);
}
