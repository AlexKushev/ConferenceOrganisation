ALTER TABLE `conferenceOrganisation`.`events` 
ADD CONSTRAINT `halld_fk`
  FOREIGN KEY (`hallId`)
  REFERENCES `conferenceOrganisation`.`halls` (`hallId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
