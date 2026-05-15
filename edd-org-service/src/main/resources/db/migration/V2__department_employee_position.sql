-- V2: 新增部门、员工、岗位表
-- 部门表
CREATE TABLE IF NOT EXISTS `tb_department` (
    `id`          BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `parent_id`   BIGINT DEFAULT 0 COMMENT '父部门ID',
    `ancestors`   VARCHAR(500) DEFAULT '' COMMENT '祖级列表',
    `name`        VARCHAR(100) NOT NULL COMMENT '部门名称',
    `code`        VARCHAR(50) COMMENT '部门编码',
    `leader`      VARCHAR(50) COMMENT '负责人',
    `phone`       VARCHAR(20) COMMENT '联系电话',
    `email`       VARCHAR(100) COMMENT '邮箱',
    `sort`        INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `enable`      TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    `description` VARCHAR(500) COMMENT '备注',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`   BIGINT COMMENT '创建者',
    `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`   BIGINT COMMENT '修改者',
    `row_version` INT COMMENT '记录版本',
    `row_valid`   TINYINT COMMENT '是否有效',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 岗位表
CREATE TABLE IF NOT EXISTS `tb_position` (
    `id`          BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`        VARCHAR(50) NOT NULL COMMENT '岗位编码',
    `name`        VARCHAR(100) NOT NULL COMMENT '岗位名称',
    `sort`        INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `enable`      TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    `description` VARCHAR(500) COMMENT '备注',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`   BIGINT COMMENT '创建者',
    `modify_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`   BIGINT COMMENT '修改者',
    `row_version` INT COMMENT '记录版本',
    `row_valid`   TINYINT COMMENT '是否有效',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';

-- 员工表
CREATE TABLE IF NOT EXISTS `tb_employee` (
    `id`              BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`            VARCHAR(50) NOT NULL COMMENT '工号',
    `name`            VARCHAR(100) NOT NULL COMMENT '姓名',
    `gender`          TINYINT COMMENT '性别：0-未知，1-男，2-女',
    `birthday`        DATE COMMENT '出生日期',
    `phone`           VARCHAR(20) COMMENT '手机号',
    `email`           VARCHAR(100) COMMENT '邮箱',
    `id_card`         VARCHAR(50) COMMENT '身份证号',
    `hire_date`       DATE COMMENT '入职日期',
    `leave_date`      DATE COMMENT '离职日期',
    `eiam_user_id`    BIGINT COMMENT '关联EIAM用户ID',
    `eiam_account`    VARCHAR(50) COMMENT '关联EIAM账号',
    `enable`          TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用',
    `sort`            INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `description`     VARCHAR(500) COMMENT '备注',
    `create_time`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`       BIGINT COMMENT '创建者',
    `modify_time`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`       BIGINT COMMENT '修改者',
    `row_version`     INT COMMENT '记录版本',
    `row_valid`       TINYINT COMMENT '是否有效',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_eiam_user_id` (`eiam_user_id`),
    KEY `idx_eiam_account` (`eiam_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 员工-部门关联表
CREATE TABLE IF NOT EXISTS `tb_employee_department` (
    `id`              BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `employee_id`     BIGINT NOT NULL COMMENT '员工ID',
    `department_id`   BIGINT NOT NULL COMMENT '部门ID',
    `is_primary`      TINYINT NOT NULL DEFAULT 0 COMMENT '是否主部门',
    `create_time`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`       BIGINT COMMENT '创建者',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_emp_dept` (`employee_id`, `department_id`),
    KEY `idx_department_id` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工-部门关联表';

-- 员工-岗位关联表
CREATE TABLE IF NOT EXISTS `tb_employee_position` (
    `id`              BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `employee_id`     BIGINT NOT NULL COMMENT '员工ID',
    `position_id`     BIGINT NOT NULL COMMENT '岗位ID',
    `is_primary`      TINYINT NOT NULL DEFAULT 0 COMMENT '是否主岗位',
    `create_time`     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`       BIGINT COMMENT '创建者',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_emp_pos` (`employee_id`, `position_id`),
    KEY `idx_position_id` (`position_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工-岗位关联表';

-- 修改tb_dealership_staff表，增加employee_id字段
ALTER TABLE `tb_dealership_staff` ADD COLUMN `employee_id` BIGINT COMMENT '员工ID';
CREATE INDEX `idx_employee_id` ON `tb_dealership_staff`(`employee_id`);
