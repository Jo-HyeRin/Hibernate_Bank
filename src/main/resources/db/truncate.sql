SET REFERENTIAL_INTEGRITY FALSE;
truncate table transaction;
truncate table account;
truncate table users;
SET REFERENTIAL_INTEGRITY TRUE;

/*
    걸려있는 모든 제약조건을 해제하고 다시 설정해주는 개념
*/