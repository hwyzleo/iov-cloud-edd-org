-- 门店表添加组织代码字段
ALTER TABLE tb_dealership ADD COLUMN org_code VARCHAR(50) DEFAULT NULL COMMENT '组织代码' AFTER area_code;