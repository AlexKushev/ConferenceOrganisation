ALTER TABLE `conferenceOrganisation`.`halls` 
CHANGE COLUMN `hallId` `hallId` INT(11) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `conferenceOrganisation`.`events` 
CHANGE COLUMN `date` `date` VARCHAR(256) NOT NULL ;

ALTER TABLE `conferenceOrganisation`.`lecture` 
CHANGE COLUMN `lectureId` `lectureId` INT(11) NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `eventId` `eventId` INT(11) NOT NULL ,
CHANGE COLUMN `title` `title` VARCHAR(256) NOT NULL ,
ADD COLUMN `lecturerId` INT(11) NOT NULL AFTER `lectureId`,
ADD INDEX `lecturer_id_fk_idx` (`lecturerId` ASC);
ALTER TABLE `conferenceOrganisation`.`lecture` 
ADD CONSTRAINT `lecturer_id_fk`
  FOREIGN KEY (`lecturerId`)
  REFERENCES `conferenceOrganisation`.`users` (`userId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `conferenceOrganisation`.`lecture` 
RENAME TO  `conferenceOrganisation`.`lectures` ;
