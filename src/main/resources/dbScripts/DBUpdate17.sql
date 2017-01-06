ALTER TABLE `conferenceorganisation`.`lectures` 
ADD COLUMN `isDeleted` TINYINT NOT NULL DEFAULT 0 AFTER `lecturerName`;
