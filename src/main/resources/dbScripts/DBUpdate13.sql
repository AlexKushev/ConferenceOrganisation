ALTER TABLE `conferenceOrganisation`.`events` 
DROP COLUMN `isPublished`,
CHANGE COLUMN `description` `description` VARCHAR(256) NOT NULL ,
CHANGE COLUMN `price` `price` DOUBLE NOT NULL ,
CHANGE COLUMN `availableSeats` `availableSeats` INT(11) NOT NULL ,
ADD COLUMN `status` VARCHAR(45) NOT NULL DEFAULT 'NEW' AFTER `availableSeats`;
