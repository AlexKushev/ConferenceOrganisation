ALTER TABLE `conferenceOrganisation`.`events` 
DROP FOREIGN KEY `events_ibfk_1`;

ALTER TABLE `conferenceOrganisation`.`lecture` 
DROP FOREIGN KEY `lecture_ibfk_1`;

ALTER TABLE `conferenceOrganisation`.`events` 
CHANGE COLUMN `eventId` `eventId` INT(11) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `conferenceOrganisation`.`events` 
ADD COLUMN `creatorId` INT(11) NOT NULL AFTER `eventId`,
ADD INDEX `creatorId_fk_idx` (`creatorId` ASC);
ALTER TABLE `conferenceOrganisation`.`events` 
ADD CONSTRAINT `creatorId_fk`
  FOREIGN KEY (`creatorId`)
  REFERENCES `conferenceOrganisation`.`users` (`userId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
