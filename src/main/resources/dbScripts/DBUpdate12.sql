ALTER TABLE `conferenceOrganisation`.`lectures` 
CHANGE COLUMN `description` `description` TEXT NULL DEFAULT NULL ,
ADD COLUMN `lecturerEmail` VARCHAR(256) NOT NULL AFTER `description`,
ADD COLUMN `lecturerDescription` TEXT NOT NULL AFTER `lecturerEmail`,
ADD COLUMN `lecturerName` VARCHAR(256) NOT NULL AFTER `lecturerDescription`;

ALTER TABLE `conferenceOrganisation`.`lectures` 
DROP FOREIGN KEY `lecturer_id_fk`;
ALTER TABLE `conferenceOrganisation`.`lectures` 
DROP COLUMN `lecturerId`,
DROP INDEX `lecturer_id_fk_idx` ;
