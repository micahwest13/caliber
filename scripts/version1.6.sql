-- NEW TRAINEE COLUMNS FOR RED/GREEN FLAG
ALTER TABLE CALIBER_TRAINEE
  ADD FLAG_NOTES VARCHAR2(4000);
ALTER TABLE CALIBER_TRAINEE
  ADD FLAG_STATUS VARCHAR2(255) DEFAULT 'NONE';
  
-- NEW TABLES AND COLUMNS FOR TRAIN THE TRAINER CHECKLIST
--------------------------------------------------------
--  DDL for Table CALIBER_TASK
--------------------------------------------------------

  CREATE TABLE "CALIBER"."CALIBER_TASK" 
   (	"TASK_ID" NUMBER(10,0), 
	"IS_ACTIVE" NUMBER(1,0), 
	"TASK_DESCRIPTION" VARCHAR2(255 CHAR), 
	"TASK_PRIORITY" NUMBER(10,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" ;
--------------------------------------------------------
--  DDL for Index SYS_C0011695
--------------------------------------------------------

  CREATE UNIQUE INDEX "CALIBER"."SYS_C0011695" ON "CALIBER"."CALIBER_TASK" ("TASK_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" ;
--------------------------------------------------------
--  Constraints for Table CALIBER_TASK
--------------------------------------------------------

  ALTER TABLE "CALIBER"."CALIBER_TASK" ADD PRIMARY KEY ("TASK_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM"  ENABLE;
  ALTER TABLE "CALIBER"."CALIBER_TASK" MODIFY ("TASK_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  DDL for Table CALIBER_TASK_COMPLETION
--------------------------------------------------------

  CREATE TABLE "CALIBER"."CALIBER_TASK_COMPLETION" 
   (	"TASK_COMPLETION_ID" NUMBER(10,0), 
	"COMPLETION_DATE" DATE, 
	"CHECKED_BY" NUMBER(10,0), 
	"TASK_ID" NUMBER(10,0), 
	"TRAINER_ID" NUMBER(10,0)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" ;
--------------------------------------------------------
--  DDL for Index SYS_C0011700
--------------------------------------------------------

  CREATE UNIQUE INDEX "CALIBER"."SYS_C0011700" ON "CALIBER"."CALIBER_TASK_COMPLETION" ("TASK_COMPLETION_ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM" ;
--------------------------------------------------------
--  Constraints for Table CALIBER_TASK_COMPLETION
--------------------------------------------------------

  ALTER TABLE "CALIBER"."CALIBER_TASK_COMPLETION" ADD PRIMARY KEY ("TASK_COMPLETION_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM"  ENABLE;
  ALTER TABLE "CALIBER"."CALIBER_TASK_COMPLETION" MODIFY ("TRAINER_ID" NOT NULL ENABLE);
  ALTER TABLE "CALIBER"."CALIBER_TASK_COMPLETION" MODIFY ("CHECKED_BY" NOT NULL ENABLE);
  ALTER TABLE "CALIBER"."CALIBER_TASK_COMPLETION" MODIFY ("COMPLETION_DATE" NOT NULL ENABLE);
  ALTER TABLE "CALIBER"."CALIBER_TASK_COMPLETION" MODIFY ("TASK_COMPLETION_ID" NOT NULL ENABLE);
--------------------------------------------------------
--  Ref Constraints for Table CALIBER_TASK_COMPLETION
--------------------------------------------------------

  ALTER TABLE "CALIBER"."CALIBER_TASK_COMPLETION" ADD CONSTRAINT "FK_3FBXNU2RHAHET3WHDCLOX75DL" FOREIGN KEY ("TRAINER_ID")
	  REFERENCES "CALIBER"."CALIBER_TRAINER" ("TRAINER_ID") ENABLE;
  ALTER TABLE "CALIBER"."CALIBER_TASK_COMPLETION" ADD CONSTRAINT "FK_N0TMAP4M8FTAFKP3FUKXXL6T3" FOREIGN KEY ("CHECKED_BY")
	  REFERENCES "CALIBER"."CALIBER_TRAINER" ("TRAINER_ID") ENABLE;
  ALTER TABLE "CALIBER"."CALIBER_TASK_COMPLETION" ADD CONSTRAINT "FK_QQRRVSYR2UDC3M2T5NJI03TQH" FOREIGN KEY ("TASK_ID")
	  REFERENCES "CALIBER"."CALIBER_TASK" ("TASK_ID") ENABLE;
--------------------------------------------------------
--  DDL for Sequence TASK_COMPLETION_ID_SEQUENCE
--------------------------------------------------------

   CREATE SEQUENCE  "CALIBER"."TASK_COMPLETION_ID_SEQUENCE"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence TASK_ID_SEQUENCE
--------------------------------------------------------

   CREATE SEQUENCE  "CALIBER"."TASK_ID_SEQUENCE"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 CACHE 20 NOORDER  NOCYCLE ;
