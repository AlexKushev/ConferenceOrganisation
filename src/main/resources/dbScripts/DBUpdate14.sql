ALTER TABLE `conferenceOrganisation`.`events` 
ADD COLUMN `rating` DOUBLE NOT NULL DEFAULT 1 AFTER `status`,
ADD COLUMN `evaluatersCount` INT NOT NULL DEFAULT 0 AFTER `rating`;
