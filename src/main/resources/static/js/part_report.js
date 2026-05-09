document.addEventListener('DOMContentLoaded', () => {
    const modalEl = document.getElementById('reportModal');
    const modal = new bootstrap.Modal(modalEl);

    modalEl.addEventListener('show.bs.modal', () => {
        modalEl.querySelectorAll('input[type="checkbox"]').forEach(cb => cb.checked = false);
        const catBtn = document.getElementById('catDropdownBtn');
        if (catBtn) catBtn.textContent = 'Выбрать категории...';
        const alertBox = document.getElementById('alertBox');
        if (alertBox) alertBox.classList.add('d-none');
        const genBtn = document.getElementById('generateBtn');
        const spinner = document.getElementById('spinner');
        if (genBtn) genBtn.disabled = false;
        if (spinner) spinner.classList.add('d-none');
    });

    const generateBtn = document.getElementById('generateBtn');
    const spinner = document.getElementById('spinner');
    const alertBox = document.getElementById('alertBox');
    const catDropdownBtn = document.getElementById('catDropdownBtn');
    const selectAllCats = document.getElementById('selectAllCats');
    const catCheckboxes = document.querySelectorAll('.cat-checkbox');
    const stateCheckboxes = document.querySelectorAll('.state-checkbox');
    const onlyWithPrice = document.getElementById('onlyWithPrice');
    const onlyInStock = document.getElementById('onlyInStock');

    // Обновление текста кнопки дропдауна
    function updateCatBtnText() {
        const count = Array.from(catCheckboxes).filter(c => c.checked).length;
        catDropdownBtn.textContent = count === 0 ? 'Выбрать категории...' : `Выбрано категорий: ${count}`;
    }

    selectAllCats.addEventListener('change', (e) => {
        catCheckboxes.forEach(cb => cb.checked = e.target.checked);
        updateCatBtnText();
    });
    catCheckboxes.forEach(cb => cb.addEventListener('change', () => {
        selectAllCats.checked = Array.from(catCheckboxes).every(c => c.checked);
        updateCatBtnText();
    }));

    // Генерация отчёта
    generateBtn.addEventListener('click', async () => {
        alertBox.classList.add('d-none');
        const selectedCats = Array.from(catCheckboxes).filter(c => c.checked).map(c => c.value);

        if (selectedCats.length === 0) {
            alertBox.textContent = 'Пожалуйста, выберите хотя бы одну категорию.';
            alertBox.classList.remove('d-none');
            return;
        }

        generateBtn.disabled = true;
        spinner.classList.remove('d-none');

        const params = new URLSearchParams();
        selectedCats.forEach(id => params.append('categoryIds', id));
        stateCheckboxes.forEach(cb => { if (cb.checked) params.append('states', cb.value); });
        if (onlyWithPrice.checked) params.append('onlyWithPrice', 'true');
        if (onlyInStock.checked) params.append('onlyInStock', 'true');

        try {
            const response = await fetch(`/reports/parts/category?${params.toString()}`);
            if (!response.ok) throw new Error('Ошибка сервера');

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `parts_report_${new Date().toISOString().slice(0,10)}.xlsx`;
            document.body.appendChild(a);
            a.click();
            a.remove();
            window.URL.revokeObjectURL(url);

            modal.hide();
        } catch (err) {
            alertBox.textContent = 'Не удалось сформировать отчёт. Попробуйте позже.';
            alertBox.classList.remove('d-none');
        } finally {
            generateBtn.disabled = false;
            spinner.classList.add('d-none');
        }
    });
});