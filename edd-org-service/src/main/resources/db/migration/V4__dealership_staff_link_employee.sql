-- EDD-ORG-DSN-CR-003 §8.2：门店员工以 ORG 员工主数据（tb_employee.id）为人员标识，
-- 解除对 MPT db_mpt.sys_user 的跨库依赖；一个员工可隶属多个门店，同门店内不可重复。
-- 变更前 user_id 指向 MPT sys_user，迁移后无法回溯映射，因此先清理无法映射到员工的孤儿关联，
-- 再收敛为 employee_id 非空；本 CR 无历史业务数据，不提供回填。
ALTER TABLE `tb_dealership_staff`
    DROP INDEX `user_id`,
    DROP COLUMN `user_id`;

DELETE FROM `tb_dealership_staff`
WHERE `employee_id` IS NULL;

ALTER TABLE `tb_dealership_staff`
    MODIFY COLUMN `employee_id` BIGINT NOT NULL COMMENT '员工ID（tb_employee.id）';

ALTER TABLE `tb_dealership_staff`
    ADD UNIQUE KEY `uk_dealership_employee` (`dealership_code`, `employee_id`);
