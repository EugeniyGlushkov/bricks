# bricks
Lego bricks project.

pg_dump -U postgres -Fc -v  --exclude-table=databasechangeloglock --exclude-table=databasechangelog -f D:\lego\db\dumps\dump_briks_20260426_22_58.dump briks
pg_restore -U postgres -d briks_dev -v D:\lego\db\dumps\dump_briks_20260426_22_58.dump