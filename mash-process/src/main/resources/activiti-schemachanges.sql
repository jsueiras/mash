ALTER TABLE `activiti`.`act_ru_task` 
DROP FOREIGN KEY `ACT_FK_TASK_PROCINST`;

ALTER TABLE `activiti`.`act_ru_task` 
ADD CONSTRAINT `ACT_FK_TASK_PROCINST`
  FOREIGN KEY (`PROC_INST_ID_`)
  REFERENCES `activiti`.`act_ru_execution` (`ID_`)
  ON DELETE CASCADE;

ALTER TABLE `activiti`.`act_ru_task` 
DROP FOREIGN KEY `ACT_FK_TASK_EXE`;


ALTER TABLE `activiti`.`act_ru_task` 
ADD CONSTRAINT `ACT_FK_TASK_EXE`
  FOREIGN KEY (`EXECUTION_ID_`)
  REFERENCES `activiti`.`act_ru_execution` (`ID_`)
  ON DELETE CASCADE;