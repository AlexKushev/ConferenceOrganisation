CREATE TABLE `conferenceOrganisation`.`tickets` (
  `ticketId` INT NOT NULL AUTO_INCREMENT,
  `ownerId` INT NOT NULL,
  `eventId` INT NOT NULL,
  PRIMARY KEY (`ticketId`),
  INDEX `ownerId_fk_idx` (`ownerId` ASC),
  INDEX `eventId_fk_idx` (`eventId` ASC),
  CONSTRAINT `ownerId_fk`
    FOREIGN KEY (`ownerId`)
    REFERENCES `conferenceOrganisation`.`users` (`userId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `eventId_fk`
    FOREIGN KEY (`eventId`)
    REFERENCES `conferenceOrganisation`.`events` (`eventId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
