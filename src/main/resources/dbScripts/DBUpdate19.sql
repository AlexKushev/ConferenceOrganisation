ALTER TABLE `conferenceOrganisation`.`lectures` 
ADD CONSTRAINT `eventId_fk_lecture`
  FOREIGN KEY (`eventId`)
  REFERENCES `conferenceOrganisation`.`events` (`eventId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
