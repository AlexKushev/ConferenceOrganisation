ALTER TABLE `conferenceOrganisation`.`events` 
ADD COLUMN `isDeleted` TINYINT NOT NULL DEFAULT 0 AFTER `evaluatersCount`;
