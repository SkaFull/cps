-- MySQL dump 10.13  Distrib 8.4.5, for Win64 (x86_64)
--
-- Host: localhost    Database: xc_union
-- ------------------------------------------------------
-- Server version	8.4.5

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `gen_table`
--

DROP TABLE IF EXISTS `gen_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table` (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table`
--

LOCK TABLES `gen_table` WRITE;
/*!40000 ALTER TABLE `gen_table` DISABLE KEYS */;
/*!40000 ALTER TABLE `gen_table` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gen_table_column`
--

DROP TABLE IF EXISTS `gen_table_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gen_table_column` (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代码生成业务表字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gen_table_column`
--

LOCK TABLES `gen_table_column` WRITE;
/*!40000 ALTER TABLE `gen_table_column` DISABLE KEYS */;
/*!40000 ALTER TABLE `gen_table_column` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `magic_api_backup`
--

DROP TABLE IF EXISTS `magic_api_backup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `magic_api_backup` (
  `id` varchar(32) NOT NULL COMMENT '原对象id',
  `create_date` bigint NOT NULL COMMENT '备份时间',
  `tag` varchar(32) DEFAULT NULL COMMENT '标签',
  `type` varchar(32) DEFAULT NULL COMMENT '类型',
  `name` varchar(64) DEFAULT NULL COMMENT '原名称',
  `content` blob COMMENT '备份内容',
  `create_by` varchar(64) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`,`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `magic_api_backup`
--

LOCK TABLES `magic_api_backup` WRITE;
/*!40000 ALTER TABLE `magic_api_backup` DISABLE KEYS */;
/*!40000 ALTER TABLE `magic_api_backup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `magic_api_file`
--

DROP TABLE IF EXISTS `magic_api_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `magic_api_file` (
  `file_path` varchar(512) NOT NULL,
  `file_content` mediumtext,
  PRIMARY KEY (`file_path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `magic_api_file`
--

LOCK TABLES `magic_api_file` WRITE;
/*!40000 ALTER TABLE `magic_api_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `magic_api_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_blob_triggers`
--

DROP TABLE IF EXISTS `qrtz_blob_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_blob_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `blob_data` blob COMMENT '存放持久化Trigger对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Blob类型的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_blob_triggers`
--

LOCK TABLES `qrtz_blob_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_blob_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_blob_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_calendars`
--

DROP TABLE IF EXISTS `qrtz_calendars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_calendars` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `calendar_name` varchar(200) NOT NULL COMMENT '日历名称',
  `calendar` blob NOT NULL COMMENT '存放持久化calendar对象',
  PRIMARY KEY (`sched_name`,`calendar_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='日历信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_calendars`
--

LOCK TABLES `qrtz_calendars` WRITE;
/*!40000 ALTER TABLE `qrtz_calendars` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_calendars` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_cron_triggers`
--

DROP TABLE IF EXISTS `qrtz_cron_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_cron_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `cron_expression` varchar(200) NOT NULL COMMENT 'cron表达式',
  `time_zone_id` varchar(80) DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Cron类型的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_cron_triggers`
--

LOCK TABLES `qrtz_cron_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_cron_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_cron_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_fired_triggers`
--

DROP TABLE IF EXISTS `qrtz_fired_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_fired_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `entry_id` varchar(95) NOT NULL COMMENT '调度器实例id',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `instance_name` varchar(200) NOT NULL COMMENT '调度器实例名',
  `fired_time` bigint NOT NULL COMMENT '触发的时间',
  `sched_time` bigint NOT NULL COMMENT '定时器制定的时间',
  `priority` int NOT NULL COMMENT '优先级',
  `state` varchar(16) NOT NULL COMMENT '状态',
  `job_name` varchar(200) DEFAULT NULL COMMENT '任务名称',
  `job_group` varchar(200) DEFAULT NULL COMMENT '任务组名',
  `is_nonconcurrent` varchar(1) DEFAULT NULL COMMENT '是否并发',
  `requests_recovery` varchar(1) DEFAULT NULL COMMENT '是否接受恢复执行',
  PRIMARY KEY (`sched_name`,`entry_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='已触发的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_fired_triggers`
--

LOCK TABLES `qrtz_fired_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_fired_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_fired_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_job_details`
--

DROP TABLE IF EXISTS `qrtz_job_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_job_details` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `job_name` varchar(200) NOT NULL COMMENT '任务名称',
  `job_group` varchar(200) NOT NULL COMMENT '任务组名',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `job_class_name` varchar(250) NOT NULL COMMENT '执行任务类名称',
  `is_durable` varchar(1) NOT NULL COMMENT '是否持久化',
  `is_nonconcurrent` varchar(1) NOT NULL COMMENT '是否并发',
  `is_update_data` varchar(1) NOT NULL COMMENT '是否更新数据',
  `requests_recovery` varchar(1) NOT NULL COMMENT '是否接受恢复执行',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`job_name`,`job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='任务详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_job_details`
--

LOCK TABLES `qrtz_job_details` WRITE;
/*!40000 ALTER TABLE `qrtz_job_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_job_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_locks`
--

DROP TABLE IF EXISTS `qrtz_locks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_locks` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `lock_name` varchar(40) NOT NULL COMMENT '悲观锁名称',
  PRIMARY KEY (`sched_name`,`lock_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='存储的悲观锁信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_locks`
--

LOCK TABLES `qrtz_locks` WRITE;
/*!40000 ALTER TABLE `qrtz_locks` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_locks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_paused_trigger_grps`
--

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  PRIMARY KEY (`sched_name`,`trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='暂停的触发器表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_paused_trigger_grps`
--

LOCK TABLES `qrtz_paused_trigger_grps` WRITE;
/*!40000 ALTER TABLE `qrtz_paused_trigger_grps` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_paused_trigger_grps` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_scheduler_state`
--

DROP TABLE IF EXISTS `qrtz_scheduler_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_scheduler_state` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `instance_name` varchar(200) NOT NULL COMMENT '实例名称',
  `last_checkin_time` bigint NOT NULL COMMENT '上次检查时间',
  `checkin_interval` bigint NOT NULL COMMENT '检查间隔时间',
  PRIMARY KEY (`sched_name`,`instance_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='调度器状态表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_scheduler_state`
--

LOCK TABLES `qrtz_scheduler_state` WRITE;
/*!40000 ALTER TABLE `qrtz_scheduler_state` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_scheduler_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_simple_triggers`
--

DROP TABLE IF EXISTS `qrtz_simple_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_simple_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `repeat_count` bigint NOT NULL COMMENT '重复的次数统计',
  `repeat_interval` bigint NOT NULL COMMENT '重复的间隔时间',
  `times_triggered` bigint NOT NULL COMMENT '已经触发的次数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='简单触发器的信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_simple_triggers`
--

LOCK TABLES `qrtz_simple_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_simple_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_simple_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_simprop_triggers`
--

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_simprop_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_name的外键',
  `trigger_group` varchar(200) NOT NULL COMMENT 'qrtz_triggers表trigger_group的外键',
  `str_prop_1` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第一个参数',
  `str_prop_2` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第二个参数',
  `str_prop_3` varchar(512) DEFAULT NULL COMMENT 'String类型的trigger的第三个参数',
  `int_prop_1` int DEFAULT NULL COMMENT 'int类型的trigger的第一个参数',
  `int_prop_2` int DEFAULT NULL COMMENT 'int类型的trigger的第二个参数',
  `long_prop_1` bigint DEFAULT NULL COMMENT 'long类型的trigger的第一个参数',
  `long_prop_2` bigint DEFAULT NULL COMMENT 'long类型的trigger的第二个参数',
  `dec_prop_1` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第一个参数',
  `dec_prop_2` decimal(13,4) DEFAULT NULL COMMENT 'decimal类型的trigger的第二个参数',
  `bool_prop_1` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第一个参数',
  `bool_prop_2` varchar(1) DEFAULT NULL COMMENT 'Boolean类型的trigger的第二个参数',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='同步机制的行锁表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_simprop_triggers`
--

LOCK TABLES `qrtz_simprop_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_simprop_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_simprop_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_triggers`
--

DROP TABLE IF EXISTS `qrtz_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qrtz_triggers` (
  `sched_name` varchar(120) NOT NULL COMMENT '调度名称',
  `trigger_name` varchar(200) NOT NULL COMMENT '触发器的名字',
  `trigger_group` varchar(200) NOT NULL COMMENT '触发器所属组的名字',
  `job_name` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_name的外键',
  `job_group` varchar(200) NOT NULL COMMENT 'qrtz_job_details表job_group的外键',
  `description` varchar(250) DEFAULT NULL COMMENT '相关介绍',
  `next_fire_time` bigint DEFAULT NULL COMMENT '上一次触发时间（毫秒）',
  `prev_fire_time` bigint DEFAULT NULL COMMENT '下一次触发时间（默认为-1表示不触发）',
  `priority` int DEFAULT NULL COMMENT '优先级',
  `trigger_state` varchar(16) NOT NULL COMMENT '触发器状态',
  `trigger_type` varchar(8) NOT NULL COMMENT '触发器的类型',
  `start_time` bigint NOT NULL COMMENT '开始时间',
  `end_time` bigint DEFAULT NULL COMMENT '结束时间',
  `calendar_name` varchar(200) DEFAULT NULL COMMENT '日程表名称',
  `misfire_instr` smallint DEFAULT NULL COMMENT '补偿执行的策略',
  `job_data` blob COMMENT '存放持久化job对象',
  PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`),
  KEY `sched_name` (`sched_name`,`job_name`,`job_group`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='触发器详细信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_triggers`
--

LOCK TABLES `qrtz_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_agent_commission_account`
--

DROP TABLE IF EXISTS `sys_agent_commission_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_agent_commission_account` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '账户ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `total_earned` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '累计收益',
  `self_earned` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '自购累计收益',
  `promotion_earned` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '推广累计收益',
  `withdrawn` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '已提现金额',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '账户余额',
  `frozen_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '冻结金额',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态(0:正常 1:冻结)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代理佣金账户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_agent_commission_account`
--

LOCK TABLES `sys_agent_commission_account` WRITE;
/*!40000 ALTER TABLE `sys_agent_commission_account` DISABLE KEYS */;
INSERT INTO `sys_agent_commission_account` VALUES (1,100,0.00,0.00,0.00,0.00,0.00,0.00,'0','2026-04-08 16:43:34',NULL);
/*!40000 ALTER TABLE `sys_agent_commission_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_agent_commission_config`
--

DROP TABLE IF EXISTS `sys_agent_commission_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_agent_commission_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `agent_level` int NOT NULL COMMENT '代理层级(1:一级代理 2:二级代理)',
  `self_commission_rate` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '自购佣金比例(%)',
  `promotion_commission_rate` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '推广佣金比例(%)',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态(0:正常 1:停用)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_agent_level` (`agent_level`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代理佣金配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_agent_commission_config`
--

LOCK TABLES `sys_agent_commission_config` WRITE;
/*!40000 ALTER TABLE `sys_agent_commission_config` DISABLE KEYS */;
INSERT INTO `sys_agent_commission_config` VALUES (1,1,40.00,5.00,'0','一级代理：自购佣金40%，推广佣金5%','admin','2026-04-08 15:32:28','',NULL),(2,2,40.00,0.00,'0','二级代理：自购佣金40%，无推广佣金','admin','2026-04-08 15:32:28','',NULL);
/*!40000 ALTER TABLE `sys_agent_commission_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_agent_union_relation`
--

DROP TABLE IF EXISTS `sys_agent_union_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_agent_union_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `apply_id` bigint NOT NULL COMMENT '代理申请ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `union_platform_id` bigint NOT NULL COMMENT '联盟平台ID',
  `platform_type` varchar(20) NOT NULL COMMENT '联盟类型(冗余字段,方便查询)',
  `status` char(1) DEFAULT '1' COMMENT '关联状态(0-已取消,1-正常)',
  `auth_status` char(1) DEFAULT '0' COMMENT '授权状态（0-未授权，1-已授权）',
  `custom_parameters` varchar(500) DEFAULT NULL COMMENT '自定义参数（JSON格式，存储授权备案时的custom_parameters等信息）',
  `AUTH_URL` text COMMENT '授权备案URL',
  `remark` varchar(500) DEFAULT NULL COMMENT '澶囨敞',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_apply_id` (`apply_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_union_platform_id` (`union_platform_id`),
  KEY `idx_platform_type` (`platform_type`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='代理联盟关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_agent_union_relation`
--

LOCK TABLES `sys_agent_union_relation` WRITE;
/*!40000 ALTER TABLE `sys_agent_union_relation` DISABLE KEYS */;
INSERT INTO `sys_agent_union_relation` VALUES (86,2,100,6,'dtk','1','0',NULL,NULL,NULL,'admin','2026-04-10 12:24:39',NULL,'2026-04-10 12:24:39'),(87,2,100,7,'tbk','1','0',NULL,NULL,NULL,'admin','2026-04-10 12:24:39',NULL,'2026-04-10 12:24:39'),(88,2,100,3,'jd','1','0',NULL,NULL,NULL,'admin','2026-04-10 12:24:39',NULL,'2026-04-10 12:24:39'),(89,2,100,5,'pdd','1','1',NULL,'https://mobile.yangkeduo.com/duo_coupon_landing.html?__page=auth&pid=44243516_315092954&customParameters=%7B%22uid%22%3A%22f899139df5e1059396431415e770c6dd%22%7D&cpsSign=CC_260410_44243516_315092954_48bdcc471eca5a4bbe72c6f39a935bac&_x_ddjb_act=%7B%22st%22%3A%22102%22%7D&duoduo_type=2&launch_pdd=1&campaign=ddjb&cid=launch_',NULL,'admin','2026-04-10 12:24:39','admin','2026-04-10 12:24:45');
/*!40000 ALTER TABLE `sys_agent_union_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_commission_config`
--

DROP TABLE IF EXISTS `sys_commission_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_commission_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(50) NOT NULL COMMENT '配置键(self_rate:自购比例 promotion_rate:推广比例)',
  `config_value` decimal(5,2) NOT NULL COMMENT '配置值(%)',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态(0:正常 1:停用)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='佣金配置表-简化版';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_commission_config`
--

LOCK TABLES `sys_commission_config` WRITE;
/*!40000 ALTER TABLE `sys_commission_config` DISABLE KEYS */;
INSERT INTO `sys_commission_config` VALUES (1,'self_rate',40.00,'0','自购佣金比例，所有代理统一40%','admin','2026-04-08 16:43:34','',NULL),(2,'promotion_rate',5.00,'0','推广佣金比例，有上级时上级获得5%','admin','2026-04-08 16:43:34','',NULL);
/*!40000 ALTER TABLE `sys_commission_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_commission_flow`
--

DROP TABLE IF EXISTS `sys_commission_flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_commission_flow` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '流水ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `flow_type` varchar(20) NOT NULL COMMENT '流水类型(income_self:自购收入 income_promotion:推广收入 withdraw:提现 refund:退款)',
  `amount` decimal(10,2) NOT NULL COMMENT '变动金额',
  `balance_before` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '变动前余额',
  `balance_after` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '变动后余额',
  `order_commission_id` bigint DEFAULT NULL COMMENT '关联订单佣金记录ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_flow_type` (`flow_type`),
  KEY `idx_order_commission_id` (`order_commission_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='佣金流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_commission_flow`
--

LOCK TABLES `sys_commission_flow` WRITE;
/*!40000 ALTER TABLE `sys_commission_flow` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_commission_flow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='参数配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2026-03-11 22:47:16','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2026-03-11 22:47:16','',NULL,'初始化密码 123456'),(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2026-03-11 22:47:16','',NULL,'深色主题theme-dark，浅色主题theme-light'),(4,'账号自助-验证码开关','sys.account.captchaEnabled','true','Y','admin','2026-03-11 22:47:16','',NULL,'是否开启验证码功能（true开启，false关闭）'),(5,'账号自助-是否开启用户注册功能','sys.account.registerUser','false','Y','admin','2026-03-11 22:47:16','',NULL,'是否开启注册用户功能（true开启，false关闭）'),(6,'用户登录-黑名单列表','sys.login.blackIPList','','Y','admin','2026-03-11 22:47:16','',NULL,'设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）'),(7,'用户管理-初始密码修改策略','sys.account.initPasswordModify','1','Y','admin','2026-03-11 22:47:16','',NULL,'0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框'),(8,'用户管理-账号密码更新周期','sys.account.passwordValidateDays','0','Y','admin','2026-03-11 22:47:16','',NULL,'密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框'),(100,'最低提现金额','commission.min.withdraw','100','Y','admin','2026-04-08 16:43:34','',NULL,'代理提现的最低金额(元)'),(101,'佣金冻结天数','commission.freeze.days','15','Y','admin','2026-04-08 16:43:34','',NULL,'订单佣金冻结天数，用于处理退款');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (100,0,'0','若依科技',0,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-11 22:47:11','',NULL),(101,100,'0,100','深圳总公司',1,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-11 22:47:11','',NULL),(102,100,'0,100','长沙分公司',2,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-11 22:47:11','',NULL),(103,101,'0,100,101','研发部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-11 22:47:11','',NULL),(104,101,'0,100,101','市场部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-11 22:47:11','',NULL),(105,101,'0,100,101','测试部门',3,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-11 22:47:11','',NULL),(106,101,'0,100,101','财务部门',4,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-11 22:47:11','',NULL),(107,101,'0,100,101','运维部门',5,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-11 22:47:11','',NULL),(108,102,'0,100,102','市场部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-11 22:47:11','',NULL),(109,102,'0,100,102','财务部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2026-03-11 22:47:11','',NULL);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1049 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` VALUES (1,1,'男','0','sys_user_sex','','','Y','0','admin','2026-03-11 22:47:15','',NULL,'性别男'),(2,2,'女','1','sys_user_sex','','','N','0','admin','2026-03-11 22:47:15','',NULL,'性别女'),(3,3,'未知','2','sys_user_sex','','','N','0','admin','2026-03-11 22:47:15','',NULL,'性别未知'),(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2026-03-11 22:47:15','',NULL,'显示菜单'),(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2026-03-11 22:47:15','',NULL,'隐藏菜单'),(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2026-03-11 22:47:15','',NULL,'正常状态'),(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2026-03-11 22:47:15','',NULL,'停用状态'),(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2026-03-11 22:47:15','',NULL,'正常状态'),(9,2,'暂停','1','sys_job_status','','danger','N','0','admin','2026-03-11 22:47:15','',NULL,'停用状态'),(10,1,'默认','DEFAULT','sys_job_group','','','Y','0','admin','2026-03-11 22:47:15','',NULL,'默认分组'),(11,2,'系统','SYSTEM','sys_job_group','','','N','0','admin','2026-03-11 22:47:15','',NULL,'系统分组'),(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2026-03-11 22:47:15','',NULL,'系统默认是'),(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2026-03-11 22:47:15','',NULL,'系统默认否'),(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2026-03-11 22:47:15','',NULL,'通知'),(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2026-03-11 22:47:15','',NULL,'公告'),(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2026-03-11 22:47:15','',NULL,'正常状态'),(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2026-03-11 22:47:15','',NULL,'关闭状态'),(18,99,'其他','0','sys_oper_type','','info','N','0','admin','2026-03-11 22:47:15','',NULL,'其他操作'),(19,1,'新增','1','sys_oper_type','','info','N','0','admin','2026-03-11 22:47:15','',NULL,'新增操作'),(20,2,'修改','2','sys_oper_type','','info','N','0','admin','2026-03-11 22:47:15','',NULL,'修改操作'),(21,3,'删除','3','sys_oper_type','','danger','N','0','admin','2026-03-11 22:47:15','',NULL,'删除操作'),(22,4,'授权','4','sys_oper_type','','primary','N','0','admin','2026-03-11 22:47:15','',NULL,'授权操作'),(23,5,'导出','5','sys_oper_type','','warning','N','0','admin','2026-03-11 22:47:15','',NULL,'导出操作'),(24,6,'导入','6','sys_oper_type','','warning','N','0','admin','2026-03-11 22:47:15','',NULL,'导入操作'),(25,7,'强退','7','sys_oper_type','','danger','N','0','admin','2026-03-11 22:47:15','',NULL,'强退操作'),(26,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2026-03-11 22:47:15','',NULL,'生成操作'),(27,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2026-03-11 22:47:15','',NULL,'清空操作'),(28,1,'成功','0','sys_common_status','','primary','N','0','admin','2026-03-11 22:47:15','',NULL,'正常状态'),(29,2,'失败','1','sys_common_status','','danger','N','0','admin','2026-03-11 22:47:15','',NULL,'停用状态'),(1000,1,'待审核','0','sys_tbk_apply_status','','info','N','0','admin','2026-03-14 16:54:11','',NULL,'待审核状态'),(1001,2,'已生效','1','sys_tbk_apply_status','','success','N','0','admin','2026-03-14 16:54:11','',NULL,'已生效状态'),(1002,3,'已失效','2','sys_tbk_apply_status','','danger','N','0','admin','2026-03-14 16:54:11','',NULL,'已失效状态'),(1003,1,'淘宝客','tbk','union_platform_type','','primary','Y','0','admin','2026-04-03 12:22:20','',NULL,'淘宝联盟'),(1004,2,'京东联盟','jd','union_platform_type','','success','N','0','admin','2026-04-03 12:22:20','',NULL,'京东联盟'),(1005,3,'拼多多','pdd','union_platform_type','','warning','N','0','admin','2026-04-03 12:22:20','',NULL,'拼多多联盟'),(1006,4,'大淘客','dtk','union_platform_type','','info','N','0','admin','2026-04-03 12:22:20','',NULL,'大淘客联盟'),(1007,1,'未被应用','0','union_quote_status','','info','Y','0','admin','2026-04-03 12:22:20','',NULL,'联盟账号未被使用'),(1008,2,'使用中','1','union_quote_status','','success','N','0','admin','2026-04-03 12:22:20','',NULL,'联盟账号正在使用'),(1009,3,'已失效','2','union_quote_status','','danger','N','0','admin','2026-04-03 12:22:20','',NULL,'联盟账号已失效'),(1010,5,'抖音联盟','dy','union_platform_type',NULL,'danger','N','0','admin','2026-04-04 17:10:07','admin','2026-04-04 17:11:59','抖音精选联盟'),(1021,1,'10','1','agent_level_subordinate_limit','','default','N','0','admin','2026-04-08 11:33:32','',NULL,'1级代理最多可拥有10个下级'),(1022,2,'20','2','agent_level_subordinate_limit','','default','N','0','admin','2026-04-08 11:33:32','',NULL,'2级代理最多可拥有20个下级'),(1023,3,'30','3','agent_level_subordinate_limit','','default','N','0','admin','2026-04-08 11:33:32','',NULL,'3级代理最多可拥有30个下级'),(1024,4,'40','4','agent_level_subordinate_limit','','default','N','0','admin','2026-04-08 11:33:32','',NULL,'4级代理最多可拥有40个下级'),(1025,5,'50','5','agent_level_subordinate_limit','','default','N','0','admin','2026-04-08 11:33:32','',NULL,'5级代理最多可拥有50个下级'),(1026,6,'60','6','agent_level_subordinate_limit','','default','N','0','admin','2026-04-08 11:33:32','',NULL,'6级代理最多可拥有60个下级'),(1027,7,'70','7','agent_level_subordinate_limit','','default','N','0','admin','2026-04-08 11:33:32','',NULL,'7级代理最多可拥有70个下级'),(1028,8,'80','8','agent_level_subordinate_limit','','default','N','0','admin','2026-04-08 11:33:32','',NULL,'8级代理最多可拥有80个下级'),(1029,9,'90','9','agent_level_subordinate_limit','','default','N','0','admin','2026-04-08 11:33:32','',NULL,'9级代理最多可拥有90个下级'),(1030,10,'100','10','agent_level_subordinate_limit','','default','N','0','admin','2026-04-08 11:33:32','',NULL,'10级代理最多可拥有100个下级'),(1031,1,'佣金收入','1','commission_flow_type',NULL,NULL,'N','0','admin','2026-04-08 15:32:28','',NULL,NULL),(1032,2,'佣金提现','2','commission_flow_type',NULL,NULL,'N','0','admin','2026-04-08 15:32:28','',NULL,NULL),(1033,3,'佣金退回','3','commission_flow_type',NULL,NULL,'N','0','admin','2026-04-08 15:32:28','',NULL,NULL),(1034,4,'佣金冻结','4','commission_flow_type',NULL,NULL,'N','0','admin','2026-04-08 15:32:28','',NULL,NULL),(1035,5,'佣金解冻','5','commission_flow_type',NULL,NULL,'N','0','admin','2026-04-08 15:32:28','',NULL,NULL),(1036,1,'待确认','0','commission_order_status',NULL,NULL,'N','0','admin','2026-04-08 15:32:28','',NULL,NULL),(1037,2,'已完成','1','commission_order_status',NULL,NULL,'N','0','admin','2026-04-08 15:32:28','',NULL,NULL),(1038,3,'已退款','2','commission_order_status',NULL,NULL,'N','0','admin','2026-04-08 15:32:28','',NULL,NULL),(1039,1,'未结算','0','commission_settlement_status',NULL,NULL,'N','0','admin','2026-04-08 15:32:28','',NULL,NULL),(1040,2,'已结算','1','commission_settlement_status',NULL,NULL,'N','0','admin','2026-04-08 15:32:28','',NULL,NULL),(1041,3,'已冻结','2','commission_settlement_status',NULL,NULL,'N','0','admin','2026-04-08 15:32:28','',NULL,NULL),(1042,1,'自购收入','income_self','commission_flow_type',NULL,NULL,'N','0','admin','2026-04-08 16:43:34','',NULL,'自购订单佣金收入'),(1043,2,'推广收入','income_promotion','commission_flow_type',NULL,NULL,'N','0','admin','2026-04-08 16:43:34','',NULL,'推广下级订单佣金收入'),(1044,3,'提现','withdraw','commission_flow_type',NULL,NULL,'N','0','admin','2026-04-08 16:43:34','',NULL,'佣金提现'),(1045,4,'退款','refund','commission_flow_type',NULL,NULL,'N','0','admin','2026-04-08 16:43:34','',NULL,'订单退款扣除佣金'),(1046,1,'待结算','pending','commission_order_status',NULL,NULL,'N','0','admin','2026-04-08 16:43:34','',NULL,'订单待结算状态'),(1047,2,'已结算','settled','commission_order_status',NULL,NULL,'N','0','admin','2026-04-08 16:43:34','',NULL,'订单已结算状态'),(1048,3,'已取消','cancelled','commission_order_status',NULL,NULL,'N','0','admin','2026-04-08 16:43:34','',NULL,'订单已取消状态');
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`)
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` VALUES (1,'用户性别','sys_user_sex','0','admin','2026-03-11 22:47:15','',NULL,'用户性别列表'),(2,'菜单状态','sys_show_hide','0','admin','2026-03-11 22:47:15','',NULL,'菜单状态列表'),(3,'系统开关','sys_normal_disable','0','admin','2026-03-11 22:47:15','',NULL,'系统开关列表'),(4,'任务状态','sys_job_status','0','admin','2026-03-11 22:47:15','',NULL,'任务状态列表'),(5,'任务分组','sys_job_group','0','admin','2026-03-11 22:47:15','',NULL,'任务分组列表'),(6,'系统是否','sys_yes_no','0','admin','2026-03-11 22:47:15','',NULL,'系统是否列表'),(7,'通知类型','sys_notice_type','0','admin','2026-03-11 22:47:15','',NULL,'通知类型列表'),(8,'通知状态','sys_notice_status','0','admin','2026-03-11 22:47:15','',NULL,'通知状态列表'),(9,'操作类型','sys_oper_type','0','admin','2026-03-11 22:47:15','',NULL,'操作类型列表'),(10,'系统状态','sys_common_status','0','admin','2026-03-11 22:47:15','',NULL,'登录状态列表'),(100,'申请状态','sys_tbk_apply_status','0','admin','2026-03-14 16:54:11','',NULL,'代理申请状态'),(101,'联盟类型','union_platform_type','0','admin','2026-04-03 12:22:20','admin','2026-04-04 17:08:21','联盟平台类型列表'),(102,'联盟使用状态','union_quote_status','0','admin','2026-04-03 12:22:20','',NULL,'联盟账号使用状态'),(103,'代理级别下级数量限制','agent_level_subordinate_limit','0','admin','2026-04-08 09:21:14','',NULL,'定义各级别代理可拥有的最大下级数量'),(104,'佣金流水类型','commission_flow_type','0','admin','2026-04-08 15:32:28','admin','2026-04-08 16:43:34','代理佣金流水类型'),(105,'佣金订单状态','commission_order_status','0','admin','2026-04-08 15:32:28','admin','2026-04-08 16:43:34','佣金订单状态'),(106,'佣金结算状态','commission_settlement_status','0','admin','2026-04-08 15:32:28','',NULL,'佣金结算状态');
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job`
--

DROP TABLE IF EXISTS `sys_job`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job` (
  `job_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `job_name` varchar(64) NOT NULL DEFAULT '' COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL DEFAULT 'DEFAULT' COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `cron_expression` varchar(255) DEFAULT '' COMMENT 'cron执行表达式',
  `misfire_policy` varchar(20) DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  `concurrent` char(1) DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1暂停）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注信息',
  PRIMARY KEY (`job_id`,`job_name`,`job_group`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务调度表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job`
--

LOCK TABLES `sys_job` WRITE;
/*!40000 ALTER TABLE `sys_job` DISABLE KEYS */;
INSERT INTO `sys_job` VALUES (1,'系统默认（无参）','DEFAULT','ryTask.ryNoParams','0/10 * * * * ?','3','1','1','admin','2026-03-11 22:47:16','',NULL,''),(2,'系统默认（有参）','DEFAULT','ryTask.ryParams(\'ry\')','0/15 * * * * ?','3','1','1','admin','2026-03-11 22:47:16','',NULL,''),(3,'系统默认（多参）','DEFAULT','ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)','0/20 * * * * ?','3','1','1','admin','2026-03-11 22:47:16','',NULL,'');
/*!40000 ALTER TABLE `sys_job` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_job_log`
--

DROP TABLE IF EXISTS `sys_job_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_job_log` (
  `job_log_id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务日志ID',
  `job_name` varchar(64) NOT NULL COMMENT '任务名称',
  `job_group` varchar(64) NOT NULL COMMENT '任务组名',
  `invoke_target` varchar(500) NOT NULL COMMENT '调用目标字符串',
  `job_message` varchar(500) DEFAULT NULL COMMENT '日志信息',
  `status` char(1) DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
  `exception_info` varchar(2000) DEFAULT '' COMMENT '异常信息',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`job_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='定时任务调度日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_job_log`
--

LOCK TABLES `sys_job_log` WRITE;
/*!40000 ALTER TABLE `sys_job_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_job_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_logininfor`
--

DROP TABLE IF EXISTS `sys_logininfor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_logininfor` (
  `info_id` bigint NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`),
  KEY `idx_sys_logininfor_s` (`status`),
  KEY `idx_sys_logininfor_lt` (`login_time`)
) ENGINE=InnoDB AUTO_INCREMENT=133 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统访问记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_logininfor`
--

LOCK TABLES `sys_logininfor` WRITE;
/*!40000 ALTER TABLE `sys_logininfor` DISABLE KEYS */;
INSERT INTO `sys_logininfor` VALUES (100,'admin','127.0.0.1','内网IP','Chrome 145','Windows10','0','登录成功','2026-03-12 16:54:46'),(101,'admin','127.0.0.1','内网IP','Chrome 145','Windows10','0','登录成功','2026-03-12 22:53:32'),(102,'admin','127.0.0.1','内网IP','Chrome 145','Windows10','0','登录成功','2026-03-15 10:53:07'),(103,'admin','127.0.0.1','内网IP','Chrome 145','Windows10','0','登录成功','2026-03-15 22:50:06'),(104,'admin','127.0.0.1','内网IP','Chrome 145','Windows10','0','登录成功','2026-03-16 00:58:09'),(105,'admin','127.0.0.1','内网IP','Chrome 145','Windows10','0','登录成功','2026-03-17 15:24:18'),(106,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','1','验证码已失效','2026-04-02 15:02:58'),(107,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-02 15:02:58'),(108,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-03 10:55:38'),(109,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-03 11:28:00'),(110,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-03 11:28:03'),(111,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-03 12:33:29'),(112,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-03 14:08:20'),(113,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-03 22:52:26'),(114,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-03 22:52:36'),(115,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-04 12:35:36'),(116,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-04 15:57:13'),(117,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','退出成功','2026-04-04 15:57:32'),(118,'wx_TE_21GxM','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-04 15:57:38'),(119,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','1','验证码错误','2026-04-04 16:30:24'),(120,'wx_TE_21GxM','127.0.0.1','内网IP','Chrome 146','Windows10','0','退出成功','2026-04-04 16:30:24'),(121,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-04 16:30:28'),(122,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-08 10:28:28'),(123,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-08 14:32:19'),(124,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-08 14:32:57'),(125,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-09 11:05:03'),(126,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-09 12:00:05'),(127,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-09 14:07:39'),(128,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-09 14:47:02'),(129,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-10 09:28:18'),(130,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','1','验证码错误','2026-04-10 10:58:07'),(131,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-10 10:58:08'),(132,'admin','127.0.0.1','内网IP','Chrome 146','Windows10','0','登录成功','2026-04-10 12:00:08');
/*!40000 ALTER TABLE `sys_logininfor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) DEFAULT '' COMMENT '路由名称',
  `is_frame` int DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2059 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'系统管理',0,4,'system',NULL,'','',1,0,'M','0','0','','system','admin','2026-03-11 22:47:11','admin','2026-04-09 11:27:26','系统管理目录'),(2,'系统监控',0,5,'monitor',NULL,'','',1,0,'M','0','0','','monitor','admin','2026-03-11 22:47:11','admin','2026-04-09 11:27:40','系统监控目录'),(3,'系统工具',0,6,'tool',NULL,'','',1,0,'M','0','0','','tool','admin','2026-03-11 22:47:11','admin','2026-04-09 11:27:50','系统工具目录'),(4,'若依官网',0,7,'http://ruoyi.vip',NULL,'','',0,0,'M','0','1','','guide','admin','2026-03-11 22:47:11','admin','2026-04-09 11:27:55','若依官网地址'),(100,'用户管理',1,1,'user','system/user/index','','',1,0,'C','0','0','system:user:list','user','admin','2026-03-11 22:47:11','',NULL,'用户管理菜单'),(101,'角色管理',1,2,'role','system/role/index','','',1,0,'C','0','0','system:role:list','peoples','admin','2026-03-11 22:47:11','',NULL,'角色管理菜单'),(102,'菜单管理',1,3,'menu','system/menu/index','','',1,0,'C','0','0','system:menu:list','tree-table','admin','2026-03-11 22:47:11','',NULL,'菜单管理菜单'),(103,'部门管理',1,4,'dept','system/dept/index','','',1,0,'C','0','0','system:dept:list','tree','admin','2026-03-11 22:47:11','',NULL,'部门管理菜单'),(104,'岗位管理',1,5,'post','system/post/index','','',1,0,'C','0','0','system:post:list','post','admin','2026-03-11 22:47:11','',NULL,'岗位管理菜单'),(105,'字典管理',1,6,'dict','system/dict/index','','',1,0,'C','0','0','system:dict:list','dict','admin','2026-03-11 22:47:11','',NULL,'字典管理菜单'),(106,'参数设置',1,7,'config','system/config/index','','',1,0,'C','0','0','system:config:list','edit','admin','2026-03-11 22:47:11','',NULL,'参数设置菜单'),(107,'通知公告',1,8,'notice','system/notice/index','','',1,0,'C','0','0','system:notice:list','message','admin','2026-03-11 22:47:11','',NULL,'通知公告菜单'),(108,'日志管理',1,9,'log','','','',1,0,'M','0','0','','log','admin','2026-03-11 22:47:11','',NULL,'日志管理菜单'),(109,'在线用户',2,1,'online','monitor/online/index','','',1,0,'C','0','0','monitor:online:list','online','admin','2026-03-11 22:47:11','',NULL,'在线用户菜单'),(110,'定时任务',2,2,'job','monitor/job/index','','',1,0,'C','0','0','monitor:job:list','job','admin','2026-03-11 22:47:11','',NULL,'定时任务菜单'),(111,'数据监控',2,3,'druid','monitor/druid/index','','',1,0,'C','0','0','monitor:druid:list','druid','admin','2026-03-11 22:47:11','',NULL,'数据监控菜单'),(112,'服务监控',2,4,'server','monitor/server/index','','',1,0,'C','0','0','monitor:server:list','server','admin','2026-03-11 22:47:11','',NULL,'服务监控菜单'),(113,'缓存监控',2,5,'cache','monitor/cache/index','','',1,0,'C','0','0','monitor:cache:list','redis','admin','2026-03-11 22:47:11','',NULL,'缓存监控菜单'),(114,'缓存列表',2,6,'cacheList','monitor/cache/list','','',1,0,'C','0','0','monitor:cache:list','redis-list','admin','2026-03-11 22:47:11','',NULL,'缓存列表菜单'),(115,'表单构建',3,1,'build','tool/build/index','','',1,0,'C','0','0','tool:build:list','build','admin','2026-03-11 22:47:11','',NULL,'表单构建菜单'),(116,'代码生成',3,2,'gen','tool/gen/index','','',1,0,'C','0','0','tool:gen:list','code','admin','2026-03-11 22:47:11','',NULL,'代码生成菜单'),(117,'系统接口',3,3,'swagger','tool/swagger/index','','',1,0,'C','0','0','tool:swagger:list','swagger','admin','2026-03-11 22:47:11','',NULL,'系统接口菜单'),(500,'操作日志',108,1,'operlog','monitor/operlog/index','','',1,0,'C','0','0','monitor:operlog:list','form','admin','2026-03-11 22:47:11','',NULL,'操作日志菜单'),(501,'登录日志',108,2,'logininfor','monitor/logininfor/index','','',1,0,'C','0','0','monitor:logininfor:list','logininfor','admin','2026-03-11 22:47:11','',NULL,'登录日志菜单'),(1000,'用户查询',100,1,'','','','',1,0,'F','0','0','system:user:query','#','admin','2026-03-11 22:47:11','',NULL,''),(1001,'用户新增',100,2,'','','','',1,0,'F','0','0','system:user:add','#','admin','2026-03-11 22:47:11','',NULL,''),(1002,'用户修改',100,3,'','','','',1,0,'F','0','0','system:user:edit','#','admin','2026-03-11 22:47:11','',NULL,''),(1003,'用户删除',100,4,'','','','',1,0,'F','0','0','system:user:remove','#','admin','2026-03-11 22:47:11','',NULL,''),(1004,'用户导出',100,5,'','','','',1,0,'F','0','0','system:user:export','#','admin','2026-03-11 22:47:11','',NULL,''),(1005,'用户导入',100,6,'','','','',1,0,'F','0','0','system:user:import','#','admin','2026-03-11 22:47:11','',NULL,''),(1006,'重置密码',100,7,'','','','',1,0,'F','0','0','system:user:resetPwd','#','admin','2026-03-11 22:47:11','',NULL,''),(1007,'角色查询',101,1,'','','','',1,0,'F','0','0','system:role:query','#','admin','2026-03-11 22:47:11','',NULL,''),(1008,'角色新增',101,2,'','','','',1,0,'F','0','0','system:role:add','#','admin','2026-03-11 22:47:11','',NULL,''),(1009,'角色修改',101,3,'','','','',1,0,'F','0','0','system:role:edit','#','admin','2026-03-11 22:47:11','',NULL,''),(1010,'角色删除',101,4,'','','','',1,0,'F','0','0','system:role:remove','#','admin','2026-03-11 22:47:11','',NULL,''),(1011,'角色导出',101,5,'','','','',1,0,'F','0','0','system:role:export','#','admin','2026-03-11 22:47:12','',NULL,''),(1012,'菜单查询',102,1,'','','','',1,0,'F','0','0','system:menu:query','#','admin','2026-03-11 22:47:12','',NULL,''),(1013,'菜单新增',102,2,'','','','',1,0,'F','0','0','system:menu:add','#','admin','2026-03-11 22:47:12','',NULL,''),(1014,'菜单修改',102,3,'','','','',1,0,'F','0','0','system:menu:edit','#','admin','2026-03-11 22:47:12','',NULL,''),(1015,'菜单删除',102,4,'','','','',1,0,'F','0','0','system:menu:remove','#','admin','2026-03-11 22:47:12','',NULL,''),(1016,'部门查询',103,1,'','','','',1,0,'F','0','0','system:dept:query','#','admin','2026-03-11 22:47:12','',NULL,''),(1017,'部门新增',103,2,'','','','',1,0,'F','0','0','system:dept:add','#','admin','2026-03-11 22:47:12','',NULL,''),(1018,'部门修改',103,3,'','','','',1,0,'F','0','0','system:dept:edit','#','admin','2026-03-11 22:47:12','',NULL,''),(1019,'部门删除',103,4,'','','','',1,0,'F','0','0','system:dept:remove','#','admin','2026-03-11 22:47:12','',NULL,''),(1020,'岗位查询',104,1,'','','','',1,0,'F','0','0','system:post:query','#','admin','2026-03-11 22:47:12','',NULL,''),(1021,'岗位新增',104,2,'','','','',1,0,'F','0','0','system:post:add','#','admin','2026-03-11 22:47:12','',NULL,''),(1022,'岗位修改',104,3,'','','','',1,0,'F','0','0','system:post:edit','#','admin','2026-03-11 22:47:12','',NULL,''),(1023,'岗位删除',104,4,'','','','',1,0,'F','0','0','system:post:remove','#','admin','2026-03-11 22:47:12','',NULL,''),(1024,'岗位导出',104,5,'','','','',1,0,'F','0','0','system:post:export','#','admin','2026-03-11 22:47:12','',NULL,''),(1025,'字典查询',105,1,'#','','','',1,0,'F','0','0','system:dict:query','#','admin','2026-03-11 22:47:12','',NULL,''),(1026,'字典新增',105,2,'#','','','',1,0,'F','0','0','system:dict:add','#','admin','2026-03-11 22:47:12','',NULL,''),(1027,'字典修改',105,3,'#','','','',1,0,'F','0','0','system:dict:edit','#','admin','2026-03-11 22:47:12','',NULL,''),(1028,'字典删除',105,4,'#','','','',1,0,'F','0','0','system:dict:remove','#','admin','2026-03-11 22:47:12','',NULL,''),(1029,'字典导出',105,5,'#','','','',1,0,'F','0','0','system:dict:export','#','admin','2026-03-11 22:47:12','',NULL,''),(1030,'参数查询',106,1,'#','','','',1,0,'F','0','0','system:config:query','#','admin','2026-03-11 22:47:12','',NULL,''),(1031,'参数新增',106,2,'#','','','',1,0,'F','0','0','system:config:add','#','admin','2026-03-11 22:47:12','',NULL,''),(1032,'参数修改',106,3,'#','','','',1,0,'F','0','0','system:config:edit','#','admin','2026-03-11 22:47:12','',NULL,''),(1033,'参数删除',106,4,'#','','','',1,0,'F','0','0','system:config:remove','#','admin','2026-03-11 22:47:12','',NULL,''),(1034,'参数导出',106,5,'#','','','',1,0,'F','0','0','system:config:export','#','admin','2026-03-11 22:47:12','',NULL,''),(1035,'公告查询',107,1,'#','','','',1,0,'F','0','0','system:notice:query','#','admin','2026-03-11 22:47:12','',NULL,''),(1036,'公告新增',107,2,'#','','','',1,0,'F','0','0','system:notice:add','#','admin','2026-03-11 22:47:12','',NULL,''),(1037,'公告修改',107,3,'#','','','',1,0,'F','0','0','system:notice:edit','#','admin','2026-03-11 22:47:12','',NULL,''),(1038,'公告删除',107,4,'#','','','',1,0,'F','0','0','system:notice:remove','#','admin','2026-03-11 22:47:12','',NULL,''),(1039,'操作查询',500,1,'#','','','',1,0,'F','0','0','monitor:operlog:query','#','admin','2026-03-11 22:47:12','',NULL,''),(1040,'操作删除',500,2,'#','','','',1,0,'F','0','0','monitor:operlog:remove','#','admin','2026-03-11 22:47:12','',NULL,''),(1041,'日志导出',500,3,'#','','','',1,0,'F','0','0','monitor:operlog:export','#','admin','2026-03-11 22:47:12','',NULL,''),(1042,'登录查询',501,1,'#','','','',1,0,'F','0','0','monitor:logininfor:query','#','admin','2026-03-11 22:47:12','',NULL,''),(1043,'登录删除',501,2,'#','','','',1,0,'F','0','0','monitor:logininfor:remove','#','admin','2026-03-11 22:47:12','',NULL,''),(1044,'日志导出',501,3,'#','','','',1,0,'F','0','0','monitor:logininfor:export','#','admin','2026-03-11 22:47:12','',NULL,''),(1045,'账户解锁',501,4,'#','','','',1,0,'F','0','0','monitor:logininfor:unlock','#','admin','2026-03-11 22:47:12','',NULL,''),(1046,'在线查询',109,1,'#','','','',1,0,'F','0','0','monitor:online:query','#','admin','2026-03-11 22:47:12','',NULL,''),(1047,'批量强退',109,2,'#','','','',1,0,'F','0','0','monitor:online:batchLogout','#','admin','2026-03-11 22:47:12','',NULL,''),(1048,'单条强退',109,3,'#','','','',1,0,'F','0','0','monitor:online:forceLogout','#','admin','2026-03-11 22:47:12','',NULL,''),(1049,'任务查询',110,1,'#','','','',1,0,'F','0','0','monitor:job:query','#','admin','2026-03-11 22:47:12','',NULL,''),(1050,'任务新增',110,2,'#','','','',1,0,'F','0','0','monitor:job:add','#','admin','2026-03-11 22:47:12','',NULL,''),(1051,'任务修改',110,3,'#','','','',1,0,'F','0','0','monitor:job:edit','#','admin','2026-03-11 22:47:12','',NULL,''),(1052,'任务删除',110,4,'#','','','',1,0,'F','0','0','monitor:job:remove','#','admin','2026-03-11 22:47:12','',NULL,''),(1053,'状态修改',110,5,'#','','','',1,0,'F','0','0','monitor:job:changeStatus','#','admin','2026-03-11 22:47:12','',NULL,''),(1054,'任务导出',110,6,'#','','','',1,0,'F','0','0','monitor:job:export','#','admin','2026-03-11 22:47:12','',NULL,''),(1055,'生成查询',116,1,'#','','','',1,0,'F','0','0','tool:gen:query','#','admin','2026-03-11 22:47:12','',NULL,''),(1056,'生成修改',116,2,'#','','','',1,0,'F','0','0','tool:gen:edit','#','admin','2026-03-11 22:47:12','',NULL,''),(1057,'生成删除',116,3,'#','','','',1,0,'F','0','0','tool:gen:remove','#','admin','2026-03-11 22:47:12','',NULL,''),(1058,'导入代码',116,4,'#','','','',1,0,'F','0','0','tool:gen:import','#','admin','2026-03-11 22:47:12','',NULL,''),(1059,'预览代码',116,5,'#','','','',1,0,'F','0','0','tool:gen:preview','#','admin','2026-03-11 22:47:12','',NULL,''),(1060,'生成代码',116,6,'#','','','',1,0,'F','0','0','tool:gen:code','#','admin','2026-03-11 22:47:12','',NULL,''),(2000,'联盟管理',0,0,'/union',NULL,NULL,'',1,0,'M','0','0',NULL,'shopping','admin','2026-03-12 16:57:34','',NULL,''),(2001,'淘宝客',2000,2,'union','',NULL,'',1,0,'M','0','0','','monitor','admin','2026-03-12 17:01:01','admin','2026-04-03 23:40:31',''),(2002,'店铺搜索',2001,1,'tbk','union/tbk/shop/index',NULL,'',1,0,'C','0','0','union:tbk:shop','monitor','admin','2026-03-12 17:07:47','',NULL,''),(2003,'淘宝商品列表',2001,2,'tbk/goods','union/tbk/goods/index',NULL,'',1,0,'C','0','0','union:tbk:dg:material:optional:upgrade','list','admin','2026-03-12 23:45:46','admin','2026-03-13 00:43:09',''),(2004,'按钮权限',2001,30,'/tbk/bution',NULL,NULL,'',1,0,'M','1','0','','skill','admin','2026-03-13 00:32:07','admin','2026-03-13 00:37:10',''),(2005,'获取淘口令',2004,1,'',NULL,NULL,'',1,0,'F','0','0','union:tbk:tpwd','#','admin','2026-03-13 00:32:40','',NULL,''),(2006,'物料精选',2001,3,'tbk/promotion','union/tbk/promotion/index',NULL,'',1,0,'C','0','0','union:tbk:dg:optimus:promotion','list','admin','2026-03-13 00:41:08','admin','2026-03-13 00:44:44',''),(2007,'物料精选升级版',2001,4,'tbk/material','union/tbk/material/index',NULL,'',1,0,'C','0','0','union:tbk:dg:material:recommend','list','admin','2026-03-13 00:46:32','',NULL,''),(2008,'物料id列表查询',2004,2,'',NULL,NULL,'',1,0,'F','0','0','union:tbk:optimus:tou:material:ids:get','#','admin','2026-03-13 01:01:17','',NULL,''),(2010,'代理申请管理',2000,0,'tbkApply/index','system/tbkApply/index',NULL,'',1,0,'C','0','0','system:tbkApply:list','system','admin','2026-03-15 11:17:32','admin','2026-04-03 23:39:55',''),(2011,'联盟信息管理',2000,1,'unionPlatform','system/unionPlatform/index',NULL,'',1,0,'C','0','0','system:unionPlatform:list','link','admin','2026-04-03 10:31:40','admin','2026-04-03 23:40:24','联盟信息管理菜单'),(2012,'联盟信息查询',2011,1,'#','',NULL,'',1,0,'F','0','0','system:unionPlatform:query','#','admin','2026-04-03 10:31:40','',NULL,''),(2013,'联盟信息新增',2011,2,'#','',NULL,'',1,0,'F','0','0','system:unionPlatform:add','#','admin','2026-04-03 10:31:40','',NULL,''),(2014,'联盟信息修改',2011,3,'#','',NULL,'',1,0,'F','0','0','system:unionPlatform:edit','#','admin','2026-04-03 10:31:40','',NULL,''),(2015,'联盟信息删除',2011,4,'#','',NULL,'',1,0,'F','0','0','system:unionPlatform:remove','#','admin','2026-04-03 10:31:40','',NULL,''),(2016,'联盟信息导出',2011,5,'#','',NULL,'',1,0,'F','0','0','system:unionPlatform:export','#','admin','2026-04-03 10:31:40','',NULL,''),(2017,'分配联盟',2010,6,'#','',NULL,'',1,0,'F','0','0','system:tbkApply:assign','#','admin','2026-04-03 10:31:40','',NULL,''),(2018,'取消分配',2010,7,'#','',NULL,'',1,0,'F','0','0','system:tbkApply:cancel','#','admin','2026-04-03 10:31:40','',NULL,''),(2038,'佣金管理',0,1,'commission',NULL,NULL,'',1,0,'M','0','0','','money','admin','2026-04-09 09:33:19','admin','2026-04-09 11:27:14','佣金分成系统管理菜单'),(2039,'佣金配置',2038,1,'config','system/commission/config/index',NULL,'',1,0,'C','0','0','system:commission:config:list','edit','admin','2026-04-09 09:33:19','',NULL,'佣金配置管理菜单'),(2040,'佣金配置查询',2039,1,'',NULL,NULL,'',1,0,'F','0','0','system:commission:config:query','#','admin','2026-04-09 09:33:19','',NULL,''),(2041,'佣金配置新增',2039,2,'',NULL,NULL,'',1,0,'F','0','0','system:commission:config:add','#','admin','2026-04-09 09:33:19','',NULL,''),(2042,'佣金配置修改',2039,3,'',NULL,NULL,'',1,0,'F','0','0','system:commission:config:edit','#','admin','2026-04-09 09:33:19','',NULL,''),(2043,'佣金配置删除',2039,4,'',NULL,NULL,'',1,0,'F','0','0','system:commission:config:remove','#','admin','2026-04-09 09:33:19','',NULL,''),(2044,'佣金配置导出',2039,5,'',NULL,NULL,'',1,0,'F','0','0','system:commission:config:export','#','admin','2026-04-09 09:33:19','',NULL,''),(2045,'订单佣金',2038,2,'order','system/commission/order/index',NULL,'',1,0,'C','0','0','system:commission:order:list','shopping','admin','2026-04-09 09:33:19','',NULL,'订单佣金管理菜单'),(2046,'订单佣金查询',2045,1,'',NULL,NULL,'',1,0,'F','0','0','system:commission:order:query','#','admin','2026-04-09 09:33:19','',NULL,''),(2047,'订单结算',2045,2,'',NULL,NULL,'',1,0,'F','0','0','system:commission:order:settle','#','admin','2026-04-09 09:33:19','',NULL,''),(2048,'订单退款',2045,3,'',NULL,NULL,'',1,0,'F','0','0','system:commission:order:refund','#','admin','2026-04-09 09:33:19','',NULL,''),(2049,'订单佣金导出',2045,4,'',NULL,NULL,'',1,0,'F','0','0','system:commission:order:export','#','admin','2026-04-09 09:33:19','',NULL,''),(2050,'佣金账户',2038,3,'account','system/commission/account/index',NULL,'',1,0,'C','0','0','system:commission:account:list','user','admin','2026-04-09 09:33:19','',NULL,'佣金账户管理菜单'),(2051,'佣金账户查询',2050,1,'',NULL,NULL,'',1,0,'F','0','0','system:commission:account:query','#','admin','2026-04-09 09:33:19','',NULL,''),(2052,'佣金账户修改',2050,2,'',NULL,NULL,'',1,0,'F','0','0','system:commission:account:edit','#','admin','2026-04-09 09:33:19','',NULL,''),(2053,'佣金账户导出',2050,3,'',NULL,NULL,'',1,0,'F','0','0','system:commission:account:export','#','admin','2026-04-09 09:33:19','',NULL,''),(2054,'佣金流水',2038,4,'flow','system/commission/flow/index',NULL,'',1,0,'C','0','0','system:commission:flow:list','list','admin','2026-04-09 09:33:19','',NULL,'佣金流水管理菜单'),(2055,'佣金流水查询',2054,1,'',NULL,NULL,'',1,0,'F','0','0','system:commission:flow:query','#','admin','2026-04-09 09:33:19','',NULL,''),(2056,'佣金流水导出',2054,2,'',NULL,NULL,'',1,0,'F','0','0','system:commission:flow:export','#','admin','2026-04-09 09:33:19','',NULL,'');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_miniapp_login_log`
--

DROP TABLE IF EXISTS `sys_miniapp_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_miniapp_login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `openid` varchar(100) NOT NULL COMMENT '微信OpenID',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `login_ip` varchar(128) DEFAULT NULL COMMENT '登录IP',
  `login_location` varchar(255) DEFAULT NULL COMMENT '登录地点',
  `browser` varchar(50) DEFAULT NULL COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `device_type` varchar(20) DEFAULT NULL COMMENT '设备类型(miniapp-小程序)',
  `device_brand` varchar(50) DEFAULT NULL COMMENT '设备品牌',
  `device_model` varchar(100) DEFAULT NULL COMMENT '设备型号',
  `mini_app_version` varchar(50) DEFAULT NULL COMMENT '小程序版本',
  `wx_version` varchar(50) DEFAULT NULL COMMENT '微信版本',
  `status` char(1) DEFAULT '0' COMMENT '登录状态(0-成功,1-失败)',
  `msg` varchar(255) DEFAULT NULL COMMENT '提示消息',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_openid` (`openid`),
  KEY `idx_login_time` (`login_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小程序登录记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_miniapp_login_log`
--

LOCK TABLES `sys_miniapp_login_log` WRITE;
/*!40000 ALTER TABLE `sys_miniapp_login_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_miniapp_login_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_miniapp_session`
--

DROP TABLE IF EXISTS `sys_miniapp_session`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_miniapp_session` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会话ID',
  `session_key` varchar(100) NOT NULL COMMENT '微信会话密钥',
  `openid` varchar(100) NOT NULL COMMENT '用户OpenID',
  `unionid` varchar(100) DEFAULT NULL COMMENT '用户UnionID',
  `token` varchar(500) NOT NULL COMMENT 'JWT Token',
  `expire_time` datetime NOT NULL COMMENT '过期时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`),
  KEY `idx_token` (`token`(255))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='小程序会话表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_miniapp_session`
--

LOCK TABLES `sys_miniapp_session` WRITE;
/*!40000 ALTER TABLE `sys_miniapp_session` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_miniapp_session` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
  `notice_id` int NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='通知公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
INSERT INTO `sys_notice` VALUES (1,'温馨提醒：2018-07-01 若依新版本发布啦','2',_binary '新版本内容','0','admin','2026-03-11 22:47:16','',NULL,'管理员'),(2,'维护通知：2018-07-01 若依系统凌晨维护','1',_binary '维护内容','0','admin','2026-03-11 22:47:16','',NULL,'管理员'),(10,'关于淘宝联盟API升级的通知','2',_binary '<p><span style=\"background-color: rgb(248, 249, 250); color: rgb(51, 51, 51);\">关于淘宝联盟API升级的通知</span></p>','0','admin','2026-04-04 16:49:25','',NULL,NULL),(11,'系统维护公告：4月5日凌晨2:00-4:00','2',_binary '<p><span style=\"background-color: rgb(248, 249, 250); color: rgb(51, 51, 51);\">系统维护公告：4月5日凌晨2:00-4:00</span></p>','0','admin','2026-04-04 16:49:40','',NULL,NULL),(12,'新增拼多多联盟平台支持','2',_binary '<p><span style=\"background-color: rgb(248, 249, 250); color: rgb(51, 51, 51);\">新增拼多多联盟平台支持</span></p>','0','admin','2026-04-04 16:49:54','',NULL,NULL),(13,'CPS系统上线了','2',_binary '<p>CPS系统上线了</p>','0','admin','2026-04-04 16:59:02','',NULL,NULL);
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(200) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `cost_time` bigint DEFAULT '0' COMMENT '消耗时间',
  PRIMARY KEY (`oper_id`),
  KEY `idx_sys_oper_log_bt` (`business_type`),
  KEY `idx_sys_oper_log_s` (`status`),
  KEY `idx_sys_oper_log_ot` (`oper_time`)
) ENGINE=InnoDB AUTO_INCREMENT=280 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oper_log`
--

LOCK TABLES `sys_oper_log` WRITE;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
INSERT INTO `sys_oper_log` VALUES (100,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createBy\":\"admin\",\"icon\":\"shopping\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"联盟管理\",\"menuType\":\"M\",\"orderNum\":0,\"params\":{},\"parentId\":0,\"path\":\"/union\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-12 16:57:34',22),(101,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createBy\":\"admin\",\"icon\":\"monitor\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"淘宝客\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"/union/tbk/shop/index\",\"perms\":\"union:tbk:shop\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-12 17:01:01',14),(102,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"/union/tbk/shop/index\",\"createTime\":\"2026-03-12 17:01:01\",\"icon\":\"monitor\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2001,\"menuName\":\"淘宝客\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"union\",\"perms\":\"union:tbk:shop\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-12 17:02:04',10),(103,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"union/tbk/shop/index\",\"createBy\":\"admin\",\"icon\":\"monitor\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"店铺搜索\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2001,\"path\":\"tbk\",\"perms\":\"union:tbk:shop\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-12 17:07:47',37),(104,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"\",\"createTime\":\"2026-03-12 17:01:01\",\"icon\":\"monitor\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2001,\"menuName\":\"淘宝客\",\"menuType\":\"M\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"union\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-12 17:08:17',20),(105,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"/union/tbk/dgMaterialOptionalUpgrade\",\"createBy\":\"admin\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"淘宝商品卡片\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":2001,\"path\":\"/tbk/dgMaterialOptionalUpgrade\",\"perms\":\"union:tbk:dg:material:recommend\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-12 23:45:46',474),(106,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"union/tbk/dgMaterialOptionalUpgrade\",\"createTime\":\"2026-03-12 23:45:46\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2003,\"menuName\":\"淘宝商品卡片\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":2001,\"path\":\"/tbk/dgMaterialOptionalUpgrade\",\"perms\":\"union:tbk:dg:material:recommend\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-12 23:45:56',23),(107,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"union/tbk/goods\",\"createTime\":\"2026-03-12 23:45:46\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2003,\"menuName\":\"淘宝商品卡片\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":2001,\"path\":\"/tbk/dgMaterialOptionalUpgrade\",\"perms\":\"union:tbk:dg:material:recommend\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-12 23:46:50',19),(108,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"union/tbk/goods/index\",\"createTime\":\"2026-03-12 23:45:46\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2003,\"menuName\":\"淘宝商品卡片\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":2001,\"path\":\"/tbk/dgMaterialOptionalUpgrade\",\"perms\":\"union:tbk:dg:material:recommend\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-12 23:46:59',38),(109,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"union/tbk/goods/index\",\"createTime\":\"2026-03-12 23:45:46\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2003,\"menuName\":\"淘宝商品卡片\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":2001,\"path\":\"tbk\",\"perms\":\"union:tbk:dg:material:recommend\",\"routeName\":\"\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"修改菜单\'淘宝商品卡片\'失败，路由名称或地址已存在\",\"code\":500}',0,NULL,'2026-03-12 23:48:08',13),(110,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"union/tbk/goods/index\",\"createTime\":\"2026-03-12 23:45:46\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2003,\"menuName\":\"淘宝商品卡片\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":2001,\"path\":\"tbk/goods\",\"perms\":\"union:tbk:dg:material:recommend\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-12 23:48:26',19),(111,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"union/tbk/goods/index\",\"createTime\":\"2026-03-12 23:45:46\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2003,\"menuName\":\"淘宝商品列表\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":2001,\"path\":\"tbk/goods\",\"perms\":\"union:tbk:dg:material:recommend\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-13 00:25:55',76),(112,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createBy\":\"admin\",\"icon\":\"skill\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"按钮权限\",\"menuType\":\"M\",\"orderNum\":30,\"params\":{},\"parentId\":2001,\"path\":\"/tbk/bution\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-13 00:32:07',19),(113,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"获取淘口令\",\"menuType\":\"F\",\"orderNum\":1,\"params\":{},\"parentId\":2004,\"perms\":\"union:tbk:tpwd\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-13 00:32:40',9),(114,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createTime\":\"2026-03-13 00:32:07\",\"icon\":\"skill\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2004,\"menuName\":\"按钮权限\",\"menuType\":\"M\",\"orderNum\":30,\"params\":{},\"parentId\":2001,\"path\":\"/tbk/bution\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"1\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-13 00:37:05',10),(115,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createTime\":\"2026-03-13 00:32:07\",\"icon\":\"skill\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2004,\"menuName\":\"按钮权限\",\"menuType\":\"M\",\"orderNum\":30,\"params\":{},\"parentId\":2001,\"path\":\"/tbk/bution\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"1\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-13 00:37:10',12),(116,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"union/tbk/promotion/index\",\"createBy\":\"admin\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"物料精选\",\"menuType\":\"C\",\"orderNum\":3,\"params\":{},\"parentId\":2001,\"path\":\"/tbk/promotion\",\"perms\":\"union:tbk:dg:optimus:promotion\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-13 00:41:08',10),(117,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"union/tbk/promotion/index\",\"createTime\":\"2026-03-13 00:41:08\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2006,\"menuName\":\"物料精选\",\"menuType\":\"C\",\"orderNum\":3,\"params\":{},\"parentId\":2001,\"path\":\"/tbk/promotion\",\"perms\":\"union:tbk:dg:optimus:promotion\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-13 00:41:43',27),(118,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"union/tbk/goods/index\",\"createTime\":\"2026-03-12 23:45:46\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2003,\"menuName\":\"淘宝商品列表\",\"menuType\":\"C\",\"orderNum\":2,\"params\":{},\"parentId\":2001,\"path\":\"tbk/goods\",\"perms\":\"union:tbk:dg:material:optional:upgrade\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-13 00:43:09',34),(119,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"union/tbk/promotion/index\",\"createTime\":\"2026-03-13 00:41:08\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2006,\"menuName\":\"物料精选\",\"menuType\":\"C\",\"orderNum\":3,\"params\":{},\"parentId\":2001,\"path\":\"tbk/promotion\",\"perms\":\"union:tbk:dg:optimus:promotion\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-13 00:44:44',19),(120,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"union/tbk/material/index\",\"createBy\":\"admin\",\"icon\":\"list\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"物料精选升级版\",\"menuType\":\"C\",\"orderNum\":4,\"params\":{},\"parentId\":2001,\"path\":\"tbk/material\",\"perms\":\"union:tbk:dg:material:recommend\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-13 00:46:32',26),(121,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createBy\":\"admin\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"物料id列表查询\",\"menuType\":\"F\",\"orderNum\":2,\"params\":{},\"parentId\":2004,\"perms\":\"union:tbk:optimus:tou:material:ids:get\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-13 01:01:18',155),(122,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"icon\":\"system\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"联盟系统管理\",\"menuType\":\"M\",\"orderNum\":0,\"params\":{},\"parentId\":1,\"path\":\"union\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"新增菜单\'联盟系统管理\'失败，路由名称或地址已存在\",\"code\":500}',0,NULL,'2026-03-15 11:09:21',17),(123,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createBy\":\"admin\",\"icon\":\"system\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"联盟系统管理\",\"menuType\":\"M\",\"orderNum\":0,\"params\":{},\"parentId\":1,\"path\":\"union/sys\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-15 11:09:32',17),(124,'菜单管理',1,'com.ruoyi.web.controller.system.SysMenuController.add()','POST',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"/system/tbkApply/index\",\"createBy\":\"admin\",\"icon\":\"system\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuName\":\"代理申请管理\",\"menuType\":\"C\",\"orderNum\":0,\"params\":{},\"parentId\":2009,\"path\":\"tbkApply/index\",\"perms\":\"system:tbkApply:list\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-15 11:17:32',13),(125,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/tbkApply/index\",\"createTime\":\"2026-03-15 11:17:32\",\"icon\":\"system\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2010,\"menuName\":\"代理申请管理\",\"menuType\":\"C\",\"orderNum\":0,\"params\":{},\"parentId\":2009,\"path\":\"tbkApply/index\",\"perms\":\"system:tbkApply:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-15 11:18:39',21),(126,'代理申请审核',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.audit()','PUT',1,'admin','研发部门','/system/tbkApply/audit/1','127.0.0.1','内网IP','{\"remark\":\"厉害\",\"status\":\"1\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-03-17 15:24:38',62),(127,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/unionPlatform/index\",\"createTime\":\"2026-04-03 10:31:40\",\"icon\":\"link\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2011,\"menuName\":\"联盟信息管理\",\"menuType\":\"C\",\"orderNum\":8,\"params\":{},\"parentId\":2009,\"path\":\"unionPlatform\",\"perms\":\"system:unionPlatform:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 10:57:25',75),(128,'联盟信息',1,'com.ruoyi.web.controller.system.SysUnionPlatformController.add()','POST',1,'admin','研发部门','/system/unionPlatform','127.0.0.1','内网IP','{\"adzoneId\":\"3103809080\",\"appKey\":\"xxxx\",\"appSecret\":\"****\",\"createBy\":\"admin\",\"id\":1,\"params\":{},\"platformType\":\"jd\",\"siteId\":\"4103428468\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 11:30:31',25),(129,'代理申请',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.changeStatus()','PUT',1,'admin','研发部门','/system/tbkApply/changeStatus','127.0.0.1','内网IP','{\"applyPlatformTypeList\":[],\"id\":1,\"params\":{},\"status\":\"1\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 12:34:24',225),(130,'代理申请',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.changeStatus()','PUT',1,'admin','研发部门','/system/tbkApply/changeStatus','127.0.0.1','内网IP','{\"applyPlatformTypeList\":[],\"id\":1,\"params\":{},\"status\":\"2\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:09:01',86),(131,'代理申请',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.changeStatus()','PUT',1,'admin','研发部门','/system/tbkApply/changeStatus','127.0.0.1','内网IP','{\"applyPlatformTypeList\":[],\"id\":1,\"params\":{},\"status\":\"1\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:09:07',34),(132,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createTime\":\"2026-04-03 12:22:20\",\"icon\":\"star\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2019,\"menuName\":\"联盟管理\",\"menuType\":\"M\",\"orderNum\":8,\"params\":{},\"parentId\":2009,\"path\":\"union\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"visible\":\"0\"} ','{\"msg\":\"修改菜单\'联盟管理\'失败，路由名称或地址已存在\",\"code\":500}',0,NULL,'2026-04-03 14:10:15',46),(133,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/2019','127.0.0.1','内网IP','2019 ','{\"msg\":\"存在子菜单,不允许删除\",\"code\":601}',0,NULL,'2026-04-03 14:10:26',6),(134,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createTime\":\"2026-04-03 12:22:20\",\"icon\":\"star\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2019,\"menuName\":\"联盟管理\",\"menuType\":\"M\",\"orderNum\":8,\"params\":{},\"parentId\":1,\"path\":\"union\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"1\",\"visible\":\"0\"} ','{\"msg\":\"修改菜单\'联盟管理\'失败，路由名称或地址已存在\",\"code\":500}',0,NULL,'2026-04-03 14:10:44',16),(135,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/2021','127.0.0.1','内网IP','2021 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:10:52',35),(136,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/2020','127.0.0.1','内网IP','2020 ','{\"msg\":\"存在子菜单,不允许删除\",\"code\":601}',0,NULL,'2026-04-03 14:10:57',6),(137,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/2022','127.0.0.1','内网IP','2022 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:11:01',33),(138,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/2023','127.0.0.1','内网IP','2023 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:11:04',23),(139,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/2024','127.0.0.1','内网IP','2024 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:11:09',25),(140,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/2025','127.0.0.1','内网IP','2025 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:11:12',30),(141,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/2020','127.0.0.1','内网IP','2020 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:11:16',28),(142,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/2019','127.0.0.1','内网IP','2019 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:11:19',25),(143,'代理申请',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.changeStatus()','PUT',1,'admin','研发部门','/system/tbkApply/changeStatus','127.0.0.1','内网IP','{\"applyPlatformTypeList\":[],\"id\":1,\"params\":{},\"status\":\"2\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:11:50',26),(144,'代理申请',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.changeStatus()','PUT',1,'admin','研发部门','/system/tbkApply/changeStatus','127.0.0.1','内网IP','{\"applyPlatformTypeList\":[],\"id\":1,\"params\":{},\"status\":\"1\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:18:24',58),(145,'代理申请',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.changeStatus()','PUT',1,'admin','研发部门','/system/tbkApply/changeStatus','127.0.0.1','内网IP','{\"applyPlatformTypeList\":[],\"id\":1,\"params\":{},\"status\":\"2\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:20:15',19),(146,'代理申请',3,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.remove()','DELETE',1,'admin','研发部门','/system/tbkApply/1','127.0.0.1','内网IP','[1] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:21:03',16),(147,'代理申请审核',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.audit()','PUT',1,'admin','研发部门','/system/tbkApply/audit/2','127.0.0.1','内网IP','{\"remark\":\"ceshi \",\"status\":\"2\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:27:26',66),(148,'代理申请审核',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.audit()','PUT',1,'admin','研发部门','/system/tbkApply/audit/2','127.0.0.1','内网IP','{\"remark\":\"嘎嘎叫\",\"status\":\"2\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:49:08',84),(149,'代理申请',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.changeStatus()','PUT',1,'admin','研发部门','/system/tbkApply/changeStatus','127.0.0.1','内网IP','{\"applyPlatformTypeList\":[],\"id\":2,\"params\":{},\"status\":\"1\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:49:43',27),(150,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [4,3] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:49:54',73),(151,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:50:07',45),(152,'代理申请',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.changeStatus()','PUT',1,'admin','研发部门','/system/tbkApply/changeStatus','127.0.0.1','内网IP','{\"applyPlatformTypeList\":[],\"id\":2,\"params\":{},\"status\":\"2\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 14:50:54',27),(153,'代理申请审核',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.audit()','PUT',1,'admin','研发部门','/system/tbkApply/audit/2','127.0.0.1','内网IP','{\"remark\":\"测试你啊\",\"status\":\"2\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 15:07:22',78),(154,'代理申请审核',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.audit()','PUT',1,'admin','研发部门','/system/tbkApply/audit/2','127.0.0.1','内网IP','{\"remark\":\"啊啊啊\",\"status\":\"2\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 15:08:51',29),(155,'代理申请审核',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.audit()','PUT',1,'admin','研发部门','/system/tbkApply/audit/2','127.0.0.1','内网IP','{\"remark\":\"你个铺盖仔\",\"status\":\"2\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 15:38:09',117),(156,'代理申请审核',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.audit()','PUT',1,'admin','研发部门','/system/tbkApply/audit/2','127.0.0.1','内网IP','{\"remark\":\"哈哈\",\"status\":\"1\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 15:38:50',31),(157,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [5,4,3] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 15:49:44',349),(158,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 15:49:51',40),(159,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [5] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 15:49:56',42),(160,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [2] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 15:50:02',29),(161,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [4] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 15:50:14',30),(162,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 15:50:20',24),(163,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [1,4] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:03:16',643),(164,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:03:24',61),(165,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [4] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:03:24',80),(166,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:03:27',39),(167,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [1,4] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:04:26',42),(168,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:04:36',51),(169,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [1,4] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:04:41',60),(170,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [4,1,6] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:04:48',90),(171,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:05:00',62),(172,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [1] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:05:14',35),(173,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:05:36',24),(174,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [2] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:14:54',44),(175,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:15:11',28),(176,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [2] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:15:33',33),(177,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:15:42',53),(178,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [2] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:15:56',44),(179,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [2,4] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:16:06',50),(180,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:17:24',37),(181,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [2] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:17:24',38),(182,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:35:12',36),(183,'联盟信息',1,'com.ruoyi.web.controller.system.SysUnionPlatformController.add()','POST',1,'admin','研发部门','/system/unionPlatform','127.0.0.1','内网IP','{\"adzoneId\":\"110571800148\",\"appKey\":\"XXXXX\",\"appSecret\":\"******\",\"params\":{},\"platformType\":\"tbk\"} ',NULL,1,'\r\n### Error updating database.  Cause: java.sql.SQLException: Field \'platform_name\' doesn\'t have a default value\r\n### The error may exist in file [D:\\workspace\\cps\\cps\\RuoYi-Vue\\ruoyi-system\\target\\classes\\mapper\\system\\SysUnionPlatformMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUnionPlatformMapper.insertSysUnionPlatform-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into sys_union_platform          ( platform_type,             app_key,             app_secret,                          adzone_id,                                                                 create_time,             update_time )           values ( ?,             ?,             ?,                          ?,                                                                 now(),             now() )\r\n### Cause: java.sql.SQLException: Field \'platform_name\' doesn\'t have a default value\n; Field \'platform_name\' doesn\'t have a default value','2026-04-03 16:40:43',582),(184,'联盟信息',1,'com.ruoyi.web.controller.system.SysUnionPlatformController.add()','POST',1,'admin','研发部门','/system/unionPlatform','127.0.0.1','内网IP','{\"adzoneId\":\"110571800148\",\"appKey\":\"XXXX\",\"appSecret\":\"0c434e5050ea97a52ea0121747e1eb6c\",\"params\":{},\"platformType\":\"tbk\"} ',NULL,1,'\r\n### Error updating database.  Cause: java.sql.SQLException: Field \'platform_name\' doesn\'t have a default value\r\n### The error may exist in file [D:\\workspace\\cps\\cps\\RuoYi-Vue\\ruoyi-system\\target\\classes\\mapper\\system\\SysUnionPlatformMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysUnionPlatformMapper.insertSysUnionPlatform-Inline\r\n### The error occurred while setting parameters\r\n### SQL: insert into sys_union_platform          ( platform_type,             app_key,             app_secret,                          adzone_id,                                                                 create_time,             update_time )           values ( ?,             ?,             ?,                          ?,                                                                 now(),             now() )\r\n### Cause: java.sql.SQLException: Field \'platform_name\' doesn\'t have a default value\n; Field \'platform_name\' doesn\'t have a default value','2026-04-03 16:40:50',5),(185,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [2] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:49:47',215),(186,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [1] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:54:19',32),(187,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 16:54:55',17),(188,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [2] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:00:21',757),(189,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [1] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:00:29',67),(190,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [2] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:00:35',67),(191,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [1] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:00:41',50),(192,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [1,4] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:00:46',81),(193,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [3] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:00:56',58),(194,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [3,2] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:01:20',65),(195,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:17:21',74),(196,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [2] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:17:21',224),(197,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:17:24',43),(198,'联盟信息',1,'com.ruoyi.web.controller.system.SysUnionPlatformController.add()','POST',1,'admin','研发部门','/system/unionPlatform','127.0.0.1','内网IP','{\"adzoneId\":\"110571800148\",\"appKey\":\"35315203\",\"appSecret\":\"0c434e5050ea97a52ea0121747e1eb6c\",\"id\":7,\"params\":{},\"platformName\":\"淘宝客001\",\"platformType\":\"tbk\",\"siteId\":\"\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:25:46',15),(199,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [7] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:29:11',27),(200,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:40:11',21),(201,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [7] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:40:16',35),(202,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:40:46',19),(203,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [7] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 17:40:50',21),(204,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 22:54:05',62),(205,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [7] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 22:54:11',51),(206,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [7,4] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:01:15',97),(207,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:08:21',151),(208,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [7] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:08:21',550),(209,'联盟信息',2,'com.ruoyi.web.controller.system.SysUnionPlatformController.edit()','PUT',1,'admin','研发部门','/system/unionPlatform','127.0.0.1','内网IP','{\"adzoneId\":\"3103809080\",\"appKey\":\"11f9e91ef53a7a7fbb58c65fc0f80011\",\"appSecret\":\"b46ebd3f98b64fc9929ead2d4f818d26\",\"createBy\":\"admin\",\"createTime\":\"2026-04-03 12:22:20\",\"id\":3,\"isQuote\":\"0\",\"params\":{},\"platformName\":\"京东联盟账号1\",\"platformType\":\"jd\",\"remark\":\"测试京东联盟账号\",\"siteId\":\"4103428468\",\"updateBy\":\"admin\",\"updateTime\":\"2026-04-03 17:17:21\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:09:14',19),(210,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [7,3] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:09:36',61),(211,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:10:03',37),(212,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [7] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:10:03',31),(213,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:10:08',34),(214,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [3] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:10:32',27),(215,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:11:04',21),(216,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [3] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:12:02',29),(217,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:14:10',19),(218,'联盟信息',2,'com.ruoyi.web.controller.system.SysUnionPlatformController.edit()','PUT',1,'admin','研发部门','/system/unionPlatform','127.0.0.1','内网IP','{\"adzoneId\":\"110571800148\",\"appKey\":\"35315203\",\"appSecret\":\"0c434e5050ea97a52ea0121747e1eb6c\",\"createBy\":\"admin\",\"createTime\":\"2026-04-03 17:25:46\",\"id\":7,\"isQuote\":\"0\",\"params\":{},\"platformName\":\"淘宝客正式使用\",\"platformType\":\"tbk\",\"remark\":\"淘宝客001系统写入\",\"siteId\":\"332212\",\"updateBy\":\"admin\",\"updateTime\":\"2026-04-03 23:10:08\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:15:57',12),(219,'联盟信息',2,'com.ruoyi.web.controller.system.SysUnionPlatformController.edit()','PUT',1,'admin','研发部门','/system/unionPlatform','127.0.0.1','内网IP','{\"adzoneId\":\"3103809080\",\"appKey\":\"11f9e91ef53a7a7fbb58c65fc0f80011\",\"appSecret\":\"b46ebd3f98b64fc9929ead2d4f818d26\",\"createBy\":\"admin\",\"createTime\":\"2026-04-03 12:22:20\",\"id\":3,\"isQuote\":\"0\",\"params\":{},\"platformName\":\"京东联盟正式使用\",\"platformType\":\"jd\",\"remark\":\"测试京东联盟账号\",\"siteId\":\"4103428468\",\"updateBy\":\"admin\",\"updateTime\":\"2026-04-03 23:14:10\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:16:28',23),(220,'联盟信息',2,'com.ruoyi.web.controller.system.SysUnionPlatformController.edit()','PUT',1,'admin','研发部门','/system/unionPlatform','127.0.0.1','内网IP','{\"adzoneId\":\"110571800148\",\"appKey\":\"35315203\",\"appSecret\":\"0c434e5050ea97a52ea0121747e1eb6c\",\"createBy\":\"admin\",\"createTime\":\"2026-04-03 17:25:46\",\"id\":7,\"isQuote\":\"0\",\"params\":{},\"platformName\":\"淘宝客正式使用\",\"platformType\":\"tbk\",\"remark\":\"淘宝客001系统写入\",\"siteId\":\"\",\"updateBy\":\"admin\",\"updateTime\":\"2026-04-03 23:15:57\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:16:41',8),(221,'联盟信息',3,'com.ruoyi.web.controller.system.SysUnionPlatformController.remove()','DELETE',1,'admin','研发部门','/system/unionPlatform/2','127.0.0.1','内网IP','[2] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:17:13',11),(222,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [7,3] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:17:30',44),(223,'用户管理',3,'com.ruoyi.web.controller.system.SysUserController.remove()','DELETE',1,'admin','研发部门','/system/user/2','127.0.0.1','内网IP','[2] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:21:58',19),(224,'用户管理',2,'com.ruoyi.web.controller.system.SysUserController.edit()','PUT',1,'admin','研发部门','/system/user','127.0.0.1','内网IP','{\"admin\":false,\"avatar\":\"\",\"createBy\":\"miniapp\",\"createTime\":\"2026-03-14 20:09:11\",\"delFlag\":\"0\",\"email\":\"\",\"loginIp\":\"\",\"nickName\":\"王小丫\",\"params\":{},\"phonenumber\":\"\",\"postIds\":[],\"roleIds\":[],\"roles\":[],\"sex\":\"0\",\"status\":\"0\",\"updateBy\":\"admin\",\"userId\":100,\"userName\":\"wx_TE_21GxM\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:37:13',79),(225,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createTime\":\"2026-03-11 22:47:11\",\"icon\":\"guide\",\"isCache\":\"0\",\"isFrame\":\"0\",\"menuId\":4,\"menuName\":\"若依官网\",\"menuType\":\"M\",\"orderNum\":4,\"params\":{},\"parentId\":0,\"path\":\"http://ruoyi.vip\",\"perms\":\"\",\"query\":\"\",\"routeName\":\"\",\"status\":\"1\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:37:56',56),(226,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/tbkApply/index\",\"createTime\":\"2026-03-15 11:17:32\",\"icon\":\"system\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2010,\"menuName\":\"代理申请管理\",\"menuType\":\"C\",\"orderNum\":0,\"params\":{},\"parentId\":2000,\"path\":\"tbkApply/index\",\"perms\":\"system:tbkApply:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:39:55',26),(227,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/unionPlatform/index\",\"createTime\":\"2026-04-03 10:31:40\",\"icon\":\"link\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2011,\"menuName\":\"联盟信息管理\",\"menuType\":\"C\",\"orderNum\":8,\"params\":{},\"parentId\":2000,\"path\":\"unionPlatform\",\"perms\":\"system:unionPlatform:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:40:07',25),(228,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"system/unionPlatform/index\",\"createTime\":\"2026-04-03 10:31:40\",\"icon\":\"link\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2011,\"menuName\":\"联盟信息管理\",\"menuType\":\"C\",\"orderNum\":1,\"params\":{},\"parentId\":2000,\"path\":\"unionPlatform\",\"perms\":\"system:unionPlatform:list\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:40:24',28),(229,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"component\":\"\",\"createTime\":\"2026-03-12 17:01:01\",\"icon\":\"monitor\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2001,\"menuName\":\"淘宝客\",\"menuType\":\"M\",\"orderNum\":2,\"params\":{},\"parentId\":2000,\"path\":\"union\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:40:31',32),(230,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/2009','127.0.0.1','内网IP','2009 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-03 23:40:35',22),(231,'用户管理',2,'com.ruoyi.web.controller.system.SysUserController.resetPwd()','PUT',1,'admin','研发部门','/system/user/resetPwd','127.0.0.1','内网IP','{\"admin\":false,\"params\":{},\"updateBy\":\"admin\",\"userId\":100} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-04 15:57:26',72),(232,'通知公告',1,'com.ruoyi.web.controller.system.SysNoticeController.add()','POST',1,'admin','研发部门','/system/notice','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"noticeContent\":\"<p><span style=\\\"background-color: rgb(248, 249, 250); color: rgb(51, 51, 51);\\\">关于淘宝联盟API升级的通知</span></p>\",\"noticeTitle\":\"关于淘宝联盟API升级的通知\",\"noticeType\":\"2\",\"params\":{},\"status\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-04 16:49:25',34),(233,'通知公告',1,'com.ruoyi.web.controller.system.SysNoticeController.add()','POST',1,'admin','研发部门','/system/notice','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"noticeContent\":\"<p><span style=\\\"background-color: rgb(248, 249, 250); color: rgb(51, 51, 51);\\\">系统维护公告：4月5日凌晨2:00-4:00</span></p>\",\"noticeTitle\":\"系统维护公告：4月5日凌晨2:00-4:00\",\"noticeType\":\"2\",\"params\":{},\"status\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-04 16:49:40',9),(234,'通知公告',1,'com.ruoyi.web.controller.system.SysNoticeController.add()','POST',1,'admin','研发部门','/system/notice','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"noticeContent\":\"<p><span style=\\\"background-color: rgb(248, 249, 250); color: rgb(51, 51, 51);\\\">新增拼多多联盟平台支持</span></p>\",\"noticeTitle\":\"新增拼多多联盟平台支持\",\"noticeType\":\"2\",\"params\":{},\"status\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-04 16:49:54',11),(235,'通知公告',1,'com.ruoyi.web.controller.system.SysNoticeController.add()','POST',1,'admin','研发部门','/system/notice','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"noticeContent\":\"<p>CPS系统上线了</p>\",\"noticeTitle\":\"CPS系统上线了\",\"noticeType\":\"2\",\"params\":{},\"status\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-04 16:59:02',8),(236,'字典类型',2,'com.ruoyi.web.controller.system.SysDictTypeController.edit()','PUT',1,'admin','研发部门','/system/dict/type','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2026-04-03 12:22:20\",\"dictId\":101,\"dictName\":\"联盟类型\",\"dictType\":\"union_platform_type\",\"params\":{},\"remark\":\"联盟平台类型列表\",\"status\":\"0\",\"updateBy\":\"admin\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-04 17:08:21',58),(237,'字典数据',1,'com.ruoyi.web.controller.system.SysDictDataController.add()','POST',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"default\":false,\"dictLabel\":\"抖音联盟\",\"dictSort\":5,\"dictType\":\"union_platform_type\",\"dictValue\":\"1007\",\"listClass\":\"default\",\"params\":{},\"status\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-04 17:10:07',21),(238,'字典数据',2,'com.ruoyi.web.controller.system.SysDictDataController.edit()','PUT',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2026-04-04 17:10:07\",\"default\":false,\"dictCode\":1010,\"dictLabel\":\"抖音联盟\",\"dictSort\":5,\"dictType\":\"union_platform_type\",\"dictValue\":\"1007\",\"isDefault\":\"N\",\"listClass\":\"warning\",\"params\":{},\"status\":\"0\",\"updateBy\":\"admin\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-04 17:10:27',11),(239,'字典数据',2,'com.ruoyi.web.controller.system.SysDictDataController.edit()','PUT',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2026-04-04 17:10:07\",\"default\":false,\"dictCode\":1010,\"dictLabel\":\"抖音联盟\",\"dictSort\":5,\"dictType\":\"union_platform_type\",\"dictValue\":\"1007\",\"isDefault\":\"N\",\"listClass\":\"danger\",\"params\":{},\"status\":\"0\",\"updateBy\":\"admin\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-04 17:10:32',20),(240,'字典数据',2,'com.ruoyi.web.controller.system.SysDictDataController.edit()','PUT',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2026-04-04 17:10:07\",\"default\":false,\"dictCode\":1010,\"dictLabel\":\"抖音联盟\",\"dictSort\":5,\"dictType\":\"union_platform_type\",\"dictValue\":\"1007\",\"isDefault\":\"N\",\"listClass\":\"danger\",\"params\":{},\"remark\":\"抖音精选联盟\",\"status\":\"0\",\"updateBy\":\"admin\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-04 17:10:52',20),(241,'字典数据',2,'com.ruoyi.web.controller.system.SysDictDataController.edit()','PUT',1,'admin','研发部门','/system/dict/data','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2026-04-04 17:10:07\",\"default\":false,\"dictCode\":1010,\"dictLabel\":\"抖音联盟\",\"dictSort\":5,\"dictType\":\"union_platform_type\",\"dictValue\":\"dy\",\"isDefault\":\"N\",\"listClass\":\"danger\",\"params\":{},\"remark\":\"抖音精选联盟\",\"status\":\"0\",\"updateBy\":\"admin\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-04 17:11:59',17),(242,'代理申请审核',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.audit()','PUT',1,'admin','研发部门','/system/tbkApply/audit/2','127.0.0.1','内网IP','{\"remark\":\"李老师猛\",\"status\":\"1\"}','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-08 10:29:24',40),(243,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-08 10:30:02',41),(244,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [3] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-08 10:30:03',31),(245,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-08 10:30:04',25),(246,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [7,3] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-08 10:30:12',37),(247,'联盟信息',2,'com.ruoyi.web.controller.system.SysUnionPlatformController.edit()','PUT',1,'admin','研发部门','/system/unionPlatform','127.0.0.1','内网IP','{\"adzoneId\":\"mm_127680077_1849900166_110571800148\",\"appKey\":\"69dxxxxxx\",\"appSecret\":\"*******\",\"createBy\":\"admin\",\"createTime\":\"2026-04-03 12:22:20\",\"id\":6,\"isQuote\":\"0\",\"params\":{},\"platformName\":\"大淘客正式账号\",\"platformType\":\"dtk\",\"remark\":\"测试大淘客账号\",\"updateBy\":\"admin\",\"updateTime\":\"2026-04-03 16:05:00\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-08 10:31:33',13),(248,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [3,7,6] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-08 10:31:46',65),(249,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/2037','127.0.0.1','内网IP','2037 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-09 11:26:40',41),(250,'菜单管理',3,'com.ruoyi.web.controller.system.SysMenuController.remove()','DELETE',1,'admin','研发部门','/system/menu/2036','127.0.0.1','内网IP','2036 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-09 11:26:44',23),(251,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createTime\":\"2026-04-09 09:33:19\",\"icon\":\"money\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2038,\"menuName\":\"佣金管理\",\"menuType\":\"M\",\"orderNum\":1,\"params\":{},\"parentId\":0,\"path\":\"commission\",\"perms\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-09 11:27:14',55),(252,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createTime\":\"2026-03-11 22:47:11\",\"icon\":\"system\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":1,\"menuName\":\"系统管理\",\"menuType\":\"M\",\"orderNum\":4,\"params\":{},\"parentId\":0,\"path\":\"system\",\"perms\":\"\",\"query\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-09 11:27:26',25),(253,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createTime\":\"2026-03-11 22:47:11\",\"icon\":\"monitor\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":2,\"menuName\":\"系统监控\",\"menuType\":\"M\",\"orderNum\":5,\"params\":{},\"parentId\":0,\"path\":\"monitor\",\"perms\":\"\",\"query\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-09 11:27:40',17),(254,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createTime\":\"2026-03-11 22:47:11\",\"icon\":\"tool\",\"isCache\":\"0\",\"isFrame\":\"1\",\"menuId\":3,\"menuName\":\"系统工具\",\"menuType\":\"M\",\"orderNum\":6,\"params\":{},\"parentId\":0,\"path\":\"tool\",\"perms\":\"\",\"query\":\"\",\"routeName\":\"\",\"status\":\"0\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-09 11:27:50',31),(255,'菜单管理',2,'com.ruoyi.web.controller.system.SysMenuController.edit()','PUT',1,'admin','研发部门','/system/menu','127.0.0.1','内网IP','{\"children\":[],\"createTime\":\"2026-03-11 22:47:11\",\"icon\":\"guide\",\"isCache\":\"0\",\"isFrame\":\"0\",\"menuId\":4,\"menuName\":\"若依官网\",\"menuType\":\"M\",\"orderNum\":7,\"params\":{},\"parentId\":0,\"path\":\"http://ruoyi.vip\",\"perms\":\"\",\"query\":\"\",\"routeName\":\"\",\"status\":\"1\",\"updateBy\":\"admin\",\"visible\":\"0\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-09 11:27:55',45),(256,'订单佣金',5,'com.ruoyi.web.controller.system.SysOrderCommissionController.export()','POST',1,'admin','研发部门','/system/orderCommission/export','127.0.0.1','内网IP','{\"pageSize\":\"10\",\"pageNum\":\"1\"}',NULL,1,'\r\n### Error querying database.  Cause: java.sql.SQLSyntaxErrorException: Unknown column \'estimated_commission\' in \'field list\'\r\n### The error may exist in file [D:\\workspace\\cps\\cps\\RuoYi-Vue\\ruoyi-system\\target\\classes\\mapper\\system\\SysOrderCommissionMapper.xml]\r\n### The error may involve com.ruoyi.system.mapper.SysOrderCommissionMapper.selectSysOrderCommissionList-Inline\r\n### The error occurred while setting parameters\r\n### SQL: select id, order_id, platform_type, item_id, item_title, order_amount,                 estimated_commission, actual_commission, commission_rate,                 buyer_user_id, has_referrer, referrer_user_id,                self_commission_rate, self_commission_amount,                 promotion_commission_rate, promotion_commission_amount,                order_status, settle_status, order_time, settle_time,                 create_time, update_time         from sys_order_commission                          order by create_time desc\r\n### Cause: java.sql.SQLSyntaxErrorException: Unknown column \'estimated_commission\' in \'field list\'\n; bad SQL grammar []','2026-04-09 14:47:46',1109),(257,'联盟信息',2,'com.ruoyi.web.controller.system.SysUnionPlatformController.edit()','PUT',1,'admin','研发部门','/system/unionPlatform','127.0.0.1','内网IP','{\"adzoneId\":\"44243516_315092954\",\"appKey\":\"xxxxx\",\"appSecret\":\"******\",\"createBy\":\"admin\",\"createTime\":\"2026-04-03 12:22:20\",\"id\":5,\"isQuote\":\"0\",\"params\":{},\"platformName\":\"拼多多联盟账号1\",\"platformType\":\"pdd\",\"remark\":\"测试拼多多联盟账号\",\"updateBy\":\"admin\",\"updateTime\":\"2026-04-03 15:49:56\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-09 15:06:38',50),(258,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [6,7,3,5] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-09 15:06:57',151),(259,'联盟信息',2,'com.ruoyi.web.controller.system.SysUnionPlatformController.edit()','PUT',1,'admin','研发部门','/system/unionPlatform','127.0.0.1','内网IP','{\"adzoneId\":\"44243516_315092954\",\"appKey\":\"df1d8d3ab0bb400c96216b765f3c77d0\",\"appSecret\":\"7a6c990a9dd1a155f9008389d52a554dcd2982ea\",\"createBy\":\"admin\",\"createTime\":\"2026-04-03 12:22:20\",\"id\":5,\"isQuote\":\"1\",\"params\":{},\"platformName\":\"拼多多联盟正式账号\",\"platformType\":\"pdd\",\"remark\":\"测试拼多多联盟账号\",\"updateBy\":\"admin\",\"updateTime\":\"2026-04-09 15:06:57\"} ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-09 15:07:19',11),(260,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-10 09:56:58',92),(261,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [5,7,6] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-10 09:56:58',72),(262,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [6,7,5,3] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-10 09:57:05',118),(263,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-10 09:58:44',52),(264,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [5,7,6] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-10 09:58:44',60),(265,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [6,7,5,3] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-10 09:58:54',87),(266,'PDD授权备案',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.pddAuthRecord()','POST',1,'admin','研发部门','/system/tbkApply/pddAuth/74','127.0.0.1','内网IP','74 ','{\"msg\":\"授权备案成功，但未获取到授权URL\",\"code\":500}',0,NULL,'2026-04-10 10:58:37',694),(267,'PDD授权备案',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.pddAuthRecord()','POST',1,'admin','研发部门','/system/tbkApply/pddAuth/74','127.0.0.1','内网IP','74 ','{\"msg\":\"授权备案成功，但未获取到授权URL\",\"code\":500}',0,NULL,'2026-04-10 11:03:53',563),(268,'PDD授权备案',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.pddAuthRecord()','POST',1,'admin','研发部门','/system/tbkApply/pddAuth/74','127.0.0.1','内网IP','74 ','{\"msg\":\"授权备案成功，但未获取到授权URL\",\"code\":500}',0,NULL,'2026-04-10 11:03:59',191),(269,'PDD授权备案',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.pddAuthRecord()','POST',1,'admin','研发部门','/system/tbkApply/pddAuth/74','127.0.0.1','内网IP','74 ','{\"msg\":\"授权备案成功，但未获取到授权URL\",\"code\":500}',0,NULL,'2026-04-10 11:05:29',61554),(270,'PDD授权备案',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.pddAuthRecord()','POST',1,'admin','研发部门','/system/tbkApply/pddAuth/74','127.0.0.1','内网IP','74 ','{\"msg\":\"操作成功\",\"code\":200,\"data\":{\"authUrl\":\"https://mobile.yangkeduo.com/duo_coupon_landing.html?__page=auth&pid=44243516_315092954&customParameters=%7B%22uid%22%3A%22f899139df5e1059396431415e770c6dd%22%7D&cpsSign=CC_260410_44243516_315092954_08f905f92ef804dc046d82f839da9f28&_x_ddjb_act=%7B%22st%22%3A%22102%22%7D&duoduo_type=2&launch_pdd=1&campaign=ddjb&cid=launch_\"}}',0,NULL,'2026-04-10 11:27:13',614),(271,'PDD授权备案',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.pddAuthRecord()','POST',1,'admin','研发部门','/system/tbkApply/pddAuth/74','127.0.0.1','内网IP','74 ','{\"msg\":\"操作成功\",\"code\":200,\"data\":{\"authUrl\":\"https://mobile.yangkeduo.com/duo_coupon_landing.html?__page=auth&pid=44243516_315092954&customParameters=%7B%22uid%22%3A%22f899139df5e1059396431415e770c6dd%22%7D&cpsSign=CC_260410_44243516_315092954_ffca06f8ec34326f9d3361681181a60f&_x_ddjb_act=%7B%22st%22%3A%22102%22%7D&duoduo_type=2&launch_pdd=1&campaign=ddjb&cid=launch_\"}}',0,NULL,'2026-04-10 12:00:21',694),(272,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-10 12:02:24',28),(273,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [3,7,6] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-10 12:02:24',42),(274,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [6,7,3,5] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-10 12:02:30',44),(275,'PDD授权备案',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.pddAuthRecord()','POST',1,'admin','研发部门','/system/tbkApply/pddAuth/82','127.0.0.1','内网IP','82 ','{\"msg\":\"操作成功\",\"code\":200,\"data\":{\"authUrl\":\"https://mobile.yangkeduo.com/duo_coupon_landing.html?__page=auth&pid=44243516_315092954&customParameters=%7B%22uid%22%3A%22f899139df5e1059396431415e770c6dd%22%7D&cpsSign=CC_260410_44243516_315092954_a5440634712c067ad750d692a3e6fe42&_x_ddjb_act=%7B%22st%22%3A%22102%22%7D&duoduo_type=2&launch_pdd=1&campaign=ddjb&cid=launch_\"}}',0,NULL,'2026-04-10 12:02:41',243),(276,'取消联盟分配',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.cancelAssignment()','DELETE',1,'admin','研发部门','/system/tbkApply/cancel/2','127.0.0.1','内网IP','2 ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-10 12:24:34',435),(277,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [3,7,6] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-10 12:24:34',145),(278,'分配联盟',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.assignUnionPlatforms()','POST',1,'admin','研发部门','/system/tbkApply/assign/2','127.0.0.1','内网IP','2 [6,7,3,5] ','{\"msg\":\"操作成功\",\"code\":200}',0,NULL,'2026-04-10 12:24:39',176),(279,'PDD授权备案',2,'com.ruoyi.web.controller.system.SysTbkAgentApplyController.pddAuthRecord()','POST',1,'admin','研发部门','/system/tbkApply/pddAuth/89','127.0.0.1','内网IP','89 ','{\"msg\":\"操作成功\",\"code\":200,\"data\":{\"authUrl\":\"https://mobile.yangkeduo.com/duo_coupon_landing.html?__page=auth&pid=44243516_315092954&customParameters=%7B%22uid%22%3A%22f899139df5e1059396431415e770c6dd%22%7D&cpsSign=CC_260410_44243516_315092954_48bdcc471eca5a4bbe72c6f39a935bac&_x_ddjb_act=%7B%22st%22%3A%22102%22%7D&duoduo_type=2&launch_pdd=1&campaign=ddjb&cid=launch_\"}}',0,NULL,'2026-04-10 12:24:45',371);
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_order_commission`
--

DROP TABLE IF EXISTS `sys_order_commission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_order_commission` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `order_id` varchar(100) NOT NULL COMMENT '订单ID',
  `platform_type` varchar(20) NOT NULL COMMENT '平台类型(tbk:淘宝客 jd:京东 pdd:拼多多)',
  `item_id` varchar(100) DEFAULT NULL COMMENT '商品ID',
  `item_title` varchar(500) DEFAULT NULL COMMENT '商品标题',
  `order_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单金额',
  `total_commission` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单总佣金',
  `agent_user_id` bigint NOT NULL COMMENT '下单代理用户ID',
  `self_commission_rate` decimal(5,2) NOT NULL DEFAULT '40.00' COMMENT '自购佣金比例(%)',
  `self_commission_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '自购佣金金额',
  `has_referrer` char(1) NOT NULL DEFAULT '0' COMMENT '是否有推荐人(0:无 1:有)',
  `referrer_user_id` bigint DEFAULT NULL COMMENT '推荐人用户ID',
  `promotion_commission_rate` decimal(5,2) DEFAULT '0.00' COMMENT '推广佣金比例(%)',
  `promotion_commission_amount` decimal(10,2) DEFAULT '0.00' COMMENT '推广佣金金额',
  `platform_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '平台收入',
  `order_status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '订单状态(pending:待结算 settled:已结算 cancelled:已取消)',
  `settle_time` datetime DEFAULT NULL COMMENT '结算时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_platform` (`order_id`,`platform_type`),
  KEY `idx_agent_user_id` (`agent_user_id`),
  KEY `idx_referrer_user_id` (`referrer_user_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单佣金记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_order_commission`
--

LOCK TABLES `sys_order_commission` WRITE;
/*!40000 ALTER TABLE `sys_order_commission` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_order_commission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_post`
--

DROP TABLE IF EXISTS `sys_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_post` (
  `post_id` bigint NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='岗位信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_post`
--

LOCK TABLES `sys_post` WRITE;
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
INSERT INTO `sys_post` VALUES (1,'ceo','董事长',1,'0','admin','2026-03-11 22:47:11','',NULL,''),(2,'se','项目经理',2,'0','admin','2026-03-11 22:47:11','',NULL,''),(3,'hr','人力资源',3,'0','admin','2026-03-11 22:47:11','',NULL,''),(4,'user','普通员工',4,'0','admin','2026-03-11 22:47:11','',NULL,'');
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','admin',1,'1',1,1,'0','0','admin','2026-03-11 22:47:11','',NULL,'超级管理员'),(2,'普通角色','common',2,'2',1,1,'0','0','admin','2026-03-11 22:47:11','',NULL,'普通角色');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_dept`
--

DROP TABLE IF EXISTS `sys_role_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和部门关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_dept`
--

LOCK TABLES `sys_role_dept` WRITE;
/*!40000 ALTER TABLE `sys_role_dept` DISABLE KEYS */;
INSERT INTO `sys_role_dept` VALUES (2,100),(2,101),(2,105);
/*!40000 ALTER TABLE `sys_role_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色和菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (2,1),(2,2),(2,3),(2,4),(2,100),(2,101),(2,102),(2,103),(2,104),(2,105),(2,106),(2,107),(2,108),(2,109),(2,110),(2,111),(2,112),(2,113),(2,114),(2,115),(2,116),(2,117),(2,500),(2,501),(2,1000),(2,1001),(2,1002),(2,1003),(2,1004),(2,1005),(2,1006),(2,1007),(2,1008),(2,1009),(2,1010),(2,1011),(2,1012),(2,1013),(2,1014),(2,1015),(2,1016),(2,1017),(2,1018),(2,1019),(2,1020),(2,1021),(2,1022),(2,1023),(2,1024),(2,1025),(2,1026),(2,1027),(2,1028),(2,1029),(2,1030),(2,1031),(2,1032),(2,1033),(2,1034),(2,1035),(2,1036),(2,1037),(2,1038),(2,1039),(2,1040),(2,1041),(2,1042),(2,1043),(2,1044),(2,1045),(2,1046),(2,1047),(2,1048),(2,1049),(2,1050),(2,1051),(2,1052),(2,1053),(2,1054),(2,1055),(2,1056),(2,1057),(2,1058),(2,1059),(2,1060);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_tbk_agent_apply`
--

DROP TABLE IF EXISTS `sys_tbk_agent_apply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tbk_agent_apply` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '申请ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `mini_app_id` varchar(100) NOT NULL COMMENT '微信小程序AppID',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `phone` varchar(20) NOT NULL COMMENT '手机号',
  `wechat` varchar(50) NOT NULL COMMENT '微信号',
  `invitation_code` varchar(20) DEFAULT NULL COMMENT '代理专属邀请码',
  `referrer_invitation_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '上级邀请码（注册时填写的邀请码，判断是否有上级的关键字段）',
  `commission_account_id` bigint DEFAULT NULL COMMENT '浣ｉ噾璐︽埛ID',
  `self_commission_rate` decimal(5,2) DEFAULT '40.00' COMMENT '鑷?喘浣ｉ噾姣斾緥(%)',
  `promotion_commission_rate` decimal(5,2) DEFAULT '5.00' COMMENT '鎺ㄥ箍浣ｉ噾姣斾緥(%)',
  `agent_level` int DEFAULT '1' COMMENT '代理级别(1-10级)',
  `can_invite_subordinate` char(1) DEFAULT '1' COMMENT '鏄?惁鍙?個璇蜂笅绾?0-涓嶅彲閭??,1-鍙?個璇?',
  `apply_platform_types` varchar(100) DEFAULT NULL COMMENT '申请的联盟类型(多个逗号分隔,如:tbk,jd,pdd)',
  `status` char(1) DEFAULT '0' COMMENT '申请状态(0-待审核,1-已生效,2-已失效)',
  `audit_by` varchar(64) DEFAULT NULL COMMENT '审核人',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_remark` varchar(500) DEFAULT NULL COMMENT '审核备注',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_mini_app_id` (`mini_app_id`),
  UNIQUE KEY `uk_invitation_code` (`invitation_code`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_referrer_invitation_code` (`referrer_invitation_code`),
  KEY `idx_agent_level` (`agent_level`),
  KEY `idx_commission_account_id` (`commission_account_id`),
  KEY `idx_can_invite` (`can_invite_subordinate`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='淘宝客代理申请表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_tbk_agent_apply`
--

LOCK TABLES `sys_tbk_agent_apply` WRITE;
/*!40000 ALTER TABLE `sys_tbk_agent_apply` DISABLE KEYS */;
INSERT INTO `sys_tbk_agent_apply` VALUES (2,100,'wx3c9a9ee6c1bbf894','王小丫','18609891321','aasretttt','3D364U',NULL,1,40.00,0.00,1,'1','tbk,jd,pdd,dtk,dy','1','admin','2026-04-08 10:29:25','李老师猛',NULL,'2026-04-03 14:21:07',NULL,'2026-04-08 10:29:25','AAAAAA');
/*!40000 ALTER TABLE `sys_tbk_agent_apply` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_union_platform`
--

DROP TABLE IF EXISTS `sys_union_platform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_union_platform` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `platform_type` varchar(20) NOT NULL COMMENT '联盟类型(tbk-淘宝客,jd-京东,pdd-拼多多,dtk-大淘客)',
  `platform_name` varchar(100) NOT NULL COMMENT '联盟名称',
  `app_key` varchar(200) NOT NULL COMMENT '应用Key/AppID',
  `app_secret` varchar(500) NOT NULL COMMENT '应用Secret',
  `site_id` varchar(100) DEFAULT NULL COMMENT '站点ID(淘宝客专用)',
  `adzone_id` varchar(100) DEFAULT NULL COMMENT '推广位ID',
  `access_token` varchar(500) DEFAULT NULL COMMENT '璁块棶浠ょ墝(鏌愪簺鑱旂洘闇??)',
  `is_quote` char(1) DEFAULT '0' COMMENT '使用状态(0-未被应用,1-被使用,2-失效)',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `status` char(1) DEFAULT '0' COMMENT '状态(0-正常,1-停用)',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_platform_type` (`platform_type`),
  KEY `idx_is_quote` (`is_quote`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='联盟信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_union_platform`
--

LOCK TABLES `sys_union_platform` WRITE;
/*!40000 ALTER TABLE `sys_union_platform` DISABLE KEYS */;
INSERT INTO `sys_union_platform` VALUES (1,'tbk','淘宝客账号1','12345678','test_secret_key_123456','12345678','109326500312',NULL,'0','测试淘宝客账号','0','admin','2026-04-03 12:22:20','admin','2026-04-03 17:00:56'),(3,'jd','京东联盟正式使用','11f9e91ef53a7a7fbb58c65fc0f80011','b46ebd3f98b64fc9929ead2d4f818d26','4103428468','3103809080',NULL,'1','测试京东联盟账号','0','admin','2026-04-03 12:22:20','admin','2026-04-10 12:24:39'),(4,'jd','京东联盟账号2','jd_app_key_002','jd_secret_002',NULL,'3040811002',NULL,'0','测试京东联盟账号','0','admin','2026-04-03 12:22:20','admin','2026-04-03 23:08:20'),(5,'pdd','拼多多联盟正式账号','df1d8d3ab0bb400c96216b765f3c77d0','7a6c990a9dd1a155f9008389d52a554dcd2982ea',NULL,'44243516_315092954',NULL,'1','测试拼多多联盟账号','0','admin','2026-04-03 12:22:20','admin','2026-04-10 12:24:39'),(6,'dtk','大淘客正式账号','69d3255ae90c7','9a8c967aa339eba1b25c5741388d1dac',NULL,'mm_127680077_1849900166_110571800148',NULL,'1','测试大淘客账号','0','admin','2026-04-03 12:22:20','admin','2026-04-10 12:24:39'),(7,'tbk','淘宝客正式使用','35315203','0c434e5050ea97a52ea0121747e1eb6c','','110571800148',NULL,'1','淘宝客001系统写入','0','admin','2026-04-03 17:25:46','admin','2026-04-10 12:24:39');
/*!40000 ALTER TABLE `sys_union_platform` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `pwd_update_date` datetime DEFAULT NULL COMMENT '密码最后更新时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,103,'admin','若依','00','ry@163.com','15888888888','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2026-04-10 12:00:03','2026-03-11 22:47:11','admin','2026-03-11 22:47:11','',NULL,'管理员'),(2,105,'ry','若依','00','ry@qq.com','15666666666','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','2','127.0.0.1','2026-03-11 22:47:11','2026-03-11 22:47:11','admin','2026-03-11 22:47:11','',NULL,'测试员'),(100,NULL,'wx_TE_21GxM','王小丫','00','','','0','','$2a$10$NufXytEjv7MMmT2zA.dPfecWmDuBNdiBsN4PRe9WfnrPNF7C2Oc3G','0','0','127.0.0.1','2026-04-04 15:57:39','2026-04-04 15:57:26','miniapp','2026-03-14 20:09:11','admin','2026-04-04 15:57:26',NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_post`
--

DROP TABLE IF EXISTS `sys_user_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户与岗位关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_post`
--

LOCK TABLES `sys_user_post` WRITE;
/*!40000 ALTER TABLE `sys_user_post` DISABLE KEYS */;
INSERT INTO `sys_user_post` VALUES (1,1);
/*!40000 ALTER TABLE `sys_user_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户和角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_wechat_user`
--

DROP TABLE IF EXISTS `sys_wechat_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_wechat_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `openid` varchar(100) NOT NULL COMMENT '微信openid',
  `unionid` varchar(100) DEFAULT NULL COMMENT '微信unionid',
  `nickname` varchar(100) DEFAULT NULL COMMENT '微信昵称',
  `avatar` varchar(500) DEFAULT NULL COMMENT '微信头像',
  `gender` varchar(1) DEFAULT NULL COMMENT '性别',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `country` varchar(50) DEFAULT NULL COMMENT '国家',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_openid` (`openid`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_unionid` (`unionid`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='微信用户关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_wechat_user`
--

LOCK TABLES `sys_wechat_user` WRITE;
/*!40000 ALTER TABLE `sys_wechat_user` DISABLE KEYS */;
INSERT INTO `sys_wechat_user` VALUES (1,100,'o6e5Y17hO4LC9PhttA1vTE_21GxM',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'2026-03-14 20:09:11',NULL,NULL,NULL);
/*!40000 ALTER TABLE `sys_wechat_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `v_agent_statistics`
--

DROP TABLE IF EXISTS `v_agent_statistics`;
