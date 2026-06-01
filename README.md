# bricks
Lego bricks project.
cd Program Files\PostgreSQL\17\bin
pg_dump -U postgres --data-only -Fc -v  --exclude-table=databasechangeloglock --exclude-table=databasechangelog -f D:\lego\db\dumps\dump_briks_20260502_21_36.dump briks
pg_restore -U postgres -d briks_dev -v D:\lego\db\dumps\dump_briks_20260502_21_36.dump