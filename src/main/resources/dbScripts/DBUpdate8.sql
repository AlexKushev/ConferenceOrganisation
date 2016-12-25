ALTER TABLE `conferenceOrganisation`.`users` 
ADD COLUMN `isAdmin` TINYINT NOT NULL DEFAULT 0 AFTER `password`;
