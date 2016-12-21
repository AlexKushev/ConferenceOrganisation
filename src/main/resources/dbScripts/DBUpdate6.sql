ALTER TABLE `conferenceOrganisation`.`halls` 
CHANGE COLUMN `capacity` `capacity` INT(11) NOT NULL ,
CHANGE COLUMN `location` `location` VARCHAR(256) NOT NULL ,
ADD COLUMN `name` VARCHAR(256) NOT NULL AFTER `hallId`;
