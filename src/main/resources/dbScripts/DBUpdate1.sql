ALTER TABLE `conferenceOrganisation`.`userRoles` 
DROP FOREIGN KEY `userroles_ibfk_2`;

ALTER TABLE `conferenceOrganisation`.`roles` 
CHANGE COLUMN `roleId` `roleId` INT(11) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `conferenceOrganisation`.`userRoles` 
ADD CONSTRAINT `userroles_ibfk_2`
  FOREIGN KEY (`roleId`)
  REFERENCES `conferenceOrganisation`.`roles` (`roleId`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
