--вынесение номеров элементов в отдельную таблицу, т.к. может быть несколько элементов с одинаковыми сочетаниями
--деталь-цвет, но разными номерами
--шаг1
TRUNCATE element_info;
select count(*) from element_info ei;

-- =============================================================================
-- ШАГ 3: создание временной таблицы temp_element_part_color
-- =============================================================================
-- Удаляем таблицу, если она уже есть (для чистоты эксперимента)
DROP TABLE IF EXISTS temp_element_part_color;

-- Создаём постоянную таблицу и сразу наполняем данными
CREATE TABLE temp_element_part_color AS
SELECT element_id, part_id, color_id
FROM elements;

-- Индекс для ускорения будущих JOIN (когда будем связывать с новой elements)
CREATE INDEX idx_temp_part_color ON temp_element_part_color(part_id, color_id);

-- Проверка: количество строк должно совпадать с elements
SELECT 'В elements: ', COUNT(*) FROM elements;
SELECT 'В temp_element_part_color: ', COUNT(*) FROM temp_element_part_color;

-- =============================================================================
-- ШАГ 5: Удаление дубликатов из elements по (part_id, color_id)
-- Оставляем запись с минимальным id (самую старую) как каноническую
-- =============================================================================
BEGIN;

-- Предварительная статистика
SELECT '📊 До очистки: всего записей' AS info, COUNT(*) FROM elements; --108805
SELECT '📊 До очистки: уникальных пар (part_id, color_id)' AS info, 
       COUNT(DISTINCT (part_id, color_id)) FROM elements; --84273

-- Удаляем дубликаты, оставляя строку с минимальным id в каждой группе
-- Используем IS NOT DISTINCT FROM для корректной обработки NULL в color_id
DELETE FROM elements e_del
USING elements e_keep
WHERE e_del.part_id = e_keep.part_id
  AND e_del.color_id IS NOT DISTINCT FROM e_keep.color_id
  AND e_del.id > e_keep.id;

-- Проверка после очистки
SELECT '✅ После очистки: всего записей' AS info, COUNT(*) FROM elements; --84273
SELECT '✅ После очистки: уникальных пар (part_id, color_id)' AS info, 
       COUNT(DISTINCT (part_id, color_id)) FROM elements; --84273

-- Финальный коммит
COMMIT;

-- =============================================================================
-- ШАГИ 7–8: Заполнение element_external_ids и удаление временной таблицы
-- =============================================================================
BEGIN;

-- ШАГ 7: Переносим ВСЕ внешние ID в element_external_ids
-- Связываем через (part_id, color_id). IS NOT DISTINCT FROM корректно обрабатывает NULL в color_id
INSERT INTO element_external_ids (element_id, element_external_id)
SELECT e.id, t.element_id
FROM elements e
JOIN temp_element_part_color t
  ON e.part_id = t.part_id
 AND e.color_id IS NOT DISTINCT FROM t.color_id;

-- Проверка: количество записей должно совпадать с исходным количеством строк в elements (до очистки)
SELECT '🔗 Всего восстановлено внешних ID: ' AS info, COUNT(*) AS cnt FROM element_external_ids;

-- ШАГ 8: Удаляем временную таблицу (больше не нужна)
DROP TABLE IF EXISTS temp_element_part_color;
SELECT '🗑️ Временная таблица temp_element_part_color удалена' AS status;

COMMIT;
