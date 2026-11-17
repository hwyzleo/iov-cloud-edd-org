-- EDD-ORG-DSN-CR-004 §9.3：员工增量同步水位索引
-- 下游（SEC-EIAM）按 modify_time 水位增量拉取员工，且排序需稳定（modify_time, id）；
-- 逻辑删除无需 DDL 变化（row_valid 已存在），删除语义由应用层调整。
CREATE INDEX `idx_modify_time` ON `tb_employee` (`modify_time`, `id`);
