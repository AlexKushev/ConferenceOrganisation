ALTER TABLE `conferenceOrganisation`.`userRoles` 
DROP FOREIGN KEY `userroles_ibfk_1`;

ALTER TABLE `conferenceOrganisation`.`users` 
CHANGE COLUMN `userId` `userId` INT(11) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `conferenceOrganisation`.`userRoles` 
ADD CONSTRAINT `userroles_ibfk_1`
  FOREIGN KEY (`userId`)
  REFERENCES `conferenceOrganisation`.`users` (`userId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
